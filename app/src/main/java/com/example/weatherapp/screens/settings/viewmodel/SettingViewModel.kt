package com.example.weatherapp.screens.settings.viewmodel
import android.app.Activity
import android.app.Application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repo.SettingsRepo
import com.example.weatherapp.utils.AppLocalization
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.constants.UserSettings
import com.example.weatherapp.utils.connectivity.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingViewModel(
   private val context: Application,
    private  val activityContext: Activity,
) : ViewModel() {
    private val _settingsState = MutableStateFlow<SettingsState>(SettingsState.Loading)
    private val settingsRepo= SettingsRepo(context=context)
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
                ) { temp, wind, lang ,location ->
                    UserSettings(
                        tempUnit = temp,
                        windUnit = wind,
                        lang = lang,
                        locationSource = location
                    )
                }.collect { (temp, wind, lang,location) ->
                    _settingsState.value = SettingsState.Data(
                        tempUnit = temp,
                        windUnit = wind,
                        language = lang,
                        locationSource = location

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
        viewModelScope.launch { settingsRepo.setTempUnit(unit) }
    }
    fun setWindUnit(unit: String) {
        if (!NetworkMonitor(context).isInternetAvailable()) {
            _snackbarEvent.value = "No internet connection"
            return
        }
        viewModelScope.launch { settingsRepo.setWindUnit(unit) }
    }
    fun setLanguage(lang: String) {
        if (!NetworkMonitor(context).isInternetAvailable()) {
            _snackbarEvent.value = "No internet connection"
            return
        }
        viewModelScope.launch { settingsRepo.setLanguage(lang) }
        when(lang) {
            "en" -> AppLocalization.changeLanguage(activityContext, "en")
            else -> AppLocalization.changeLanguage(activityContext, "ar")
        }
    }
    fun checkConnectivity() : Boolean{
        if(NetworkMonitor(context).isInternetAvailable()){
           return true
        }
        else{
            _snackbarEvent.value = "No internet connection"
            return false
        }



    }
     fun  setLocationSourceToMap(){
        viewModelScope.launch {
            settingsRepo.setLocationSource(LocationSource.GPS.displayName)

        }
    }
    fun setMapLocation(lon: Double,lat: Double){
        if(NetworkMonitor(context).isInternetAvailable()){
            viewModelScope.launch {
         settingsRepo.setLocationSource(LocationSource.MAP.displayName)
         settingsRepo.setMapLocation(lat = lat, lon = lon)
        } }
        else{
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

class SettingViewModelFactory(val context: Application,val activityContext: Activity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingViewModel(context,activityContext) as T
    }
}