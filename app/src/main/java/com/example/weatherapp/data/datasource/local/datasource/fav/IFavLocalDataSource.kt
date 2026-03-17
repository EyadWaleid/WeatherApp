package com.example.weatherapp.data.datasource.local.datasource.fav

import com.example.weatherapp.data.model.entity.FavCity
import kotlinx.coroutines.flow.Flow

interface IFavLocalDataSource {
        suspend fun insertData(favCity: FavCity)
        fun getUpdatedData(): Flow<List<FavCity>>
        suspend fun getAllCountryOnce(): List<FavCity>
        suspend fun deleteData(favCity: FavCity)

}