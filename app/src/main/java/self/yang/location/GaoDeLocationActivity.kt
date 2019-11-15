package self.yang.location

import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener


class GaoDeLocationActivity : AppCompatActivity() {

    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null
    var mLocationListener: AMapLocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gao_de_location)

        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        //设置定位回调监听
        mLocationClient?.setLocationListener(mLocationListener);

        mLocationClient?.startLocation()
    }

    // 展示位置信息
    private fun handleLocation(aMapLocation: AMapLocation?) {
        var gaodeTextView = findViewById<TextView>(R.id.gaodeTextView)

        var longitude = aMapLocation?.longitude
        var latitude = aMapLocation?.latitude

        gaodeTextView.text = "$longitude $latitude"
    }
}
