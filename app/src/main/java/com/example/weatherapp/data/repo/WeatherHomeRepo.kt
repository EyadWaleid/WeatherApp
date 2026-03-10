package com.example.weatherapp.data.repo

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.data.datasource.local.UserSettings
import com.example.weatherapp.data.datasource.local.WeatherLocalDatasource
import com.example.weatherapp.data.datasource.remote.WeatherDataSource
import com.example.weatherapp.data.model.CountryForecast

import com.example.weatherapp.utils.WeatherMapper.mapToDailyWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherHomeRepo(
    val weatherDataSource: WeatherDataSource = WeatherDataSource(),
    val localDatasource: WeatherLocalDatasource= WeatherLocalDatasource(context.applicationContext),
    val context: Application
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCountryWeatherData(
        lan: Double,
        lat: Double,
        units: String = "metric",
        lang: String = "en"
    ): Flow<Result<CountryForecast>> = flow {
        try {
            weatherDataSource.getWeatherCountryInfo(lan, lat, units, lang).collect { result ->
                result.fold(
                    onSuccess = { forecastData ->
                        val conuntryWeather = CountryForecast(
                            city = forecastData.city.name,
                            countryCode = forecastData.city.country,
                            weatherOfDays = mapToDailyWeather(
                                forecastData,
                                lang = lang
                            )
                        )


                        localDatasource.insertForecast(conuntryWeather)
                        emit(Result.success(conuntryWeather))
                    },
                    onFailure = { emit(Result.failure(it)) }
                )
            }
        } catch (e: Exception) {

            localDatasource.getForecast().collect { cached ->
                if (cached != null) {
                    emit(Result.success(cached))
                } else {
                    emit(Result.failure(Exception("No internet connection and no cached data")))
                }
            }
        }
    }


    suspend fun setLanguage(lang: String) {
        UserSettings.setLanguage(context, lang)
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