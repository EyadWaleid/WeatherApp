package com.example.weatherapp.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.data.model.DailyWeather
import com.example.weatherapp.data.model.HourlyWeather
import com.example.weatherapp.data.model.dto.ForecastData
import java.time.LocalDate.*
import java.time.format.TextStyle
import java.util.Locale


object WeatherMapper {


    @RequiresApi(Build.VERSION_CODES.O)
    fun mapToDailyWeather(forecastData: ForecastData, lang: String): List<DailyWeather> {
        return forecastData.list
            .map { it.dt_txt.substring(0, 10) }
            .distinct()
            .map { date ->
                val items = forecastData.list.filter { it.dt_txt.startsWith(date) }
                val representativeItem = items.first()
                val maxTemp = representativeItem.main.temp_max
                val temp=representativeItem.main.temp
                val tempDescription=representativeItem.weather.get(0).description
                val minTemp = representativeItem.main.temp_min
                val icon = representativeItem.weather.firstOrNull()?.icon.orEmpty()
                val humidity = representativeItem.main.humidity
                val humidityCondition=checkHumiditySafety(humidity = humidity.toDouble(),lang)
                val pressure=representativeItem.main.pressure
                val pressureCondition=getPressureCondition(pressure,lang)
                val wind=representativeItem.wind.speed
                val  windDirection=getDirectionFromDegrees(representativeItem.wind.deg,lang)
                val cloudCover=representativeItem.clouds.all
                val hourlyList = items.map { item ->
                    HourlyWeather(
                        time = formatTime(item.dt_txt.substring(11, 16), lang),
                        temp = item.main.temp.toInt(),
                        icon = item.weather.firstOrNull()?.icon.orEmpty()
                    )
                }

                val locale = if (lang == "en") Locale.ENGLISH else Locale("ar")
                val dayName = parse(date)
                    .dayOfWeek
                    .getDisplayName(TextStyle.FULL, locale)
                DailyWeather(
                    dayName=dayName,
                    maxTemp = maxTemp,
                    minTemp = minTemp,
                    humidity = humidity,
                    humidityCondition = humidityCondition,
                    pressure = pressure.toDouble(),
                    pressureCondition = pressureCondition,
                    wind = wind,
                    windDirction = windDirection,
                    cloudCover = cloudCover,
                    icon = icon,
                    hoursOfDayForecast =hourlyList,
                    temp =temp.toInt() ,
                    tempDescription =tempDescription
                )

            }
    }
    fun getPressureCondition(pressure: Int, lang: String): String {
        return if (lang == "ar") {
            when {
                pressure < 1009 -> "منخفض"
                pressure > 1022 -> "مرتفع"
                else            -> "مستقر"
            }
        } else {
            when {
                pressure < 1009 -> "Low"
                pressure > 1022 -> "High"
                else            -> "Stable"
            }
        }
    }
    fun getDirectionFromDegrees(degrees: Double, lang: String): String {
        val directions = if (lang == "ar") {
            arrayOf("شمال", "شمال شرق", "شرق", "جنوب شرق", "جنوب", "جنوب غرب", "غرب", "شمال غرب")
        } else {
            arrayOf("North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest")
        }
        val normalizedDegrees = (degrees + 360) % 360
        val index = ((normalizedDegrees % 360) / 45).toInt()
        return directions[index]
    }    fun checkHumiditySafety(humidity: Double, lang: String): String {
        return if (lang == "ar") {
            when {
                humidity < 30.0 -> "جاف"
                humidity < 60.0 -> "مريح"
                humidity < 80.0 -> "رطب"
                else            -> "رطب جداً"
            }
        } else {
            when {
                humidity < 30.0 -> "Dry"
                humidity < 60.0 -> "Comfortable"
                humidity < 80.0 -> "Humid"
                else            -> "Very Humid"
            }
        }
    }
    private fun formatTime(timeStr: String, lang: String): String {
        val parts = timeStr.split(":")
        var hours = parts[0].toInt()
        val period = if (hours >= 12) {
            Log.d("LAN",lang+" inside the mapper ")

            if (lang == "en") "pm" else "م"
        } else {
            if (lang == "en") "am" else "ص"
        }
        if (hours > 12) hours -= 12
        if (hours == 0) hours = 12
        return "$hours $period"
    }
    fun convertWind(wind: Double, windUnit: String,lang: String): Double {
        return when (windUnit) {
            "mph" -> wind * 2.237
            "m/s" -> wind / 2.237
            else -> wind
        }
    }
      fun getUnits(unit: String):String{
        Log.d("Units","My unit now is : $unit")
        if(unit == TempUnits.FAHRENHEIT.displayName){
            return  "imperial"
        }
        if(unit== TempUnits.KELVIN.displayName){
            return  "standard"
        }
        return "metric"
    }

}