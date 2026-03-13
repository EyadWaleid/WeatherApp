package com.example.weatherapp.utils

data class UserSettings(
    val tempUnit: String = "metric",
    val windUnit: String = "m/s",
    val lang: String = "en",
    val locationSource: String = "gps",
    val mapLat: Double = 0.0,
    val mapLon: Double = 0.0
)