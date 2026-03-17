package com.example.weatherapp.screens.alert.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.entity.UserAlerts
import com.example.weatherapp.data.repo.alert.AlertRepo
import com.example.weatherapp.utils.connectivity.INetworkMonitor
import com.example.weatherapp.utils.connectivity.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel( private  val  repo:AlertRepo,private val networkMonitor: INetworkMonitor) : ViewModel() {

    sealed class AlertState {
        object Loading : AlertState()
        object Empty : AlertState()
        data class Data(val userAlerts: List<UserAlerts>) : AlertState()
        data class Error(val message: String) : AlertState()
    }


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
        if(!networkMonitor.isInternetAvailable()){
            _snackbarEvent.value="check your connectivity"
            return
        }
        viewModelScope.launch {
            repo.insertAlarm(userAlerts)
        }


    }

    fun deleteAlarm(userAlerts: UserAlerts)
    {
        if(!networkMonitor.isInternetAvailable()){
            _snackbarEvent.value="check your connectivity"
            return
        }
        viewModelScope.launch {
            repo.deleteAlarm(userAlerts)
        }

    }
    fun checkConnectivity(): Boolean{
        if(networkMonitor.isInternetAvailable()){
            return true
        }
        _snackbarEvent.value="check your connectivity"
        return false
    }


    fun clearEvent() {
        _snackbarEvent.value = null
    }
}

class AlertViewModelFactory(private  val  repo:AlertRepo,private val networkMonitor: INetworkMonitor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertViewModel(repo,networkMonitor) as T
    }
}