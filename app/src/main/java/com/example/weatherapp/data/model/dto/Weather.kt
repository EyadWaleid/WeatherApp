package com.example.weatherapp.data.model.dto

data class Weather(
    val main: String,
    val description: String,
    val icon: String
) {
    companion object {
        fun empty() = Weather(
            main = "",
            description = "",
            icon = ""
        )
    }
}