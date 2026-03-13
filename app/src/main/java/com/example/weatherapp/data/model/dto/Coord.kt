package com.example.weatherapp.data.model.dto

data class Coord(val lat: Double,val lon: Double){
    companion object {
        fun empty() = Coord(
            lon = 0.0,
            lat=0.0
        )
    }
}