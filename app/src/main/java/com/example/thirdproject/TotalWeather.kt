package com.example.thirdproject

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.jar.Attributes

//TotalWeather 가 json 가장 상위객체
class TotalWeather (

    //json의 키 이름과 동일하게 해야한다.
    var main : Main? = null,
    var name : String? = null,
    @SerializedName("weather")
    var weatherList : ArrayList<Weather>? = null
//: Serializable를 붙여야지만 각각의 벨류에 맞는 값들을 넣어주게된다. 꼭넣어야함
): Serializable
class Weather(
    var description: String? = null,
    var icon : String?= null,
    var main : String? = null

): Serializable
class Main(
    var humidity : Int? = null,
    var pressure : Int? = null,
    var temp : Float? = null,
    @SerializedName("temp_max")
    //변수이름에 언더바 들어가면 스네이크케이스, 아니면 카멜케이스
    var tempMax : Float? = null,
    @SerializedName("temp_min")
    var tempMin : Float? = null

    ): Serializable




