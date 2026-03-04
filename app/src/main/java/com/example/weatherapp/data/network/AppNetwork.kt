package com.example.weatherapp.data.network

import com.example.weatherapp.data.datasource.remote.WeatherService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppNetwork {
    val gson = GsonBuilder()
        .serializeNulls()
        .create()
    private var weatherService: WeatherService? = null
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    fun getCountryTemp(): WeatherService {
        if (weatherService == null) {
            weatherService = retrofit.create(WeatherService::class.java)
        }
        return weatherService!!
    }
}