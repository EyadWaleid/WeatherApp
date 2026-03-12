package com.example.weatherapp.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.model.CountryForecast
import com.example.weatherapp.data.model.FavCity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCity(favCity: FavCity)
    @Query("SELECT * FROM fav_country")
    suspend fun getAllCitiesOnce(): List<FavCity>
    @Query("SELECT * FROM fav_country")
    fun getAllCities(): Flow<List<FavCity>>
    @Delete
    suspend fun deleteCity(city: FavCity)
}