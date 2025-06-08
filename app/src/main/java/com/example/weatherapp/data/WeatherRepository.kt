package com.example.weatherapp.data

import com.example.weatherapp.openweather.OpenWeatherDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class WeatherRepository(
    private val openWeatherDataSource: OpenWeatherDataSource
) {

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val weatherJsonAdapter = moshi.adapter(Weather::class.java)
    private val forecastJsonAdapter = moshi.adapter(Forecast::class.java)

    suspend fun getCurrentWeather(city: String): Weather? {
        val response = openWeatherDataSource.getCurrentWeather(city)
        return response?.let { weatherJsonAdapter.fromJson(it) }
    }

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather? {
        val response = openWeatherDataSource.getCurrentWeather(latitude, longitude)
        return response?.let { weatherJsonAdapter.fromJson(it) }
    }

    suspend fun getForecast(city: String): List<Weather>? {
        val response = openWeatherDataSource.getForecast(city)
        return response?.let { forecastJsonAdapter.fromJson(it)?.list }
    }

    suspend fun getForecast(latitude: Double, longitude: Double): List<Weather>? {
        val response = openWeatherDataSource.getForecast(latitude, longitude)
        return response?.let { forecastJsonAdapter.fromJson(it)?.list }
    }

}