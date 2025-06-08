package com.example.weatherapp.openweather

import com.example.weatherapp.openweather.internal.OpenWeatherApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class OpenWeatherDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            var request = chain.request()
            val url: HttpUrl = request
                .url
                .newBuilder()
                .addQueryParameter("units", "metric")
                .addQueryParameter("appid", BuildConfig.OPEN_WEATHER_API_KEY)
                .build()
            request = request.newBuilder().url(url).build()
            return@addInterceptor chain.proceed(request)
        }
        .build()

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .client(client)
        .build()

    private val openWeatherApi = retrofit.create(OpenWeatherApi::class.java)

    suspend fun getCurrentWeather(city: String): String? = withContext(dispatcher) {
        try {
            return@withContext openWeatherApi.getCurrentWeather(city).string()
        } catch (e: Exception) {
            return@withContext null
        }
    }

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): String? = withContext(dispatcher) {
        try {
            return@withContext openWeatherApi.getCurrentWeather(latitude, longitude).string()
        } catch (e: Exception) {
            return@withContext null
        }
    }

    suspend fun getForecast(city: String): String? = withContext(dispatcher) {
        try {
            return@withContext openWeatherApi.getForecast(city).string()
        } catch (e: Exception) {
            return@withContext null
        }
    }

    suspend fun getForecast(latitude: Double, longitude: Double): String? = withContext(dispatcher) {
        try {
            return@withContext openWeatherApi.getForecast(latitude, longitude).string()
        } catch (e: Exception) {
            return@withContext null
        }
    }

}