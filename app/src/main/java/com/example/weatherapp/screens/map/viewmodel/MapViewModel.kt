package com.example.weatherapp.screens.map.viewmodel

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repo.settings.SettingsRepo
import com.example.weatherapp.data.repo.homeRepo.WeatherHomeRepo
import com.example.weatherapp.data.repo.settings.ISettingsRepo
import com.example.weatherapp.utils.location.LocationHelper
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.geoCoder.IGeocoder
import com.example.weatherapp.utils.location.ILocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.maplibre.spatialk.geojson.Position
import java.io.IOException
import java.util.Locale

class MapViewModel(
    private val locationProvider: ILocationHelper,
    private val settingsRepo: ISettingsRepo,
    private val geocoder: IGeocoder
) : ViewModel() {

    private val _mapState = MutableStateFlow<MapState>(MapState.Loading)
    val mapState: StateFlow<MapState> = _mapState


    init {
        viewModelScope.launch {
            val source = settingsRepo.getLocationSource().first()
            if (source == LocationSource.MAP.displayName) {
                val mapLocation = settingsRepo.getMapLocation().first()
                _mapState.value = MapState.Idle
                onMapClick(Position(mapLocation.second, mapLocation.first))
            } else {
                try {
                    val location = locationProvider.getUserLocation()
                    _mapState.value = MapState.Idle
                    if (location != null) {
                        onMapClick(Position(location.longitude, location.latitude))
                    }
                } catch (e: Exception) {
                    _mapState.value = MapState.Idle
                }
            }
        }
    }

    fun onMapClick(position: Position) {
        viewModelScope.launch {
            val address = geocoder.getAddressFromLocation(
                lat = position.latitude,
                lon = position.longitude
            )
            _mapState.value = if (address != "Unknown Location")
                MapState.LocationSelected(position, address)
            else
                MapState.Error("Unknown Location")
        }
    }

    sealed class MapState {
        object Loading : MapState()
        object Idle : MapState()
        data class LocationSelected(val position: Position, val address: String) : MapState()
        data class Error(val message: String) : MapState()
    }
}


class MapFactory(private val locationProvider: ILocationHelper,
                 private val settingsRepo: ISettingsRepo,
                 private val geocoder: IGeocoder    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(locationProvider,settingsRepo,geocoder) as T
    }}