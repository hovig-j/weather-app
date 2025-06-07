package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class HomeUiState(
    val city: String = "",
)

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("weather_app_shared_pref", Context.MODE_PRIVATE)
    private var lastSavedCity: String = sharedPreferences.getString("last_city", "")!!

    private val _uiState = MutableStateFlow(HomeUiState(lastSavedCity))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun setCity(city: String) {
        _uiState.update { currentState ->
            currentState.copy(
                city = city
            )
        }
        sharedPreferences.edit {
            putString("last_city", city)
        }
    }
}