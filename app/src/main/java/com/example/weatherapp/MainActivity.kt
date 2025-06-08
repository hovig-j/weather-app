package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.data.Weather
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.openweather.OpenWeatherDataSource
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                val homeViewModel = viewModel {
                    val application = checkNotNull(get(AndroidViewModelFactory.APPLICATION_KEY))
                    val weatherRepository = WeatherRepository(OpenWeatherDataSource())
                    HomeViewModel(application, weatherRepository)
                }
                var showCityDialog by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(getString(R.string.app_name)) },
                            actions = {
                                IconButton(onClick = { showCityDialog = true }) {
                                    Icon(Icons.Default.Search, contentDescription = "Search a City")
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        LocationFAB()
                    }
                ) { innerPadding ->
                    HomeScreen(innerPadding)

                    if (showCityDialog) {
                        var cityName by remember { mutableStateOf(TextFieldValue()) }

                        Dialog(onDismissRequest = { showCityDialog = false }) {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                tonalElevation = 8.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(24.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text("Enter a city name")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        modifier = Modifier.semantics { contentType = ContentType.AddressLocality },
                                        value = cityName,
                                        onValueChange = { cityName = it },
                                        label = { Text("City") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                        keyboardActions = KeyboardActions(
                                            onSearch = {
                                                homeViewModel.setCity(cityName.text)
                                                showCityDialog = false
                                            }
                                        ),
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        modifier = Modifier.align(Alignment.End),
                                        onClick = {
                                            homeViewModel.setCity(cityName.text)
                                            showCityDialog = false
                                        }) {
                                        Text("OK")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationFAB(
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val hasFinePermission = checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    val hasCoarsePermission = checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val receivedFinePermission = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
            val receivedCoarsePermission = permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            if (receivedFinePermission || receivedCoarsePermission) {
                readLocation(context, homeViewModel)
            } else {
                Toast.makeText(context, "Permission is denied.\nPlease grant it from the app settings.", Toast.LENGTH_LONG).show()
            }
        }
    )

    FloatingActionButton(
        onClick = {
            if (hasFinePermission || hasCoarsePermission) {
                readLocation(context, homeViewModel)
            } else {
                val locationPermissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                locationPermissionLauncher.launch(locationPermissions)
            }
        }
    ) {
        Icon(Icons.Default.Place, contentDescription = "Use my current location")
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun readLocation(context: Context, homeViewModel: HomeViewModel) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!gpsEnabled && !networkEnabled) {
        Toast.makeText(context, "Location is not enabled", Toast.LENGTH_SHORT).show()
    }

    val locationClient = LocationServices.getFusedLocationProviderClient(context)
    locationClient
        .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location: Location? ->
            location?.let {
                homeViewModel.setCoordinates(it.latitude, it.longitude)
            }
        }
}

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        when (uiState.loadingState) {
            LoadingState.INITIAL -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Search a city or use your current location.")
                }
            }

            LoadingState.LOADING -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            LoadingState.LOADED -> {
                Text(text = "Location: ${uiState.location}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                CurrentWeather(uiState.currentWeather)
                Spacer(modifier = Modifier.height(24.dp))
                Forecast(uiState.forecast)
            }

            LoadingState.ERROR -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "An error occurred while getting the weather.\nPlease try again later.")
                }
            }
        }
    }
}

@Composable
fun CurrentWeather(weather: Weather?) {
    if (weather == null) return

    val dateTime = remember(weather.dt) {
        DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault())
            .format(Instant.ofEpochSecond(weather.dt))
    }
    val currentTemperature = remember(weather.temperatures.temperature) {
        "${weather.temperatures.temperature.toInt()}°"
    }
    val description = remember(weather.summary.first()) {
        "${weather.summary.first().main} (${weather.summary.first().description})"
    }
    val temperatures = remember(weather.temperatures) {
        "${weather.temperatures.minTemperature.toInt()}°~${weather.temperatures.maxTemperature.toInt()}° | Feels like: ${weather.temperatures.feelsLike.toInt()}°"
    }
    val wind = remember(weather.wind) {
        "Wind: ${weather.wind.speed}m/s"
    }
    val cloud = remember(weather.clouds) {
        "Cloud: ${weather.clouds.cloudiness}%"
    }
    val rain = remember(weather.rain) {
        "Rain: ${weather.rain?.volumePerHour ?: 0.0}mm"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = currentTemperature,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = description, style = MaterialTheme.typography.titleMedium)
                Text(text = temperatures, style = MaterialTheme.typography.titleSmall)
                Text(text = "$wind | $cloud | $rain", style = MaterialTheme.typography.bodyMedium)
                Text(text = dateTime, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun Forecast(forecast: List<Weather>) {
    if (forecast.isNotEmpty()) {
        Text(text = "Forecast", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
    }

    LazyColumn {
        items(forecast) { weather ->
            val dateTime = remember(weather.dt) {
                DateTimeFormatter
                    .ofPattern("EEEE HH:mm")
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.ofEpochSecond(weather.dt))
            }
            val description = remember(weather.summary.first()) {
                "${weather.summary.first().main} (${weather.summary.first().description})"
            }
            val temperature = remember(weather.temperatures.temperature) {
                "${weather.temperatures.temperature.toInt()}°"
            }
            val wind = remember(weather.wind) {
                "Wind: ${weather.wind.speed}m/s"
            }
            val cloud = remember(weather.clouds) {
                "Cloud: ${weather.clouds.cloudiness}%"
            }
            val rain = remember(weather.rain) {
                "Rain: ${weather.rain?.volumePer3 ?: 0.0}mm (${weather.precipitation}%)"
            }

            Card(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = dateTime, style = MaterialTheme.typography.titleLarge)
                    Text(text = description, style = MaterialTheme.typography.titleMedium)
                    Text(text = temperature, style = MaterialTheme.typography.titleSmall)
                    Text(text = "$wind | $cloud | $rain", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
