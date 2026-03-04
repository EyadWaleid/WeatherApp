package com.example.weatherapp.data.repo

import com.example.weatherapp.data.datasource.remote.WeatherDataSource
import com.example.weatherapp.data.model.ForecastData
import kotlinx.coroutines.flow.Flow

class WeatherHomeRepo(val  weatherDataSource : WeatherDataSource= WeatherDataSource()) {

    fun loadCountryWeatherData(lan: Double,lat: Double): Flow<Result<ForecastData>> {
        return  weatherDataSource.getWeatherCountryInfo(long = lan, lat = lat)
    }

}