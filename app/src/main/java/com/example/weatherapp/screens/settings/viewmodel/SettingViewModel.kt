package com.example.weatherapp.screens.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repo.ISettingsRepo
import com.example.weatherapp.data.repo.SettingsRepo
import com.example.weatherapp.utils.connectivity.INetworkMonitor
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.localization.IAppLocalization
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingsRepo: ISettingsRepo,
    private val networkMonitor: INetworkMonitor,
    private val appLocalization: IAppLocalization
) : ViewModel() {
    private val _settingsState = MutableStateFlow<SettingsState>(SettingsState.Loading)
    private val _snackbarEvent = MutableStateFlow<String?>(null)
    val snackbarEvent: StateFlow<String?> = _snackbarEvent

    val settingsState: StateFlow<SettingsState> = _settingsState

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            try {
                combine(
                    settingsRepo.getTempUnit(),
                    settingsRepo.getWindUnit(),
                    settingsRepo.getLanguage(),
                    settingsRepo.getLocationSource(),
                ) { temp, wind, lang, location ->
                    SettingsState.Data(
                        tempUnit = temp,
                        windUnit = wind,
                        language = lang,
                        locationSource = location
                    )
                }.collect { it ->
                    _settingsState.value = it


                }
            } catch (e: Exception) {
                _settingsState.value = SettingsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clearEvent() {
        _snackbarEvent.value = null
    }

    fun setTempUnit(unit: String) {
        if (!networkMonitor.isInternetAvailable()) {
            _snackbarEvent.value = "No internet connection"
            return
        }
        viewModelScope.launch { settingsRepo.setTempUnit(unit) }
    }

    fun setWindUnit(unit: String) {
        if (!networkMonitor.isInternetAvailable()) {
            _snackbarEvent.value = "No internet connection"
            return
        }
        viewModelScope.launch { settingsRepo.setWindUnit(unit) }
    }

    fun setLanguage(lang: String) {
        if (!networkMonitor.isInternetAvailable()) {
            _snackbarEvent.value = "No internet connection"
            return
        }
        viewModelScope.launch { settingsRepo.setLanguage(lang) }
        when (lang) {
            "en" -> appLocalization.changeLanguage("en")
            else -> appLocalization.changeLanguage("ar")
        }
    }

    fun checkConnectivity(): Boolean {
        if (networkMonitor.isInternetAvailable()) {
            return true
        } else {
            _snackbarEvent.value = "No internet connection"
            return false
        }


    }

    fun setLocationSourceToMap() {
        viewModelScope.launch {
            settingsRepo.setLocationSource(LocationSource.GPS.displayName)

        }
    }

    fun setMapLocation(lon: Double, lat: Double) {
        if (networkMonitor.isInternetAvailable()) {
            viewModelScope.launch {
                settingsRepo.setLocationSource(LocationSource.MAP.displayName)
                settingsRepo.setMapLocation(lat = lat, lon = lon)
            }
        } else {
            _snackbarEvent.value = "No internet connection"
            return
        }

    }

    sealed class SettingsState {
        object Loading : SettingsState()
        data class Data(
            val tempUnit: String,
            val windUnit: String,
            val language: String,
            val locationSource: String,
        ) : SettingsState()

        data class Error(val message: String) : SettingsState()
    }
}

class SettingViewModelFactory(
    val settingsRepo: SettingsRepo,
    val networkMonitor: INetworkMonitor,
    val appLocalization: IAppLocalization
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingViewModel(settingsRepo, networkMonitor, appLocalization) as T
    }
}