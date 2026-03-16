package com.example.weatherapp.screens.home.view

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R

import com.example.weatherapp.screens.home.view.components.shimmer.ShowLoading
import com.example.weatherapp.screens.home.view.components.views.ShowWeather
import com.example.weatherapp.screens.home.view_model.HomeViewModel
import com.example.weatherapp.utils.constants.Offline
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel,snackbarHostState:SnackbarHostState){
    val  context=LocalContext.current
    val weatherState by homeViewModel.weatherState.collectAsState()
    var isRefresh by remember { mutableStateOf(false) }
    val snackbarEvent by homeViewModel.snackbarEvent.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(snackbarEvent) {
        snackbarEvent?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short,
                )
                homeViewModel.clearEvent()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            homeViewModel.clearEvent()
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        if (granted){
            homeViewModel.refreshLocation()
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
            is HomeViewModel.WeatherState.PermissionDisabled-> {
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
            is HomeViewModel.WeatherState.LocationDisabled ->{

                val  intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
            is HomeViewModel.WeatherState.WeatherData ->{
                PullToRefreshBox(
                    isRefreshing =isRefresh,
                    onRefresh = {
                        isRefresh=true
                        homeViewModel.refreshLocation()
                        isRefresh=false

                    },
                ) {
                    ShowWeather(modifier, weatherState as HomeViewModel.WeatherState.WeatherData)
                }
            }

            HomeViewModel.WeatherState.IsLoading -> {
                    ShowLoading()
            }
            is HomeViewModel.WeatherState.OfflineError -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                ){
                    Offline()
                    Spacer(Modifier.height(10.dp))
                    Text(text = stringResource(R.string.checkConectivity), textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        color = colorResource(R.color.greyBlue))
                }
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ){
                    Error()
                    Spacer(Modifier.height(10.dp))
                    Text(text ="Something went wrong" , textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        color = colorResource(R.color.greyBlue))
                }
            }
        }
    }


}


