package self.yang.location

import android.Manifest
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView


class GaoDeMapActivity : AppCompatActivity() {

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

        val mapView = findViewById(R.id.map) as MapView

        mapView.onCreate(savedInstanceState)// 此方法必须重写

        val aMap = mapView.map

        aMap.mapType = AMap.MAP_TYPE_NORMAL
    }
}
