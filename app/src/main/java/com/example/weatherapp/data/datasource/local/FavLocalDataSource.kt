package com.example.weatherapp.data.datasource.local

import android.app.Application
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.FavCity
import kotlinx.coroutines.flow.Flow

class FavLocalDataSource(val  context: Application){
  private val favDao= AppDatabase.getInstance(context).favDao()
 suspend fun insertData(favCity: FavCity)= favDao.insertFavCity(favCity)
 fun getUpdatedData(): Flow<List<FavCity>>{
  return  favDao.getAllCities()
 }
 suspend fun getAllCountryOnce(): List<FavCity> = favDao.getAllCitiesOnce()

 suspend fun  deleteData(favCity: FavCity){
  return favDao.deleteCity(favCity)
 }
}
