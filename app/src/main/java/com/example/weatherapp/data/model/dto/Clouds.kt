package com.example.weatherapp.data.model.dto

data class Clouds(
    val all: Int
) {
    companion object {
        fun empty() = Clouds(
            all = 0
        )
    }
}