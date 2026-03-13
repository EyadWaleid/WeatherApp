package com.example.weatherapp.data.model.dto

data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coord:Coord,
) {
    companion object {
        fun empty() = City(
            id = 0,
            name = "",
            country = "",
            coord=Coord.empty()
        )
    }
}