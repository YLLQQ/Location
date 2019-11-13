package self.yang.location

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle


class GaodeActivity : AppCompatActivity() {

    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null
    var mLocationListener: AMapLocationListener? = null
    //高德地图视图
    var mMapView: MapView? = null
    var aMap: AMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaode)

        //初始化定位
        mLocationClient = AMapLocationClient(getApplicationContext());

        //声明定位回调监听器
        mLocationListener = AMapLocationListener { aMapLocation ->
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //解析定位结果
                    handleLocation(aMapLocation)
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

    private fun setMap() {
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        var myLocationStyle = MyLocationStyle()
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle?.interval(2000)

        //设置定位蓝点的Style
        aMap?.setMyLocationStyle(myLocationStyle)
        //设置默认定位按钮是否显示，非必需设置。
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap?.setMyLocationEnabled(true)
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
