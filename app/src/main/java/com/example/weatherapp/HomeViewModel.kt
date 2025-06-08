package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.Weather
import com.example.weatherapp.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val loadingState: LoadingState = LoadingState.INITIAL,
    val location: String = "",
    val currentWeather: Weather? = null,
    val forecast: List<Weather> = listOf()
)

enum class LoadingState {
    INITIAL,
    LOADING,
    LOADED,
    ERROR
}

class HomeViewModel(
    application: Application,
    private val weatherRepository: WeatherRepository
) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("weather_app_shared_pref", Context.MODE_PRIVATE)
    private val lastSavedCity = sharedPreferences.getString("last_city", null)
    private val lastSavedCoordinates = sharedPreferences.getString("last_coordinates", null)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        lastSavedCity?.let {
            setCity(it)
        } ?: lastSavedCoordinates?.let {
            val coordinates = it.split("|")
            val latitude = coordinates.first().toDouble()
            val longitude = coordinates.last().toDouble()
            setCoordinates(latitude, longitude)
        }
    }

    fun setCity(city: String) {
        setLoadingState()
        viewModelScope.launch {
            val currentWeather = weatherRepository.getCurrentWeather(city)
            val forecast = weatherRepository.getForecast(city)
            if (currentWeather != null || forecast != null) {
                sharedPreferences.edit {
                    remove("last_coordinates")
                    putString("last_city", city)
                }
            }
            updateUiState(city, currentWeather, forecast)
        }
    }

    fun setCoordinates(latitude: Double, longitude: Double) {
        setLoadingState()
        viewModelScope.launch {
            val currentWeather = weatherRepository.getCurrentWeather(latitude, longitude)
            val forecast = weatherRepository.getForecast(latitude, longitude)
            if (currentWeather != null || forecast != null) {
                sharedPreferences.edit {
                    remove("last_city")
                    putString("last_coordinates", "$latitude|$longitude")
                }
            }
            updateUiState("($latitude, $longitude)", currentWeather, forecast)
        }
    }

    private fun setLoadingState() {
        _uiState.update {
            HomeUiState(
                loadingState = LoadingState.LOADING,
                location = "",
                currentWeather = null,
                forecast = listOf()
            )
        }
    }

    private fun updateUiState(location: String, currentWeather: Weather?, forecast: List<Weather>?) {
        if (currentWeather == null && forecast == null) {
            _uiState.update {
                HomeUiState(
                    loadingState = LoadingState.ERROR,
                    location = "",
                    currentWeather = null,
                    forecast = listOf()
                )
            }
        } else {
            _uiState.update {
                HomeUiState(
                    loadingState = LoadingState.LOADED,
                    location = location,
                    currentWeather = currentWeather,
                    forecast = forecast ?: listOf()
                )
            }
        }
    }

}