package com.example.weatherapp.data.datasource.remote

import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.network.AppNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherDataSource {
    val weatherService : WeatherService= AppNetwork().getCountryTemp()
    fun getWeatherCountryInfo(lat: Double, long: Double , units:String="metric") : Flow<Result<ForecastData>>{
       return flow {
           try{
               val result =weatherService.getCurrentWeather(lat =lat, lon = long, units = units )
               if (result.isSuccessful){
                   emit(Result.success(result.body()?: ForecastData.empty() ))
               }
               else{
                   emit(Result.failure(Exception(result.message())))
               }

           }catch (e: Exception) {
                  emit(Result.failure(e))
           }

       }

    }

}