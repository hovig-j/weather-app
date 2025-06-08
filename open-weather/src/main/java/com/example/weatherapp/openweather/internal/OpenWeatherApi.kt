package com.example.weatherapp.openweather.internal

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("weather")
    suspend fun getCurrentWeather(@Query("q") city: String): ResponseBody

    @GET("weather")
    suspend fun getCurrentWeather(@Query("lat") latitude: Double, @Query("lon") longitude: Double): ResponseBody

    @GET("forecast")
    suspend fun getForecast(@Query("q") city: String): ResponseBody

    @GET("forecast")
    suspend fun getForecast(@Query("lat") latitude: Double, @Query("lon") longitude: Double): ResponseBody

}