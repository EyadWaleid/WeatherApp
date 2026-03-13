package com.example.weatherapp.data.model.dto

data class Wind(
    val speed: Double,
    val deg: Double
) {
    companion object {
        fun empty() = Wind(
            speed = 0.0,
            deg=0.0
        )
    }
}