package com.example.weatherapp.data.datasource.remote

import com.example.weatherapp.data.model.dto.ForecastData


interface IWeatherRemoteDataSource {
        suspend fun getWeatherCountryInfo(
            lat: Double,
            lon: Double,
            units: String,
            lang: String
        ): Result<ForecastData>
    }
