package com.example.weatherapp.data.datasource.remote


import com.example.weatherapp.data.model.dto.ForecastData
import com.example.weatherapp.data.network.AppNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherDataSource: IWeatherRemoteDataSource {
    val weatherService : WeatherAppService= AppNetwork.weatherService
    override suspend fun getWeatherCountryInfo(
        lat: Double,
        lon: Double,
        units: String ,
        lang: String
    ): Result<ForecastData> = runCatching {
        val result = weatherService.getCurrentWeather(lat = lat, lon = lon, units = units, lang = lang)
        result.body() ?: ForecastData.empty()
    }
}