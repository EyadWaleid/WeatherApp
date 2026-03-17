package com.example.weatherapp.data.datasource.local.datasource.weather

import android.content.Context
import com.example.weatherapp.data.datasource.local.dao.ForecastDao
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.entity.CountryForecast

class WeatherLocalDatasource(private  val forecastDao: ForecastDao): IWeatherLocalDatasource {

    override suspend fun insertForecast(forecast: CountryForecast) =
        forecastDao.insertForecast(forecast)
    override suspend fun getForecast(): CountryForecast? =
        forecastDao.getForecast()
}