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
import com.example.weatherapp.utils.location.LocationHelper
import com.example.weatherapp.utils.constants.LocationSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.maplibre.spatialk.geojson.Position
import java.io.IOException
import java.util.Locale

class MapViewModel(val context: Application) : ViewModel() {
    private val _mapState = MutableStateFlow<MapState>(MapState.Loading)
    val mapState: StateFlow<MapState> = _mapState
    private val locationProvider = LocationHelper(context)
    private val settingsRepo = SettingsRepo(context)
    private val repo = WeatherHomeRepo(context = context)

    sealed class MapState {
        object Loading : MapState()
        object Idle : MapState()
        data class LocationSelected(
            val position: Position,
            val address: String
        ) : MapState()

        data class Error(val message: String) : MapState()
    }


    init {
        viewModelScope.launch {
            val source = settingsRepo.getLocationSource().first()
            if (source == LocationSource.MAP.displayName) {
                val mapLocation = settingsRepo.getMapLocation().first()
                _mapState.value = MapState.Idle
                onMapClick(Position(mapLocation.second, mapLocation.first)) // lon, lat
            } else {
                try {
                    val location = locationProvider.getUserLocation()
                    if (location != null) {
                        _mapState.value = MapState.Idle
                        onMapClick(Position(location.longitude, location.latitude))
                    } else {
                        _mapState.value = MapState.Idle
                    }
                } catch (e: Exception) {
                    _mapState.value = MapState.Idle
                }
            }
        }
    }


    fun onMapClick(position: Position) {
        getAddress(position)
    }

    fun getAddress(position: Position) {
        viewModelScope.launch {
            val geoCoder = Geocoder(context, Locale.getDefault())
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geoCoder.getFromLocation(
                        position.latitude,
                        position.longitude,
                        1
                    ) { addresses ->
                        _mapState.value = if (addresses.isNotEmpty())
                            MapState.LocationSelected(position, formatAddress(addresses[0]))
                        else MapState.Error("Unknown Location")
                    }
                } else {
                    val addresses =
                        geoCoder.getFromLocation(position.latitude, position.longitude, 1)
                    _mapState.value = if (addresses?.isNotEmpty() == true)
                        MapState.LocationSelected(position, formatAddress(addresses[0]))
                    else MapState.Error("Unknown Location")
                }
            } catch (e: IOException) {
                _mapState.value = MapState.Error("Unknown Location")
            }
        }
    }


    private fun formatAddress(address: Address): String {
        val city = address.locality
        val country = address.countryName
        return when {
            city != null && country != null -> "$city, $country"
            city != null -> city
            country != null -> country
            else -> "Unknown Location"
        }
    }
}

class MapFactory(val context: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(context) as T
    }
}