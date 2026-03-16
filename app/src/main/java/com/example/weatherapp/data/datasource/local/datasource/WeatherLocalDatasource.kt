package com.example.weatherapp.data.datasource.local.datasource

import android.content.Context
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.entity.CountryForecast


class WeatherLocalDatasource(context: Context) {
    private val forecastDao = AppDatabase.getInstance(context).forecastDao()

    suspend fun insertForecast(forecast: CountryForecast) =
        forecastDao.insertForecast(forecast)
    suspend fun getForecast():CountryForecast? =
        forecastDao.getForecast()
}