package self.yang.location

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import self.yang.location.data.config.AppDatabase
import self.yang.location.data.entity.LocationEntity


class MainActivity : AppCompatActivity() {

    // Create location services client
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationListView: ListView

    companion object {
        // 位置权限
        internal const val LOCATION_PERMISSION = 1
    }

    /**
     * 刷新位置列表
     */
    private var refreshLocationListViewHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            var obj = msg.obj as Array<LocationEntity>

            val adapter =
                ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_list_item_1,
                    obj
                )

            locationListView.adapter = adapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 隐藏状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 检测是否拥有位置相关权限
        requestPermissions(
            arrayOf(
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ), LOCATION_PERMISSION
        )

        //  create an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 本地位置设置
        locationRequest = LocationRequest.create()?.apply {
            interval = 100000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!

        // 定义位置信息更新回调
        // 一体化位置信息提供器会调用 LocationCallback.onLocationResult() 回调方法。
        // 传入参数包含 Location 对象列表，其中包含位置的纬度和经度。
        // 以下代码段展示了如何实现 LocationCallback 接口并定义该方法，然后获取位置信息更新的时间戳，并在应用的界面上显示纬度、经度和时间戳：
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                handleLocation(locationResult.lastLocation)
            }
        }

        locationListView = findViewById<ListView>(R.id.locationListView)

        // this.showNewLocation()
    }

    override fun onResume() {
        super.onResume()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper() /* Looper */
        )

    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    /**
     * 要停止位置信息更新
     */
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    // 刷新位置信息
    fun refreshLocation(view: View) {
        showNewLocation()
    }

    // 访问高德ativity
    fun gotoGaode(view: View) {
        var intent = Intent(this, GaodeActivity::class.java)

        startActivity(intent)
    }

    /**
     * 展示最新位置信息
     */
    private fun showNewLocation() {
        // Get the last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            handleLocation(location)
        }
    }

    // 展示位置信息
    private fun handleLocation(location: Location?) {
        var longitudeTextView = findViewById<TextView>(R.id.longitude)
        var latitudeTextView = findViewById<TextView>(R.id.latitude)

        var longitude = location?.longitude.toString()
        var latitude = location?.latitude.toString()

        longitudeTextView.text = longitude
        latitudeTextView.text = latitude

        //Log.d("Main Activity", "current location is $longitude $latitude")

        Thread(Runnable {
            kotlin.run {
                var locationEntity =
                    LocationEntity(null, longitude.toDouble(), latitude.toDouble())

                AppDatabase.getInstance(applicationContext).locationDao()
                    .insertLocation(locationEntity)

                Log.d("Main Activity", "insert location success")

                var locationList =
                    AppDatabase.getInstance(applicationContext).locationDao().loadAllLocation()

                Log.d("Main Activity", "list is $locationList")

                for (locationEntity in locationList) {
                    Log.d("Main Activity", "location is $locationEntity")
                }

                var message = Message.obtain()

                message.obj = locationList

                refreshLocationListViewHandler.sendMessage(message)
            }
        }).start()
    }


}
