package com.example.thirdproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    //요청하고싶은 api 주소들 (get, put 인지 잘 봐)
        @GET("/data/2.5/weather/")
        fun getWeatherInfoOfLocation(
                @Query("q") location: String,
                @Query("APPID") appID: String
            ): Call<TotalWeather>  //<>안에는  내가 이요청을 했을때  그 레스폰스를 어떤 타입으로 파싱해서
                                    //받을지 적어주는 자리

        @GET("/data/2.5/weather/")
        fun getWeatherInfoOfCoordinates(
            @Query("lat")latitude :  Double,
            @Query("lon")longitude : Double,
            @Query("APPID")appID : String,
            @Query("units")units : String,
            @Query("lang")language : String
        ): Call<TotalWeather>

}