package com.example.weatherapp.data.datasource.remote


import com.example.weatherapp.data.model.dto.ForecastData
import com.example.weatherapp.data.network.AppNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherDataSource {
    val weatherService : WeatherAppService= AppNetwork.weatherService
    fun getWeatherCountryInfo(lat: Double, long: Double , units:String="metric",lang:String="en") : Flow<Result<ForecastData>>{
       return flow {
           try{
               val result =weatherService.getCurrentWeather(lat =lat, lon = long, units = units, lang = lang)
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