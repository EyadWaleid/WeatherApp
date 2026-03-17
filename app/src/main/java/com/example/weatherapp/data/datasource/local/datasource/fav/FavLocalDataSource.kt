package com.example.weatherapp.data.datasource.local.datasource.fav

import android.app.Application
import android.content.Context
import com.example.weatherapp.data.datasource.local.dao.FavDao
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.entity.FavCity
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Contextual

class FavLocalDataSource(private val favDao: FavDao): IFavLocalDataSource{

 override suspend fun insertData(favCity: FavCity)= favDao.insertFavCity(favCity)
 override fun getUpdatedData(): Flow<List<FavCity>> {
  return  favDao.getAllCities()
 }
 override suspend fun getAllCountryOnce(): List<FavCity> = favDao.getAllCitiesOnce()

 override suspend fun  deleteData(favCity: FavCity){
  return favDao.deleteCity(favCity)
 }
}