package com.example.weatherapp.screens.settings.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import com.example.weatherapp.data.repo.WeatherHomeRepo
import com.example.weatherapp.utils.AppLocalization
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingViewModel(
    val context: Application,
    val activityContext: Activity,
    val repo: WeatherHomeRepo = WeatherHomeRepo(context = context)
) : ViewModel() {
    private val _settingsState = MutableStateFlow<SettingsState>(SettingsState.Loading)
    val settingsState: StateFlow<SettingsState> = _settingsState

    init {
        observeSettings()
    }
    private fun observeSettings() {
        viewModelScope.launch {
            try {
                combine(
                    repo.getTempUnit(),
                    repo.getWindUnit(),
                    repo.getLanguage()
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

    fun setTempUnit(unit: String) {
        viewModelScope.launch { repo.setTempUnit(unit) }
    }

    fun setWindUnit(unit: String) {
        viewModelScope.launch { repo.setWindUnit(unit) }
    }


    fun setLanguage(lang: String) {
        Log.d("Localize","Enter here in viewModel with $lang")
        viewModelScope.launch {
            repo.setLanguage(lang)
        }
        when(lang){
            "en"->{
                AppLocalization.changeLanguage(activityContext,"en")
            }
            else -> {
                AppLocalization.changeLanguage(activityContext,"ar")
            }
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