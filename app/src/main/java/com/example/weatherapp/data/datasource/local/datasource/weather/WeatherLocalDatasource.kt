package com.example.weatherapp.data.datasource.local.datasource.weather

import android.content.Context
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.entity.CountryForecast

class WeatherLocalDatasource(context: Context): IWeatherLocalDatasource {
    private val forecastDao = AppDatabase.Companion.getInstance(context).forecastDao()

    override suspend fun insertForecast(forecast: CountryForecast) =
        forecastDao.insertForecast(forecast)
    override suspend fun getForecast(): CountryForecast? =
        forecastDao.getForecast()
}