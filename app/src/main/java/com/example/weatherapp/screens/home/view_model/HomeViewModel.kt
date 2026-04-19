package com.example.weatherapp.screens.home.view_model

import android.os.Build
import android.util.Log
import com.example.weatherapp.utils.NetworkExceptions
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.entity.DailyWeather
import com.example.weatherapp.data.model.entity.HourlyWeather
import com.example.weatherapp.data.repo.homeRepo.IWeatherHomeRepo
import com.example.weatherapp.data.repo.settings.SettingsRepo
import com.example.weatherapp.data.repo.homeRepo.WeatherHomeRepo
import com.example.weatherapp.data.repo.settings.ISettingsRepo
import com.example.weatherapp.utils.location.LocationHelper
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.constants.Units
import com.example.weatherapp.utils.constants.UserSettings
import com.example.weatherapp.utils.WeatherMapper.convertWind
import com.example.weatherapp.utils.WeatherMapper.getUnits
import com.example.weatherapp.utils.connectivity.INetworkMonitor
import com.example.weatherapp.utils.connectivity.NetworkMonitor
import com.example.weatherapp.utils.location.ILocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val repo: IWeatherHomeRepo,
    private val networkMonitor: INetworkMonitor,
    private val userSettingsRepo: ISettingsRepo,
    private val locationProvider: ILocationHelper
) : ViewModel() {
    private val _snackbarEvent = MutableStateFlow<String?>(null)
    val snackbarEvent: StateFlow<String?> = _snackbarEvent
    fun clearEvent() {
        _snackbarEvent.value = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.IsLoading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private var currentSettings = UserSettings()

    init {
        observeSettings()
    }


    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                userSettingsRepo.getTempUnit(),
                userSettingsRepo.getLanguage(),
                userSettingsRepo.getWindUnit(),
                userSettingsRepo.getLocationSource(),
                userSettingsRepo.getMapLocation()
            ) { temp, lang, wind, source, mapLocation ->
                UserSettings(
                    tempUnit = getUnits(temp),
                    lang = lang,
                    windUnit = wind,
                    locationSource = source,
                    mapLat = mapLocation.first,
                    mapLon = mapLocation.second
                )
            }.distinctUntilChanged().collect { newSettings ->
                val windChanged = currentSettings.windUnit != newSettings.windUnit
                currentSettings = newSettings
                when {
                    windChanged && _weatherState.value is WeatherState.WeatherData -> {
                        updateWindLocally(newSettings.windUnit, newSettings.lang)
                    }
                    else -> fetchLocatoinByLocationSoruce()
                }
            }
        }
    }

    private fun fetchLocatoinByLocationSoruce() {
        when (currentSettings.locationSource) {
            LocationSource.GPS.displayName -> fetchLocation()
            LocationSource.MAP.displayName -> {
                if (!(currentSettings.mapLat == 0.0 && currentSettings.mapLon == 0.0)) {
                    loadWeatherData(
                        long = currentSettings.mapLon, lat = currentSettings.mapLat
                    )
                }
            }
        }
    }

    private fun updateWindLocally(wind: String, lang: String) {
        val state = _weatherState.value as WeatherState.WeatherData
        _weatherState.value = state.copy(
            windUnit = wind,
            weather = state.weather.copy(wind = convertWind(state.weather.wind, wind, lang)),
            dailyWeatherData = state.dailyWeatherData.map {
                it.copy(wind = convertWind(it.wind, wind, lang))
            })
    }

    fun refreshLocation() {
        if (!networkMonitor.isInternetAvailable()) {
            _snackbarEvent.value = "Please check your connectivity"
            return
        }

        fetchLocatoinByLocationSoruce()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchLocation() {

        viewModelScope.launch {
            if (!locationProvider.checkPermissions()) {
                _weatherState.value = WeatherState.PermissionDisabled

            } else if (!locationProvider.isLocationEnabled()) {
                _weatherState.value = WeatherState.LocationDisabled

            } else {
                _weatherState.value = WeatherState.IsLoading
                try {
                    val location = locationProvider.getUserLocation()
                    if (location != null) {

                        loadWeatherData(long = location.longitude, lat = location.latitude)
                    } else {
                        _weatherState.value = WeatherState.OnError("Unable to get location")
                    }
                } catch (e: Exception) {
                    Log.d("LOC", "errrorrr")
                    _weatherState.value = WeatherState.OnError(e.message ?: "Unknown error")
                }

            }
        }
    }


    private fun loadWeatherData(long: Double, lat: Double) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.IsLoading
            val result = repo.loadCountryWeatherData(
                lon = long, lat = lat, units = currentSettings.tempUnit, lang = currentSettings.lang
            )
            _weatherState.value = result.fold(onSuccess = { response ->
                val convertedDays = when {
                    currentSettings.tempUnit == "imperial" && currentSettings.windUnit == Units.METERS_PER_SECOND.displayName -> response.weatherOfDays.map {
                        it.copy(
                            wind = it.wind / 2.237
                        )
                    }

                    currentSettings.tempUnit != "imperial" && currentSettings.windUnit == Units.MILES_PER_HOUR.displayName -> response.weatherOfDays.map {
                        it.copy(
                            wind = it.wind * 2.237
                        )
                    }

                    else -> response.weatherOfDays
                }
                WeatherState.WeatherData(
                    dailyWeatherData = convertedDays.drop(1),
                    hourlyWeather = convertedDays.first().hoursOfDayForecast,
                    weather = convertedDays.first(),
                    city = response.city,
                    country = response.countryCode,
                    windUnit = currentSettings.windUnit,
                    tempUnit = currentSettings.tempUnit
                )
            }, onFailure = {
                e->
                when (e) {
                    is NetworkExceptions ->  WeatherState.OfflineError
                    else ->  WeatherState.OnError(e.message ?: "")
                }
            })
        }
    }
    sealed class WeatherState {
        object IsLoading : WeatherState()
        object PermissionDisabled : WeatherState()
        object LocationDisabled : WeatherState()
        class OnError(val errorMessage: String) : WeatherState()
        object OfflineError : WeatherState()
        data class WeatherData(
            val weather: DailyWeather,
            val hourlyWeather: List<HourlyWeather>,
            val dailyWeatherData: List<DailyWeather>,
            val city: String,
            val country: String,
            val tempUnit: String,
            val windUnit: String
        ) : WeatherState()
    }
}
@Suppress("UNCHECKED_CAST")
class WeatherFactory(
    private val repo: IWeatherHomeRepo,
    private val networkMonitor: INetworkMonitor,
    private val userSettingsRepo: ISettingsRepo,
    private val locationProvider: ILocationHelper
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo, networkMonitor, userSettingsRepo, locationProvider) as T
    }
}
