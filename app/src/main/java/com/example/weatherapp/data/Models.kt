package com.example.weatherapp.data

import com.squareup.moshi.Json

data class Forecast(
    val list: List<Weather>
)

data class Weather(
    val dt: Long,
    @Json(name = "main")
    val temperatures: Temperatures,
    @Json(name = "weather")
    val summary: List<Summary>,
    val clouds: Clouds,
    val wind: Wind,
    val rain: Rain?,
    @Json(name = "pop")
    val precipitation: Double?
)

data class Summary(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Temperatures(
    @Json(name = "temp")
    val temperature: Double,
    @Json(name = "temp_min")
    val minTemperature: Double,
    @Json(name = "temp_max")
    val maxTemperature: Double,
    @Json(name = "feels_like")
    val feelsLike: Double
)

data class Rain(
    @Json(name = "1h")
    val volumePerHour: Double?,
    @Json(name = "3h")
    val volumePer3: Double?
)

data class Wind(
    val speed: Double
)

data class Clouds(
    @Json(name = "all")
    val cloudiness: Int
)
