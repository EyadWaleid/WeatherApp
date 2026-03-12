package com.example.weatherapp.data.repo

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.data.datasource.local.FavLocalDataSource
import com.example.weatherapp.data.datasource.local.UserSettings
import com.example.weatherapp.data.datasource.local.WeatherLocalDatasource
import com.example.weatherapp.data.datasource.remote.WeatherDataSource
import com.example.weatherapp.data.model.CountryForecast
import com.example.weatherapp.data.model.FavCity

import com.example.weatherapp.utils.WeatherMapper.mapToDailyWeather
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
class WeatherHomeRepo(
    val weatherDataSource: WeatherDataSource = WeatherDataSource(),
    val context: Application,
    val localDatasource: WeatherLocalDatasource= WeatherLocalDatasource(context.applicationContext),
    val favLocalDataSource: FavLocalDataSource= FavLocalDataSource(context = context),

) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loadCountryWeatherData(
        lon: Double,
        lat: Double,
        units: String = "metric",
        lang: String = "en"
    ): Result<CountryForecast> {
        return try {
            val result = weatherDataSource.getWeatherCountryInfo(lon, lat, units, lang)
            if (result.isSuccess) {
                val forecastData = result.getOrThrow()
                val countryWeather = CountryForecast(
                    city = forecastData.city.name,
                    countryCode = forecastData.city.country,
                    weatherOfDays = mapToDailyWeather(forecastData, lang = lang)
                )
                localDatasource.insertForecast(countryWeather)
                Result.success(countryWeather)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            val cached = localDatasource.getForecast()
            if (cached != null) {
                Result.success(cached)
            } else {
                Result.failure(Exception("No internet and no cached data"))
            }
        }
    }

    fun getAllCountry()=favLocalDataSource.getUpdatedData()
    suspend fun delete(favCity: FavCity){
        favLocalDataSource.deleteData(favCity)
    }
    suspend fun  saveFavcity(favCity: FavCity){
        favLocalDataSource.insertData(favCity)

    }
    suspend fun getAllCountryOnce(): List<FavCity> = favLocalDataSource.getAllCountryOnce()
    suspend fun refreshFavData(units: String, lang: String) {
        val cities = getAllCountryOnce()
        if (cities.isEmpty()) return
        coroutineScope {
            cities.map { city ->
                async {
                    runCatching {
                        Log.d("APIUNIT",units)
                        val response = weatherDataSource.getWeatherCountryInfo(lat = city.lat, lon = city.long, units=units, lang = lang)
                        Log.d("APIUNIT","get the value ${units}")

                        val forecastData = response.getOrThrow()
                        Log.d("APIUNIT","get the value ${response.getOrThrow()}")

                        favLocalDataSource.insertData(

                            city.copy(
                                name = forecastData.city.name,
                                countryCode = forecastData.city.country,
                                temp = forecastData.list.first().main.temp,
                                tempDescription = forecastData.list.first().weather.firstOrNull()?.description ?: ""
                            )
                        )
                    }
                }
            }.awaitAll()
        }
    }



}