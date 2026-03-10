package com.example.weatherapp.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_forecast")
data class CountryForecast(
    @PrimaryKey
    val id: Long = 0,
    val city: String,
    val countryCode: String,
    val weatherOfDays: List<DailyWeather>
)

data class DailyWeather(
    val  dayName:String,
    val temp:Int,
    val tempDescription:String,
    val maxTemp: Double,
    val minTemp: Double,
    val humidity: Int,
    val humidityCondition: String,
    val pressure: Double,
    val pressureCondition: String,
    val wind: Double,
    val windDirction: String,
    val cloudCover: Int,
    val icon: String,
    val hoursOfDayForecast: List<HourlyWeather>
)
data class HourlyWeather(val time: String, val temp: Int, val icon: String)
