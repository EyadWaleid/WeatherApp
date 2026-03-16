package com.example.weatherapp.data.repo.homeRepo

import com.example.weatherapp.data.model.entity.CountryForecast
import com.example.weatherapp.data.model.entity.FavCity
import kotlinx.coroutines.flow.Flow

interface IWeatherHomeRepo {
    suspend fun loadCountryWeatherData(
        lon: Double,
        lat: Double,
        units: String = "metric",
        lang: String = "en"
    ): Result<CountryForecast>

    suspend fun getSavedWeatherForecast(): Result<CountryForecast>

    suspend fun fetchCountryWeatherData(
        lon: Double,
        lat: Double,
        units: String = "metric",
        lang: String = "en"
    ): Result<CountryForecast>

    fun getAllCountry(): Flow<List<FavCity>>

    suspend fun delete(favCity: FavCity)

    suspend fun saveFavcity(favCity: FavCity)

    suspend fun getAllCountryOnce(): List<FavCity>

    suspend fun refreshFavData(units: String, lang: String): Boolean
}