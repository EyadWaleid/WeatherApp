package com.example.weatherapp.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConuntryWeatherConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromDailyWeatherList(days: List<DailyWeather>): String {
        return gson.toJson(days)
    }
    @TypeConverter
    fun toDailyWeatherList(json: String): List<DailyWeather> {
        val type = object : TypeToken<List<DailyWeather>>() {}.type
        return gson.fromJson(json, type)
    }
}