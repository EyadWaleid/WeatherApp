package com.example.weatherapp.screens.discoverScreen.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.data.repo.SettingsRepo
import com.example.weatherapp.data.repo.WeatherHomeRepo
import com.example.weatherapp.utils.WeatherMapper.getUnits
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.maplibre.spatialk.geojson.Position
class DiscoverViewModel(val context: Application) : ViewModel() {
    sealed class FavState {
        object Loading : FavState()
        object Empty : FavState()
        data class Data(val cities: List<FavCity>) : FavState()
        data class Error(val message: String) : FavState()
    }

    private val _favState = MutableStateFlow<FavState>(FavState.Loading)
    val favState: StateFlow<FavState> = _favState
    private val _currentUnit = MutableStateFlow("metric")
    private val _currentLang = MutableStateFlow("en")
    val repo = WeatherHomeRepo(context = context)
    val userSettings = SettingsRepo(context = context)

    init {
        viewModelScope.launch {
            repo.getAllCountry().collect { cities ->
                _favState.value = if (cities.isEmpty()) FavState.Empty else FavState.Data(cities)
            }
        }
        viewModelScope.launch {
            combine(
                userSettings.getTempUnit(),
                userSettings.getLanguage()
            ) { temp, lang -> Pair(temp, lang) }
                .collect { (temp, lang) ->
                    _currentUnit.value = getUnits(temp)
                    _currentLang.value = lang
                    refreshAll()
                }
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

    private suspend fun refreshAll() {
        _favState.value = FavState.Loading
        repo.refreshFavData(_currentUnit.value, _currentLang.value)
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
