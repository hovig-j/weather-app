package com.example.weatherapp

import android.app.Application
import android.content.SharedPreferences
import com.example.weatherapp.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val scope = TestScope()
    private val mockWeatherRepository = mock<WeatherRepository>()
    private val mockSharedPreferencesEditor = mock<SharedPreferences.Editor> {
        on { remove(any()) } doReturn this.mock
    }
    private val mockSharedPreferences = mock<SharedPreferences> {
        on { getString(any(), any()) } doReturn null
        on { edit() } doReturn mockSharedPreferencesEditor
    }
    private val mockApplication = mock<Application> {
        on { getSharedPreferences(any(), any()) } doReturn mockSharedPreferences
    }
    private val homeViewModel = HomeViewModel(mockApplication, mockWeatherRepository)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(scope.testScheduler))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun homeViewModel_whenCityIsNotSet_UiStateIsInitial() = runTest {
        val uiState = homeViewModel.uiState.value
        assertEquals(LoadingState.INITIAL, uiState.loadingState)
        assertTrue(uiState.location.isEmpty())
        assertNull(uiState.currentWeather)
        assertTrue(uiState.forecast.isEmpty())
    }

    @Test
    fun homeViewModel_whenCityIsSet_UiStateIsLoading() = runTest {
        homeViewModel.setCity("London")

        val uiState = homeViewModel.uiState.value
        assertEquals(LoadingState.LOADING, uiState.loadingState)
        assertTrue(uiState.location.isEmpty())
        assertNull(uiState.currentWeather)
        assertTrue(uiState.forecast.isEmpty())
    }

    @Test
    fun homeViewModel_whenCurrentWeatherIsNotNull_ForecastIsNotNull_UiStateIsLoaded() = runTest {
        mockWeatherRepository.stub {
            onBlocking { getCurrentWeather(any()) } doReturn Stubs.weatherStub
            onBlocking { getForecast(any()) } doReturn Stubs.forecastStub
        }

        homeViewModel.setCity("London")
        advanceUntilIdle()

        val uiState = homeViewModel.uiState.value
        assertEquals(LoadingState.LOADED, uiState.loadingState)
        assertFalse(uiState.location.isEmpty())
        assertNotNull(uiState.currentWeather)
        assertFalse(uiState.forecast.isEmpty())
    }

    @Test
    fun homeViewModel_whenCurrentWeatherIsNotNull_ForecastIsNull_UiStateIsLoaded() = runTest {
        mockWeatherRepository.stub {
            onBlocking { getCurrentWeather(any()) } doReturn Stubs.weatherStub
            onBlocking { getForecast(any()) } doReturn null
        }

        homeViewModel.setCity("London")
        advanceUntilIdle()

        val uiState = homeViewModel.uiState.value
        assertEquals(LoadingState.LOADED, uiState.loadingState)
        assertFalse(uiState.location.isEmpty())
        assertNotNull(uiState.currentWeather)
        assertTrue(uiState.forecast.isEmpty())
    }

    @Test
    fun homeViewModel_whenCurrentWeatherIsNull_ForecastIsNotNull_UiStateIsLoaded() = runTest {
        mockWeatherRepository.stub {
            onBlocking { getCurrentWeather(any()) } doReturn null
            onBlocking { getForecast(any()) } doReturn Stubs.forecastStub
        }

        homeViewModel.setCity("London")
        advanceUntilIdle()

        val uiState = homeViewModel.uiState.value
        assertEquals(LoadingState.LOADED, uiState.loadingState)
        assertFalse(uiState.location.isEmpty())
        assertNull(uiState.currentWeather)
        assertFalse(uiState.forecast.isEmpty())
    }

    @Test
    fun homeViewModel_whenCurrentWeatherIsNull_ForecastIsNull_UiStateIsError() = runTest {
        mockWeatherRepository.stub {
            onBlocking { getCurrentWeather(any()) } doReturn null
            onBlocking { getForecast(any()) } doReturn null
        }

        homeViewModel.setCity("London")
        advanceUntilIdle()

        val uiState = homeViewModel.uiState.value
        assertEquals(LoadingState.ERROR, uiState.loadingState)
        assertTrue(uiState.location.isEmpty())
        assertNull(uiState.currentWeather)
        assertTrue(uiState.forecast.isEmpty())
    }


}