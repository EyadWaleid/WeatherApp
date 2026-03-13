package com.example.weatherapp.screens.details.view.views

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.weatherapp.screens.details.view.shimmer.DiscoverLoading
import com.example.weatherapp.screens.details.viewmodel.DetailViewModel
import com.example.weatherapp.screens.home.view_model.WeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetialScreen(modifier: Modifier = Modifier, detailsViewModel: DetailViewModel){

    val weatherState by detailsViewModel.detailData.collectAsState()

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
            is DetailViewModel.WeatherDetailState.IsLoading -> {
                DiscoverLoading()
            }
            is DetailViewModel.WeatherDetailState.OnError ->{
                Log.d("Weather", "Error")
            }
            is DetailViewModel.WeatherDetailState.WeatherData->{
                ShowDetailWeather(detailViewModel = weatherState as DetailViewModel.WeatherDetailState.WeatherData)
            }
            else -> {
                Log.d("Weather", "none of the above ")
            }
        }
    }


}


