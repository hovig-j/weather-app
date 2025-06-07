package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                val homeViewModel = viewModel {
                    val application = checkNotNull(get(AndroidViewModelFactory.APPLICATION_KEY))
                    HomeViewModel(application)
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
fun HomeScreen(
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        val uiState by homeViewModel.uiState.collectAsState()
        Text(uiState.city)
    }
}