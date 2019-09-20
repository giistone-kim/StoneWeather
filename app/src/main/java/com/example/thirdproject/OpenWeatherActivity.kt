package com.example.thirdproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
//import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_open_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeatherActivity : AppCompatActivity(), LocationListener {

    private val PERMISSION_REQUEST_CODE = 2000
    private val APP_ID = "3ffe66b90c6bcf38a5ccb7e6237c8d91"

    //섭씨정보를 요청하는 value = metric
    private val UNITS = "METRIC"
    private val LANGUAGE = "KR"



    private lateinit var backPressHolder: OnBackPressHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_weather)


        //뒤로가기로 어플종료 실행
        backPressHolder = OnBackPressHolder()
        getLocationInfo()

        setting.setOnClickListener {
            startActivity(Intent(this, AccountSettingActivity::class.java))


        }
    }

    //뒤로가기 종료 오버라이드
    override fun onBackPressed() {
       backPressHolder.onBackPressed()
    }


    private fun drawCurrentWeather(currentWeather: TotalWeather) {
        with(currentWeather) {
            this.name?.let {current_location.text = it

            }
            this.weatherList?.getOrNull(0)?.let {
                it.icon?.let {
                    val glide = Glide.with(this@OpenWeatherActivity)
                    glide
                        .load(Uri.parse("https://openweathermap.org/img/w/" + it + ".png"))
                        .into(current_icon)
                }

                it.main?.let { current_description.text = it }
                it.description?.let { current_main.text = it }

            }


            this.main?.temp?.let { current_now.text = String.format("%.1f", it) }
            this.main?.tempMax?.let { current_max.text = String.format("%.1f", it) }
            this.main?.tempMin?.let { current_min.text = String.format("%.1f", it) }


            loading_view.visibility = View.GONE
            weather_view.visibility = View.VISIBLE

        }
    }

    private fun getLocationInfo() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                this@OpenWeatherActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@OpenWeatherActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        } else {
            val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                requestWeatherInfoOfLocation(latitude = latitude, longitude = longitude)
            } else {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, //NETWORK_PROVIDER를 사용해도된다.
                    0L,
                    0F,
                    this
                )
                locationManager.removeUpdates(this)
            }
        }
    }

    private fun requestWeatherInfoOfLocation(latitude: Double, longitude: Double) {
        (application as WeatherApplication)
            .requestService()
            ?.getWeatherInfoOfCoordinates(
                latitude = latitude,
                longitude = longitude,
                appID = APP_ID,
                units = UNITS,
                language = LANGUAGE
            )
            ?.enqueue(object : Callback<TotalWeather> {
                override fun onFailure(call: Call<TotalWeather>, t: Throwable) {
                    loading_text.text = "로딩 실패"
                }

                override fun onResponse(
                    call: Call<TotalWeather>,
                    response: Response<TotalWeather>
                ) {
                    //response 성공했을시에만 totalWeather 만들어줌
                    if (response.isSuccessful) {
                        //가져올 정보가 json body()에 있으므로
                        val totalWeather = response.body()
                        totalWeather?.let {
                            drawCurrentWeather(it)
                        }
                    }else loading_text.text = "로딩 실패"
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (requestCode == Activity.RESULT_OK) getLocationInfo()
        }
    }

    override fun onLocationChanged(location: Location?) {
        val latitude = location?.latitude
        val longitude = location?.longitude
        if (latitude != null && longitude != null) {
            requestWeatherInfoOfLocation(latitude = latitude, longitude = longitude)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }


    //뒤로가기로 어플 종료
    inner class OnBackPressHolder() {
        private var backPressHolder: Long = 0

        fun onBackPressed() {
            if (System.currentTimeMillis() > backPressHolder + 2000) {//2000은 2초 의미 currnetimemill 은 현재시간의미
                backPressHolder = System.currentTimeMillis()
                showBackToast()
                return
            }

            if(System.currentTimeMillis() <= backPressHolder + 2000){
                finishAffinity()
            }
        }

        fun showBackToast() {
            Toast.makeText(this@OpenWeatherActivity, "한번 더 누르시면 종료합니다", Toast.LENGTH_SHORT).show()
        }

    }
}
