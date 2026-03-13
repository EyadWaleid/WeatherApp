package com.example.weatherapp.screens.discover.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.data.repo.SettingsRepo
import com.example.weatherapp.data.repo.WeatherHomeRepo
import com.example.weatherapp.utils.UserSettings
import com.example.weatherapp.utils.WeatherMapper.getUnits
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.maplibre.spatialk.geojson.Position

class DiscoverViewModel(val context: Application) : ViewModel() {
    sealed class FavState {
        object Loading : FavState()
        object Empty : FavState()
        data class Data(val cities: List<FavCity>, val units: String) : FavState()
        data class Error(val message: String) : FavState()
    }

    private val _favState = MutableStateFlow<FavState>(FavState.Loading)
    val favState: StateFlow<FavState> = _favState
    val repo = WeatherHomeRepo(context = context)
    var currentUserUnit= UserSettings()
    val userSettings = SettingsRepo(context = context)
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

}

class DiscoverFactoryModel(val context: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiscoverViewModel(context) as T

    }
}