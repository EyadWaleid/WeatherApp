package com.example.weatherapp.data.repo.homeRepo
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.data.datasource.local.datasource.FavLocalDataSource
import com.example.weatherapp.data.datasource.local.datasource.WeatherLocalDatasource
import com.example.weatherapp.data.datasource.remote.WeatherDataSource
import com.example.weatherapp.data.model.entity.CountryForecast
import com.example.weatherapp.data.model.entity.FavCity
import com.example.weatherapp.utils.AppException
import com.example.weatherapp.utils.NetworkExceptions
import com.example.weatherapp.utils.WeatherMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class WeatherHomeRepo(
    val weatherDataSource: WeatherDataSource = WeatherDataSource(),
    val context: Application,
    val localDatasource: WeatherLocalDatasource = WeatherLocalDatasource(context.applicationContext),
    val favLocalDataSource: FavLocalDataSource = FavLocalDataSource(context = context),

    ) : IWeatherHomeRepo{


    // Download the data to database and then View
    @RequiresApi(Build.VERSION_CODES.O)
  override  suspend fun loadCountryWeatherData(
        lon: Double,
        lat: Double,
        units: String,
        lang: String,
    ): Result<CountryForecast> {

        return try {
            val result = weatherDataSource.getWeatherCountryInfo(lat = lat, lon = lon, units, lang)
                val forecastData = result.getOrThrow()
                val countryWeather = CountryForecast(
                    city = forecastData.city.name,
                    long = forecastData.city.coord.lon,
                    lat = forecastData.city.coord.lat,
                    countryCode = forecastData.city.country,
                    weatherOfDays = WeatherMapper.mapToDailyWeather(forecastData, lang = lang)
                )


                localDatasource.insertForecast(countryWeather)
                Result.success(countryWeather)

        } catch (e: Exception) {
            val isNetworkError = e is java.net.UnknownHostException
                    || e is java.net.SocketTimeoutException
                    || e is java.io.IOException

            if (isNetworkError) {
                val cached = localDatasource.getForecast()
                if (cached != null) {
                    Result.success(cached)
                } else {
                    Result.failure(NetworkExceptions("No internet and no cached data"))
                }
            } else {
                Result.failure(AppException(e.message ?: "Unexpected error"))
            }
        }
    }
   override suspend  fun getSavedWeatherForecast(): Result<CountryForecast>  {
        return  runCatching {
            localDatasource.getForecast()
                ?: throw Exception("No forecast saved in database")
        }
    }

    // View Data from the Api
    @RequiresApi(Build.VERSION_CODES.O)
  override  suspend fun fetchCountryWeatherData(
        lon: Double,
        lat: Double,
        units: String ,
        lang: String
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
            long = forecastData.city.coord.lon,
            lat = forecastData.city.coord.lat,
            weatherOfDays = WeatherMapper.mapToDailyWeather(forecastData, lang = lang)
        )
    }

    // Observe the database of FavCountry table
   override fun getAllCountry() = favLocalDataSource.getUpdatedData()

    // delete the FavCountry from the table
   override suspend fun delete(favCity: FavCity) {
        favLocalDataSource.deleteData(favCity)
    }

    // insert in database
  override  suspend fun saveFavcity(favCity: FavCity) {
        favLocalDataSource.insertData(favCity)
    }

    // get All data from database
  override  suspend fun getAllCountryOnce(): List<FavCity> = favLocalDataSource.getAllCountryOnce()

    // Revalue the data from the Api
   override suspend fun refreshFavData(units: String, lang: String): Boolean {
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