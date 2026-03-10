package com.example.weatherapp.utils
import com.example.weatherapp.R


enum class Units(val displayName: String) {
    METERS_PER_SECOND("m/s"),
    MILES_PER_HOUR("mph")
}
enum class TempUnits(val displayName: String) {
    CELSIUS("Celsius (°C)"),
    FAHRENHEIT("Fahrenheit (°F)"),
    KELVIN("Kelvin (K)")
}
enum class Language(val resId: Int, val code: String) {
    ENGLISH(R.string.englishLanguageChoose, "en"),
    ARABIC(R.string.arabicLanguageChoose, "ar")
}