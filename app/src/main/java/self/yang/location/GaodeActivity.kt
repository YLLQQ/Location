package self.yang.location

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener


class GaodeActivity : AppCompatActivity() {

    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaode)

        //初始化定位
        mLocationClient = AMapLocationClient(getApplicationContext());

        //声明定位回调监听器
        var mLocationListener = AMapLocationListener { aMapLocation ->
            Log.d("Gaode", "$aMapLocation")

            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //解析定位结果

                }
            }
        }

        //设置定位回调监听
        mLocationClient!!.setLocationListener(mLocationListener);

        mLocationClient!!.startLocation()
    }
}
