package self.yang.location

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.MapView


class GaodeActivity : AppCompatActivity() {

    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null
    var mLocationListener: AMapLocationListener? = null
    //高德地图视图
    var mMapView: MapView? = null


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

        //设置定位回调监听
        mLocationClient?.setLocationListener(mLocationListener);

        mLocationClient?.startLocation()

        //获取地图控件引用
        mMapView = findViewById<MapView>(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView?.onCreate(savedInstanceState)
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
