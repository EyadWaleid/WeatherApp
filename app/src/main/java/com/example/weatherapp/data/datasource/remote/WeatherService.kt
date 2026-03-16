package com.example.weatherapp.data.datasource.remote
import com.example.weatherapp.data.model.dto.ForecastData
import com.example.weatherapp.utils.constants.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAppService {

    @GET("/data/2.5/forecast")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String= "a71745f051e01ebdb134fb48009e6027",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): Response<ForecastData>


}