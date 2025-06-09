package com.example.weatherapp.data

import com.example.weatherapp.Stubs
import com.example.weatherapp.openweather.OpenWeatherDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryTest {

    private val scope = TestScope()
    private val mockOpenWeatherDataSource = mock<OpenWeatherDataSource>()
    private val weatherRepository = WeatherRepository(mockOpenWeatherDataSource)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(scope.testScheduler))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun weatherRepository_whenCurrentWeatherJsonIsNotNull_CorrectWeatherIsReturned() = runTest {
        mockOpenWeatherDataSource.stub {
            onBlocking { getCurrentWeather(any()) } doReturn Stubs.weatherJsonStub
        }

        val currentWeather = weatherRepository.getCurrentWeather("London")

        assertNotNull(currentWeather)
        assertEquals(Stubs.weatherStub, currentWeather)
    }

    @Test
    fun weatherRepository_whenCurrentWeatherJsonIsNull_NullIsReturned() = runTest {
        mockOpenWeatherDataSource.stub {
            onBlocking { getCurrentWeather(any()) } doReturn null
        }

        val currentWeather = weatherRepository.getCurrentWeather("London")

        assertNull(currentWeather)
    }

    @Test
    fun weatherRepository_whenForecastJsonIsNotNull_CorrectForecastIsReturned() = runTest {
        mockOpenWeatherDataSource.stub {
            onBlocking { getForecast(any()) } doReturn Stubs.forecastJsonStub
        }

        val forecast = weatherRepository.getForecast("London")

        assertNotNull(forecast)
        assertFalse(forecast!!.isEmpty())
        assertEquals(Stubs.forecastStub, forecast)
    }

    @Test
    fun weatherRepository_whenForecastJsonIsNull_NullIsReturned() = runTest {
        mockOpenWeatherDataSource.stub {
            onBlocking { getForecast(any()) } doReturn null
        }

        val forecast = weatherRepository.getForecast("London")

        assertNull(forecast)
    }

}