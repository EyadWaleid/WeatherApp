package com.example.weatherapp.screens.home.view

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.weatherapp.R
import com.example.weatherapp.data.model.DailyWeather
import com.example.weatherapp.data.model.HourlyWeather
import com.example.weatherapp.screens.home.view.components.shimmer.ShowLoading
import com.example.weatherapp.screens.home.view.components.views.ShowWeather
import com.example.weatherapp.screens.home.view_model.WeatherViewModel
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, weatherViewModel: WeatherViewModel){
    val  context=LocalContext.current
    val weatherState by weatherViewModel.locationFlow.collectAsState()
    var isRefresh by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        if (granted){
            weatherViewModel.refreshLocation()
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1C2F4E),
                            Color(0xFF112040),
                            Color(0xFF0A1628)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),

                        )
                )
        )
        when(weatherState){
            is WeatherViewModel.WeatherState.PermissionDisabled-> {
                Log.d("Weather", "permission disabled")
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
            is WeatherViewModel.WeatherState.LocationDisabled ->{
                Log.d("Weather", "Location disabled")

                val  intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
            is WeatherViewModel.WeatherState.WeatherData ->{
                PullToRefreshBox(
                    isRefreshing =isRefresh,
                    onRefresh = {
                        isRefresh=true
                        weatherViewModel.refreshLocation()
                        isRefresh=false

                    },
                ) {
                    ShowWeather(modifier, weatherState as WeatherViewModel.WeatherState.WeatherData)
                }
            }

            WeatherViewModel.WeatherState.IsLoading -> {
                Log.d("Weather",  "Loading")
                    ShowLoading()
            }
            is WeatherViewModel.WeatherState.OnError -> {
                val errorState = weatherState as WeatherViewModel.WeatherState.OnError
                Log.d("Weather", errorState.errorMessage)
            }

        }
    }


}


