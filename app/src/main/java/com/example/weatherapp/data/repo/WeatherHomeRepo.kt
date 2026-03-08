package com.example.weatherapp.data.repo

import android.app.Application
import android.content.Context
import com.example.weatherapp.data.datasource.local.UserSettings
import com.example.weatherapp.data.datasource.remote.WeatherDataSource
import com.example.weatherapp.data.model.ForecastData
import kotlinx.coroutines.flow.Flow

class WeatherHomeRepo(val  weatherDataSource : WeatherDataSource= WeatherDataSource(),val  context: Application) {

    fun loadCountryWeatherData(lan: Double,lat: Double, units :String ="metric"): Flow<Result<ForecastData>> {
        return  weatherDataSource.getWeatherCountryInfo(long = lan, lat = lat, units = units)
    }

        suspend fun setLanguage(lang: String) {
            UserSettings.setLanguage(  context, lang)
        }

        fun getLanguage(): Flow<String> {
            return UserSettings.getLanguage(context)
        }

        suspend fun setTempUnit(unit: String) {
            UserSettings.setTempUnit(context, unit)
        }

        fun getTempUnit(): Flow<String> {
            return UserSettings.getTempUnit(context)
        }

        suspend fun setWindUnit(unit: String) {
            UserSettings.setWindUnit(context, unit)
        }

        fun getWindUnit(): Flow<String> {
            return UserSettings.getWindUnit(context)
    }


}