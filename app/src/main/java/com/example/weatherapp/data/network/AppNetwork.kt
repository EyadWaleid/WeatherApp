package com.example.weatherapp.data.network

import com.example.weatherapp.data.datasource.remote.WeatherAppService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.getValue
import kotlin.jvm.java

object AppNetwork {
    private val baseUrl="https://api.openweathermap.org/"
    private val gson= GsonBuilder()
        .serializeNulls()
        .create()
    private  val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(5, TimeUnit.SECONDS)
    .readTimeout(5, TimeUnit.SECONDS)
    .writeTimeout(5, TimeUnit.SECONDS)
    .build()
     private val instance: Retrofit by lazy {
         Retrofit.Builder().baseUrl(baseUrl)
             .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient)
             .build()
     }
    val weatherService: WeatherAppService by lazy {
        instance.create(WeatherAppService::class.java)
    }
}