package self.yang.location

import android.Manifest
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*


class GaodeActivity : AppCompatActivity() {

    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null
    var mLocationListener: AMapLocationListener? = null
    //高德地图视图
    var mMapView: MapView? = null
    var aMap: AMap? = null

    companion object {
        // 我的老家
        var sxz = LatLng(33.066929, 112.839609)
        // 琼琼老家
        var xw = LatLng(34.464351, 110.909972)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaode)

        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
            ), MainActivity.LOCATION_PERMISSION
        )

        //初始化定位
        mLocationClient = AMapLocationClient(getApplicationContext());

        //声明定位回调监听器
        mLocationListener = AMapLocationListener { aMapLocation ->
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //解析定位结果
                    handleLocation(aMapLocation)
                } else {
                    var gaodeTextView = findViewById<TextView>(R.id.gaodeTextView)

                    gaodeTextView.text = aMapLocation.getErrorCode().toString()
                }
            }
        }

        //获取地图控件引用
        mMapView = findViewById<MapView>(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView?.onCreate(savedInstanceState)

        if (aMap == null) {
            aMap = mMapView?.getMap();
            setMap();
        }

        //设置定位回调监听
        mLocationClient?.setLocationListener(mLocationListener);

        mLocationClient?.startLocation()
    }

    /**
     * 设置地图样式
     */
    private fun setMap() {
        //设置地图的放缩级别
        aMap?.moveCamera(CameraUpdateFactory.zoomTo(13F));

        //初始化定位蓝点样式类
        var myLocationStyle = MyLocationStyle()
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle?.interval(1000)
        //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle?.showMyLocation(true)
        //设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        //myLocationStyle?.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(
        //resources,R.drawable.flag)))

        //设置定位蓝点的Style
        aMap?.myLocationStyle = myLocationStyle
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap?.isMyLocationEnabled = true

        initMarker()

        val latLngs = ArrayList<LatLng>()

        latLngs.add(sxz)
        latLngs.add(xw)

        createLine(latLngs)
    }

    /**
     * 根据经纬度绘制线
     */
    private fun createLine(latLngs: ArrayList<LatLng>) {
        aMap?.addPolyline(
            PolylineOptions().addAll(latLngs).geodesic(true).width(1f).color(
                Color.argb(
                    255,
                    1,
                    1,
                    1
                )
            )
        )
    }

    /**
     * 初始化标记点
     */
    private fun initMarker() {
        addMarker(sxz, "我的老家");
        addMarker(xw, "琼琼老家")
    }

    /**
     * 增加标记点信息
     */
    private fun addMarker(latLng: LatLng, title: String) {
        val markerOption = MarkerOptions()

        markerOption.position(latLng)
        markerOption.title(title)
        markerOption.draggable(true)//设置Marker可拖动
        markerOption.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory
                    .decodeResource(resources, android.R.drawable.presence_online)
            )
        )
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.isFlat = true//设置marker平贴地图效果

        aMap?.addMarker(markerOption)
    }

    override fun onPause() {
        super.onPause()

        mLocationClient?.unRegisterLocationListener(mLocationListener)

        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView?.onDestroy()

        if (null != mLocationClient) {
            mLocationClient?.onDestroy();
        }
    }

    override fun onResume() {
        super.onResume()

        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView?.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        mMapView?.onSaveInstanceState(outState)
    }

    // 展示位置信息
    private fun handleLocation(aMapLocation: AMapLocation?) {
        var gaodeTextView = findViewById<TextView>(R.id.gaodeTextView)

        var longitude = aMapLocation?.longitude
        var latitude = aMapLocation?.latitude

        gaodeTextView.text = "$longitude $latitude"
    }
}
