package com.example.weatherapp.screens.home.view_model
import android.app.Application
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.weatherapp.data.model.DailyWeather
import com.example.weatherapp.data.model.HourlyWeather
import com.example.weatherapp.data.repo.SettingsRepo
import com.example.weatherapp.data.repo.WeatherHomeRepo
import com.example.weatherapp.utils.LocationHelper
import com.example.weatherapp.utils.TempUnits
import com.example.weatherapp.utils.Units
import com.example.weatherapp.utils.WeatherMapper.convertWind
import com.example.weatherapp.utils.WeatherMapper.getUnits
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel(context: Application) : ViewModel() {
    val repo = WeatherHomeRepo(context = context)
    val userSettingsRepo= SettingsRepo(context=context)
    @RequiresApi(Build.VERSION_CODES.O)
    val locationProvider = LocationHelper(context)
    private val _locationFlow = MutableStateFlow<WeatherState>(WeatherState.IsLoading)
    val locationFlow: StateFlow<WeatherState> = _locationFlow
    private val _currentUnit = MutableStateFlow("metric")
    private val _currentWindUnit = MutableStateFlow("m/s")
    private val _currentLang = MutableStateFlow("en")
    init {
        viewModelScope.launch {
            combine(
                userSettingsRepo.getTempUnit(),
                userSettingsRepo.getLanguage(),
                userSettingsRepo.getWindUnit(),

            ){
                temp,lang,wind->
                Triple(temp,lang,wind) }.collect {
                    (temp ,lang,wind)->
                _currentUnit.value=getUnits(temp)
                _currentLang.value=lang
                if(_currentWindUnit.value!=wind){
                    _currentWindUnit.value=wind
                    val currentState = _locationFlow.value
                    if (currentState is WeatherState.WeatherData) {
                        _locationFlow.value = currentState.copy(
                            windUnit = wind,
                            weather = currentState.weather.copy(
                                wind = convertWind(currentState.weather.wind, wind,lang)
                            ),
                            dailyWeatherData = currentState.dailyWeatherData.map {
                                it.copy(wind = convertWind(
                                    it.wind, wind,
                                    lang = lang
                                ))
                            }
                        )
                }
                    else{

                        fetchLocation(unit = getUnits(temp),lang=lang)
                    }
                }
                else{
                    fetchLocation(getUnits(temp),lang)
                }


                }
        }
    }

    fun refreshLocation(){
        fetchLocation( unit = _currentUnit.value,lang=_currentLang.value)
    }
    @RequiresApi(Build.VERSION_CODES.O)
   private fun fetchLocation(unit: String,lang:String) {

        viewModelScope.launch {
            if (!locationProvider.checkPermissions()) {
                _locationFlow.value = WeatherState.PermissionDisabled

            }
            else if (!locationProvider.isLocationEnabled()) {
                _locationFlow.value = WeatherState.LocationDisabled

            }
            else {
                _locationFlow.value = WeatherState.IsLoading
                try {
                    val location = locationProvider.getUserLocation()
                    if (location != null) {

                        loadWeatherData(location,unit,lang)
                    } else {
                        _locationFlow.value = WeatherState.OnError("Unable to get location")
                    }
                } catch (e: Exception) {
                    _locationFlow.value = WeatherState.OnError(e.message ?: "Unknown error")
                }

            }
        }
    }
    private fun loadWeatherData(location: Location, unit: String, lang: String) {
        viewModelScope.launch {
            val result = repo.loadCountryWeatherData(
                lon = location.longitude,
                lat = location.latitude,
                units = unit,
                lang = lang
            )
            _locationFlow.value = result.fold(
                onSuccess = { response ->
                    val convertedDays = when {
                        _currentUnit.value == "imperial" && _currentWindUnit.value == Units.METERS_PER_SECOND.displayName ->
                            response.weatherOfDays.map { it.copy(wind = it.wind / 2.237) }
                        _currentUnit.value != "imperial" && _currentWindUnit.value == Units.MILES_PER_HOUR.displayName ->
                            response.weatherOfDays.map { it.copy(wind = it.wind * 2.237) }
                        else -> response.weatherOfDays
                    }
                    WeatherState.WeatherData(
                        dailyWeatherData = convertedDays.drop(1),
                        hourlyWeather = convertedDays.first().hoursOfDayForecast,
                        weather = convertedDays.first(),
                        city = response.city,
                        country = response.countryCode,
                        windUnit = _currentWindUnit.value,
                        tempUnit = _currentUnit.value
                    )
                },
                onFailure = { WeatherState.OnError(it.message ?: "Unknown error") }
            )
        }
    }
    sealed class WeatherState {
        object IsLoading : WeatherState()
        object PermissionDisabled : WeatherState()
        object LocationDisabled : WeatherState()
        class OnError(val errorMessage: String) : WeatherState()
       data class WeatherData(
            val weather: DailyWeather,
            val hourlyWeather: List<HourlyWeather>,
            val dailyWeatherData: List<DailyWeather>,
            val city: String,
            val country:String,
            val tempUnit:String,
            val windUnit: String
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