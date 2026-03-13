package com.example.weatherapp.data.model.dto

data class Main(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
) {
    companion object {
        fun empty() = Main(
            temp = 0.0,
            temp_min = 0.0,
            temp_max = 0.0,
            pressure = 0,
            humidity = 0
        )
    }
}