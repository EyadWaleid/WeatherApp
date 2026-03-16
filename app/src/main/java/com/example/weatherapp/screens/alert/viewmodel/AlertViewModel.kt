package com.example.weatherapp.screens.alert.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.entity.UserAlerts
import com.example.weatherapp.data.repo.alert.AlertRepo
import com.example.weatherapp.utils.connectivity.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel(private  val  context: Application) : ViewModel() {

    sealed class AlertState {
        object Loading : AlertState()
        object Empty : AlertState()
        data class Data(val userAlerts: List<UserAlerts>) : AlertState()
        data class Error(val message: String) : AlertState()
    }

    private val repo = AlertRepo(context)
    private val _alertState = MutableStateFlow<AlertState>(AlertState.Loading)
    val alertState: StateFlow<AlertState> = _alertState
    private val _snackbarEvent = MutableStateFlow<String?>(null)
    val snackbarEvent: StateFlow<String?> = _snackbarEvent

    init {
        viewModelScope.launch {
            repo.getAllAlarms().collect { alarms ->
                _alertState.value = if (alarms.isEmpty()) AlertState.Empty
                else AlertState.Data(alarms)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertAlarm(userAlerts: UserAlerts) {
        if(!NetworkMonitor(context).isInternetAvailable()){
            _snackbarEvent.value="check your connectivity"
            return
        }
        viewModelScope.launch {
            repo.insertAlarm(userAlerts)
        }


    }

    fun deleteAlarm(userAlerts: UserAlerts) {
        if(!NetworkMonitor(context).isInternetAvailable()){
            _snackbarEvent.value="check your connectivity"
            return
        }
        viewModelScope.launch {
            repo.deleteAlarm(userAlerts)
        }

    }

    fun closeAlarm(id: Long) {
        viewModelScope.launch {
            repo.turnOffAlarm(id)
        }
    }

    fun clearEvent() {
        _snackbarEvent.value = null
    }
}

class AlertViewModelFactory(val context: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertViewModel(context) as T
    }
}