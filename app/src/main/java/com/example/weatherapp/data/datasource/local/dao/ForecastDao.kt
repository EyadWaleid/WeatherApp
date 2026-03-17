package com.example.weatherapp.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.model.entity.CountryForecast

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: CountryForecast)

    @Query("SELECT * FROM country_forecast LIMIT 1")
    suspend fun getForecast(): CountryForecast?

}