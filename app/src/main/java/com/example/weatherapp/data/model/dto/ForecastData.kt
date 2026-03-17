package com.example.weatherapp.data.model.dto
data class ForecastData(
    val list: List<ForecastItem>,
    val city: City,

) {
    companion object {
        fun empty() = ForecastData(
            list = emptyList(),
            city = City.empty(),
        ) }
}







