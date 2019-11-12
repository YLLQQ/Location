package self.yang.location

import android.Manifest
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    // Create location services client
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        // 位置权限
        internal const val LOCATION_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    // 刷新位置信息
    fun refreshLocation(view: View) {
        updateLocation()
    }

    /**
     * 更新位置信息
     */
    private fun updateLocation() {
        // 检测是否拥有位置相关权限
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ), LOCATION_PERMISSION
        )

        //  create an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Get the last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->

            var locationTextView = findViewById<TextView>(R.id.location)

            var longitude = location?.longitude
            var latitude = location?.latitude

            locationTextView.text = "经度：$longitude \n纬度：$latitude"
        }
    }

}
