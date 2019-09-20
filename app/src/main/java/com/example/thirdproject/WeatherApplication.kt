package com.example.thirdproject

import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApplication :Application() {

    var service: Service? = null

    override fun onCreate() {
        Log.d("start", "Application onCreate")
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        setupRetrofit()
    }

    private fun setupRetrofit() {

        val httpClient = OkHttpClient.Builder()
        //NetworkInterceptor란 어플리케이션이 네트워크를 할건데 그 통신을 가로채는 역할
        //즉 서버나 컴퓨터와 통신을 할 때 그 통신을 가로채서 우리에게 보여주는역할
        //그 가로채는 역할을 Stetho가 할거다 라는 코드
        httpClient.addNetworkInterceptor(StethoInterceptor())
        val client = httpClient.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        service = retrofit.create(Service::class.java)
    }

    fun requestService(): Service? {
        return service
    }

    //chrome://inspect/#devices
}