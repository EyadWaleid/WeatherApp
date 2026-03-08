package com.example.weatherapp.screens.viewmodel
import android.app.Application
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.model.DailyWeather
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.ForecastItem
import com.example.weatherapp.data.model.HourlyWeather
import com.example.weatherapp.data.repo.WeatherHomeRepo
import com.example.weatherapp.utils.LocationHelper
import com.example.weatherapp.utils.TempUnits
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel(context: Application) : ViewModel() {
    val repo = WeatherHomeRepo(context = context)
    @RequiresApi(Build.VERSION_CODES.O)
    val selectedDate = LocalDate.now()
    val locationProvider = LocationHelper(context)
    private val _locationFlow = MutableStateFlow<WeatherState>(WeatherState.IsLoading)
    val locationFlow: StateFlow<WeatherState> = _locationFlow
    private val _currentUnit = MutableStateFlow("metric")
    init {
        viewModelScope.launch {
          repo.getTempUnit().collect {
              it->
               _currentUnit.value=getUnits(it)
                    fetchLocation(unit = getUnits(it))
                }
        }
    }
    fun refreshLocation(){
        fetchLocation( _currentUnit.value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
   private fun fetchLocation(unit: String) {
        viewModelScope.launch {
            if (!locationProvider.checkPermissions()) {
                _locationFlow.value = WeatherState.PermissionDisabled

            } else if (!locationProvider.isLocationEnabled()) {
                _locationFlow.value = WeatherState.LocationDisabled

            } else {
                _locationFlow.value = WeatherState.IsLoading
                try {
                    val location = locationProvider.getUserLocation()

                    if (location != null) {
                        loadWeatherData(location,unit)
                    } else {
                        _locationFlow.value = WeatherState.OnError("Unable to get location")
                    }
                } catch (e: Exception) {
                    _locationFlow.value = WeatherState.OnError(e.message ?: "Unknown error")
                }

            }

        }

    }

    private suspend fun loadWeatherData(location: Location,unit: String) {
        repo.loadCountryWeatherData(location.longitude, location.latitude, units = unit )
            .collect { result ->
                _locationFlow.value = result.fold(
                    onSuccess = { response ->
                        val currentWeather = response.list.firstOrNull() ?: ForecastItem.empty()
                        val city = response.city

                        WeatherState.WeatherData(
                            dailyWeatherData = mapToDailyWeather(response),
                            hourlyWeather = mapToHourlyWeather(response),
                            weather = currentWeather,
                            city = city
                        )
                    },
                    onFailure = { WeatherState.OnError(it.message ?: "Unknown error") }
                )
            }
    }
    private  fun getUnits(unit: String):String{

        Log.d("Units","My unit now is : $unit")

        if(unit == TempUnits.FAHRENHEIT.displayName){
         return  "Imperial"
        }
        if(unit==TempUnits.KELVIN.displayName){
            return  "Standard"
        }
        return "metric"
    }
    private fun mapToDailyWeather(forecastData: ForecastData): List<DailyWeather> {
        return forecastData.list
            //filter all the dates from the time
            .map { it.dt_txt.substring(0, 10) }
            // remove the duplicated data
            .distinct()
            .map { date ->
                val items = forecastData.list.filter { it.dt_txt.startsWith(date) }
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
    }
    private fun mapToHourlyWeather(forecastData: ForecastData): List<HourlyWeather> {
        return forecastData.list
            .filter { it.dt_txt.substring(0, 10) == selectedDate.toString() }
            .map { item ->
                HourlyWeather(
                    time = formatTime(item.dt_txt.substring(11, 16)),
                    temp = item.main.temp.toInt(),
                    icon = item.weather.firstOrNull()?.icon.orEmpty()
                )
            }.toList()

    }

    private fun formatTime(timeStr: String): String {
        val parts = timeStr.split(":")
        var hours = parts[0].toInt()
        val period = if (hours >= 12) "PM" else "AM"
        if (hours > 12) hours -= 12
        if (hours == 0) hours = 12

        return "$hours $period"
    }
    //sealed class of the weather states
    sealed class WeatherState {
        object IsLoading : WeatherState()
        object PermissionDisabled : WeatherState()
        object LocationDisabled : WeatherState()
        class OnError(val errorMessage: String) : WeatherState()
        class WeatherData(
            val weather: ForecastItem,
            val hourlyWeather: List<HourlyWeather>,
            val dailyWeatherData: List<DailyWeather>,
            val city: City
        ) : WeatherState()
    }

}

@Suppress("UNCHECKED_CAST")
class WeatherFactory(val context: Application) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(context) as T
    }
}