package com.example.weatherapp.data.repo

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.data.datasource.local.FavLocalDataSource
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
    val localDatasource: WeatherLocalDatasource = WeatherLocalDatasource(context.applicationContext),
    val favLocalDataSource: FavLocalDataSource = FavLocalDataSource(context = context),

    ) {


    // Download the data to database and then View
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loadCountryWeatherData(
        lon: Double,
        lat: Double,
        units: String = "metric",
        lang: String = "en"
    ): Result<CountryForecast> {
        return try {
            Log.d("LOC", "I entered the load function")

            val result = weatherDataSource.getWeatherCountryInfo(lat = lat, lon = lon, units, lang)
            if (result.isSuccess) {
                val forecastData = result.getOrThrow()
                Log.d(
                    "LOC",
                    "long=${forecastData.city.coord.lon},lat=${forecastData.city.coord.lat}"
                )
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
            Log.d("LOC", "error${e.message}")

            val cached = localDatasource.getForecast()
            if (cached != null) {
                Result.success(cached)
            } else {
                Result.failure(Exception("No internet and no cached data"))
            }
        }
    }

    // View Data from the Api
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchCountryWeatherData(
        lon: Double,
        lat: Double,
        units: String = "metric",
        lang: String = "en"
    ): Result<CountryForecast> = runCatching {
        val forecastData = weatherDataSource.getWeatherCountryInfo(
            lat = lat,
            lon = lon,
            units = units,
            lang = lang
        ).getOrThrow()

        CountryForecast(
            city = forecastData.city.name,
            countryCode = forecastData.city.country,
            weatherOfDays = mapToDailyWeather(forecastData, lang = lang)
        )
    }

    // Observe the database of FavCountry table
    fun getAllCountry() = favLocalDataSource.getUpdatedData()

    // delete the FavCountry from the table
    suspend fun delete(favCity: FavCity) {
        favLocalDataSource.deleteData(favCity)
    }

    // insert in database
    suspend fun saveFavcity(favCity: FavCity) {
        favLocalDataSource.insertData(favCity)
    }

    // get All data from database
    suspend fun getAllCountryOnce(): List<FavCity> = favLocalDataSource.getAllCountryOnce()

    // Revalue the data from the Api
    suspend fun refreshFavData(units: String, lang: String): Boolean {
        val cities = getAllCountryOnce()
        if (cities.isEmpty()) return false
        coroutineScope {
            cities.map { city ->
                async {
                    runCatching {
                        val response = weatherDataSource.getWeatherCountryInfo(
                            lat = city.lat,
                            lon = city.long,
                            units = units,
                            lang = lang
                        )
                        val forecastData = response.getOrThrow()
                        favLocalDataSource.insertData(
                            city.copy(
                                name = forecastData.city.name,
                                countryCode = forecastData.city.country,
                                temp = forecastData.list.first().main.temp,
                                tempDescription = forecastData.list.first().weather.firstOrNull()?.description
                                    ?: "",
                                createdAt = city.createdAt
                            )
                        )
                    }
                }
            }.awaitAll()


        }
        return true
    }


}