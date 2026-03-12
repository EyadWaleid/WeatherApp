package com.example.weatherapp.screens.settings.viewmodel
import android.app.Activity
import android.app.Application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repo.SettingsRepo
import com.example.weatherapp.data.repo.WeatherHomeRepo
import com.example.weatherapp.utils.AppLocalization
import com.example.weatherapp.utils.connectivity.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingViewModel(
    val context: Application,
    val activityContext: Activity,
) : ViewModel() {
    private val _settingsState = MutableStateFlow<SettingsState>(SettingsState.Loading)
    val settingsState: StateFlow<SettingsState> = _settingsState
    private val _snackbarEvent = MutableStateFlow<String?>(null)
    val snackbarEvent: StateFlow<String?> = _snackbarEvent
    val userSetting= SettingsRepo(context=context)

    init {
        observeSettings()
    }
    private fun observeSettings() {
        viewModelScope.launch {
            try {
                combine(
                    userSetting.getTempUnit(),
                    userSetting.getWindUnit(),
                    userSetting.getLanguage()
                ) { temp, wind, lang ->
                    Triple(temp, wind, lang)
                }.collect { (temp, wind, lang) ->
                    _settingsState.value = SettingsState.Data(
                        tempUnit = temp,
                        windUnit = wind,
                        language = lang
                    )
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
        if (!NetworkMonitor(context).isInternetAvailable()) {
            _snackbarEvent.value = "No internet connection"
            return
        }
        viewModelScope.launch { userSetting.setTempUnit(unit) }
    }

    fun setWindUnit(unit: String) {
        if (!NetworkMonitor(context).isInternetAvailable()) {
            _snackbarEvent.value = "No internet connection"
            return
        }
        viewModelScope.launch { userSetting.setWindUnit(unit) }
    }

    fun setLanguage(lang: String) {
        if (!NetworkMonitor(context).isInternetAvailable()) {
            _snackbarEvent.value = "No internet connection"
            return
        }
        viewModelScope.launch { userSetting.setLanguage(lang) }
        when(lang) {
            "en" -> AppLocalization.changeLanguage(activityContext, "en")
            else -> AppLocalization.changeLanguage(activityContext, "ar")
        }
    }
    sealed class SettingsState {
        object Loading : SettingsState()
        data class Data(
            val tempUnit: String,
            val windUnit: String,
            val language: String
        ) : SettingsState()

        data class Error(val message: String) : SettingsState()
    }
}

class SettingViewModelFactory(val context: Application,val activityContext: Activity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingViewModel(context,activityContext) as T
    }
}