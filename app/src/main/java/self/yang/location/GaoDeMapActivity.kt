package self.yang.location

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle


class GaoDeMapActivity : AppCompatActivity() {

    private var aMap: AMap? = null
    private var mMapView: MapView? = null

    companion object {
        // 我的老家
        var sxz = LatLng(33.066929, 112.839609)
        // 琼琼老家
        var xw = LatLng(34.464351, 110.909972)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 隐藏状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_gao_de_map)

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

        mMapView = findViewById<MapView>(R.id.map)

        mMapView!!.onCreate(savedInstanceState)// 此方法必须重写

        aMap = mMapView!!.map

        if (aMap != null) {
            setMap()
        }
    }

    /**
     * 设置地图样式
     */
    private fun setMap() {
        setLocationStyle()

        setMarker(sxz)
        setMarker(xw)
    }

    /**
     * 设置标记点
     */
    private fun setMarker(latLng: LatLng) {
        val markerOption = MarkerOptions()

        markerOption.position(latLng)

        markerOption.draggable(true)//设置Marker可拖动

        markerOption.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.flag
                )
            )
        )

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.isFlat = true//设置marker平贴地图效果

        aMap!!.addMarker(markerOption)
    }

    /**
     * 设置定位点样式
     */
    private fun setLocationStyle() {
        val myLocationStyle: MyLocationStyle = MyLocationStyle()

        myLocationStyle.interval(2000L)
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)

        aMap!!.myLocationStyle = myLocationStyle
        aMap!!.isMyLocationEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView!!.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView!!.onSaveInstanceState(outState)
    }
}
