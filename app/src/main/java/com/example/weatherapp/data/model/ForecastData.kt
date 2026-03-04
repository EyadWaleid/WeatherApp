package com.example.weatherapp.data.model
data class ForecastData(
    val list: List<ForecastItem>,
    val city: City,

) {
    companion object {
        fun empty() = ForecastData(
            list = emptyList(),
            city = City.empty(),

        )
    }
}
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
data class City(
    val id: Int,
    val name: String,
    val country: String
) {
    companion object {
        fun empty() = City(
            id = 0,
            name = "",
            country = ""
        )
    }
}
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
data class Wind(
    val speed: Double
) {
    companion object {
        fun empty() = Wind(
            speed = 0.0
        )
    }
}
data class Clouds(
    val all: Int
) {
    companion object {
        fun empty() = Clouds(
            all = 0
        )
    }
}