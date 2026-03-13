package com.example.weatherapp.screens.details.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.DailyWeather
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.data.model.HourlyWeather
import com.example.weatherapp.data.repo.SettingsRepo
import com.example.weatherapp.data.repo.WeatherHomeRepo
import com.example.weatherapp.utils.Units
import com.example.weatherapp.utils.UserSettings
import com.example.weatherapp.utils.WeatherMapper.getUnits
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DetailViewModel(
    private val context: Application,
    private val lat: Double,
    private val long: Double
) : ViewModel() {
    private val repo = WeatherHomeRepo(context = context)
    private val settings = SettingsRepo(context)
    private val _detailData = MutableStateFlow<WeatherDetailState>(WeatherDetailState.IsLoading)
    val detailData: StateFlow<WeatherDetailState> = _detailData
    private var currentSettings = UserSettings()
    init {
        viewModelScope.launch {
            combine(
                settings.getTempUnit(),
                settings.getLanguage(),
                settings.getWindUnit()
            ) { temp, lang, wind ->
                UserSettings(
                    tempUnit = getUnits(temp),
                    lang = lang,
                    windUnit = wind
                )
            }.collect { settings ->
                currentSettings = settings
                loadWeatherData()
            }

        }
    }
    private fun loadWeatherData() {
        viewModelScope.launch {
            val result = repo.fetchCountryWeatherData(
                lon = long,
                lat = lat,
                units = currentSettings.tempUnit,
                lang = currentSettings.lang
            )
            result.fold(
                { response ->
                    _detailData.value = WeatherDetailState.IsLoading
                    val convertedDays = when {
                        currentSettings.tempUnit == "imperial" && currentSettings.windUnit == Units.METERS_PER_SECOND.displayName ->
                            response.weatherOfDays.map { it.copy(wind = it.wind / 2.237) }

                        currentSettings.tempUnit != "imperial" && currentSettings.windUnit == Units.MILES_PER_HOUR.displayName ->
                            response.weatherOfDays.map { it.copy(wind = it.wind * 2.237) }

                        else -> response.weatherOfDays
                    }
                    _detailData.value = WeatherDetailState.WeatherData(
                        dailyWeatherData = convertedDays.drop(1),
                        hourlyWeather = convertedDays.first().hoursOfDayForecast,
                        weather = convertedDays.first(),
                        city = response.city,
                        country = response.countryCode,
                        windUnit = currentSettings.windUnit,
                        tempUnit = currentSettings.tempUnit
                    )

                },
                {
                    _detailData.value = WeatherDetailState.OnError(it.message.toString())
                }
            )

        }
    }
    sealed class WeatherDetailState {
        object IsLoading : WeatherDetailState()
        class OnError(val errorMessage: String) : WeatherDetailState()
        data class WeatherData(
            val weather: DailyWeather,
            val hourlyWeather: List<HourlyWeather>,
            val dailyWeatherData: List<DailyWeather>,
            val city: String,
            val country: String,
            val tempUnit: String,
            val windUnit: String
        ) : WeatherDetailState()
    }
}

class DetialFactoryViewModel(val context: Application, val lat: Double, val long: Double) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(context = context, lat = lat, long = long) as T
    }
}