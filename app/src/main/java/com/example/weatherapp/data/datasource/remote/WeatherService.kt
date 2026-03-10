package com.example.weatherapp.data.datasource.remote
import com.example.weatherapp.data.model.dto.ForecastData
import com.example.weatherapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAppService {

    @GET("/data/2.5/forecast")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String= Constants.ApiKey,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): Response<ForecastData>
}