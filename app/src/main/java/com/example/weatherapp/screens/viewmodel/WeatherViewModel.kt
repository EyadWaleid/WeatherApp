package com.example.weatherapp.screens.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.model.DailyWeather
import com.example.weatherapp.data.model.ForecastItem
import com.example.weatherapp.data.model.HourlyWeather
import com.example.weatherapp.data.repo.WeatherHomeRepo
import com.example.weatherapp.utils.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel (context: Context) : ViewModel() {
    val repo= WeatherHomeRepo()
    @RequiresApi(Build.VERSION_CODES.O)
    val selectedDate = LocalDate.now()
    val locationProvider= LocationHelper(context)
    private val _locationFlow = MutableStateFlow<WeatherState>(WeatherState.IsLoading)
    val locationFlow: StateFlow<WeatherState> = _locationFlow
    sealed class WeatherState{
        object IsLoading: WeatherState()
        object PermissionDisabled: WeatherState()
        object LocationDisabled: WeatherState()
        class OnError(val errorMessage : String) : WeatherState()
        class  WeatherData (val weather: ForecastItem , val hourlyWeather: List<HourlyWeather>, val dailyWeatherData: List<DailyWeather>,val city: City) : WeatherState()
    }
    init {
        fetchLocation()
    }
      @RequiresApi(Build.VERSION_CODES.O)
      fun fetchLocation() {
          viewModelScope.launch {
              if (!locationProvider.checkPermissions()) {
                  _locationFlow.value = WeatherState.PermissionDisabled

              }
              else if (!locationProvider.isLocationEnabled()) {
                  _locationFlow.value = WeatherState.LocationDisabled

              }
              else{
                  _locationFlow.value = WeatherState.IsLoading
                  try {
                      val location = locationProvider.getUserLocation()

                      if (location != null) {
                          Log.d("Weather","Locaiton is with me  lon=${location.longitude},lat=${location.latitude}")
                          repo.loadCountryWeatherData(location.longitude, location.latitude)
                              .collect { result ->
                                  _locationFlow.value = result.fold(
                                      onSuccess = { response ->
                                          val dailyWeather = response.list
                                              .map { it.dt_txt.substring(0, 10) }
                                              .distinct()
                                              .drop(1)
                                              .map { date ->
                                                  val items = response.list.filter { it.dt_txt.startsWith(date) }
                                                  val maxTemp = items.maxOf { it.main.temp_max }
                                                  val minTemp = items.minOf { it.main.temp_min }
                                                  val representativeItem = items.firstOrNull { it.dt_txt.contains("12:00") }
                                                      ?: items.first()
                                                  val icon = representativeItem.weather.firstOrNull()?.icon.orEmpty()
                                                  val condition = representativeItem.weather.firstOrNull()?.main.orEmpty()
                                                  val dayName = LocalDate.parse(date)
                                                      .dayOfWeek
                                                      .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                                                  DailyWeather(
                                                      date = dayName,
                                                      maxTemp = maxTemp.toInt(),
                                                      minTemp = minTemp.toInt(),
                                                      icon = icon,
                                                      condition = condition
                                                  )
                                              }

                                          val hourlyWeather = response.list
                                              .filter { it.dt_txt.substring(0, 10) == selectedDate.toString() }
                                              .map { item ->
                                                  HourlyWeather(
                                                      time = formatTime(item.dt_txt.substring(11, 16)),
                                                      temp = item.main.temp.toInt(),
                                                      icon = item.weather.firstOrNull()?.icon.orEmpty()
                                                  )
                                              }.toList()

                                          val currentWeather = response.list.firstOrNull() ?: ForecastItem.empty() // 👈 response.list
                                          val city = response.city

                                          WeatherState.WeatherData(
                                              dailyWeatherData = dailyWeather,
                                              hourlyWeather = hourlyWeather,
                                              weather = currentWeather,
                                              city = city
                                          )
                                      },
                                      onFailure = { WeatherState.OnError(it.message ?: "Unknown error") }
                                  )
                              }

                      } else {
                          _locationFlow.value = WeatherState.OnError("Unable to get location")
                      }
                  } catch (e: Exception) {
                      _locationFlow.value = WeatherState.OnError(e.message ?: "Unknown error")
                  }

              }

          }

    }
    private fun formatTime(timeStr: String): String {
        val parts = timeStr.split(":")
        var hours = parts[0].toInt()
        val period = if (hours >= 12) "PM" else "AM"
        if (hours > 12) hours -= 12
        if (hours == 0) hours = 12

        return "$hours $period"
    }
}
@Suppress("UNCHECKED_CAST")
class  WeatherFactory (val context: Application): ViewModelProvider.Factory{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(context) as T
    }
}