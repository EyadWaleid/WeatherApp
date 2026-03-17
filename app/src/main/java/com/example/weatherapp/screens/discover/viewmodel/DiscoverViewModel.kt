package com.example.weatherapp.screens.discover.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.entity.FavCity
import com.example.weatherapp.data.repo.homeRepo.IWeatherHomeRepo
import com.example.weatherapp.data.repo.settings.ISettingsRepo
import com.example.weatherapp.utils.constants.UserSettings
import com.example.weatherapp.utils.WeatherMapper.getUnits
import com.example.weatherapp.utils.connectivity.INetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.maplibre.spatialk.geojson.Position

class DiscoverViewModel(
    private val repo: IWeatherHomeRepo,
    private val networkMonitor: INetworkMonitor,
    private val userSettings: ISettingsRepo,
) : ViewModel() {
    sealed class FavState {
        object Loading : FavState()
        object Empty : FavState()
        data class Data(val cities: List<FavCity>, val units: String) : FavState()
        data class Error(val message: String) : FavState()
    }

    private val _snackbarEvent = MutableStateFlow<String?>(null)
    val snackbarEvent: StateFlow<String?> = _snackbarEvent

    private val _favState = MutableStateFlow<FavState>(FavState.Loading)
    val favState: StateFlow<FavState> = _favState

    var currentUserUnit = UserSettings()

    init {
        viewModelScope.launch {
            combine(
                userSettings.getTempUnit(),
                userSettings.getLanguage()
            ) { temp, lang ->
                UserSettings(tempUnit = getUnits(temp), lang = lang)
            }
                .distinctUntilChanged()
                .collect { newSettings ->
                    currentUserUnit = newSettings
                    refreshAll()
                }
        }

        viewModelScope.launch {
            repo.getAllCountry().collect { cities ->
                _favState.value = if (cities.isEmpty()) FavState.Empty
                else FavState.Data(cities, currentUserUnit.tempUnit)
            }
        }
    }

    private suspend fun refreshAll() {
        val hasData = repo.refreshFavData(currentUserUnit.tempUnit, currentUserUnit.lang)
        if (!hasData) {
            _favState.value = FavState.Empty
        }
    }

    fun saveFavCity(position: Position, address: String) {
        viewModelScope.launch {
            repo.saveFavcity(
                FavCity(
                    name = address,
                    lat = position.latitude,
                    long = position.longitude,
                    countryCode = "",
                    temp = 0.0,
                    tempDescription = ""
                )
            )
            refreshAll()
        }
    }


    fun deleteFavCity(favCity: FavCity) {
        viewModelScope.launch {
            repo.delete(favCity)
        }
    }

    fun checkConnectivity(): Boolean {
        if (!networkMonitor.isInternetAvailable()) {
            _snackbarEvent.value = "Check connectivity"
            return false
        }
        return true
    }

    fun clearEvent() {
        _snackbarEvent.value = null
    }

}

class DiscoverFactoryModel(
    private val repo: IWeatherHomeRepo,
    private val networkMonitor: INetworkMonitor,
    private val userSettings: ISettingsRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiscoverViewModel(repo, networkMonitor, userSettings) as T

    }
}