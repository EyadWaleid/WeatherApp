package com.example.weatherapp.data.datasource.local.datasource.weather

import com.example.weatherapp.data.model.entity.CountryForecast

interface IWeatherLocalDatasource {
    suspend fun insertForecast(forecast: CountryForecast)
    suspend fun getForecast(): CountryForecast?
}