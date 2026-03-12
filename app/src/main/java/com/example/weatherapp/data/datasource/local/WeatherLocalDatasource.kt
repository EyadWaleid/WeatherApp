package com.example.weatherapp.data.datasource.local

import android.content.Context
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.CountryForecast
import kotlinx.coroutines.flow.Flow


class WeatherLocalDatasource(context: Context) {
    private val forecastDao = AppDatabase.getInstance(context).forecastDao()

    suspend fun insertForecast(forecast: CountryForecast) =
        forecastDao.insertForecast(forecast)
    suspend fun getForecast():CountryForecast? =
        forecastDao.getForecast()
}