package com.example.weatherapp.data.model.dto

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val clouds: Clouds,
    val dt_txt: String
) {
    companion object {
        fun empty() = ForecastItem(
            dt = 0L,
            main = Main.empty(),
            weather = emptyList(),
            wind = Wind.empty(),
            clouds = Clouds.empty(),
            dt_txt = ""
        )
    }
}