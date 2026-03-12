package com.example.weatherapp.screens.mapScreen.viewmodel

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.data.repo.WeatherHomeRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.maplibre.spatialk.geojson.Position
import java.io.IOException
import java.util.Locale

class MapViewModel(val  context: Application)  : ViewModel(){
    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address
    val repo= WeatherHomeRepo(context = context)
    private val _selectedPosition = MutableStateFlow(Position(31.2357, 30.0444))
    val selectedPosition: StateFlow<Position> = _selectedPosition

    fun onMapClick(position: Position) {
        _selectedPosition.value = position
        getAddress(position)
    }
/*
    fun saveCityData(favCity: FavCity){
        viewModelScope.launch{
            repo.saveFavcity(favCity)
        }
    }
*/
    fun saveForecastData(favCity: FavCity){
        /*Add save forecast data */
    }
    fun getAddress(position: Position) {
        viewModelScope.launch {
            val geoCoder = Geocoder(context, Locale.getDefault())
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geoCoder.getFromLocation(position.latitude, position.longitude, 1) { addresses ->
                        _address.value = if (addresses.isNotEmpty())
                            formatAddress(addresses[0])
                        else "Unknown Location"
                    }
                } else {
                    val addresses = geoCoder.getFromLocation(position.latitude, position.longitude, 1)
                    _address.value = if (addresses?.isNotEmpty() == true)
                        formatAddress(addresses[0])
                    else "Unknown Location"
                }
            } catch (e: IOException) {
                _address.value = "Unknown Location"
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
class MapFactory(val  context: Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(context) as T
    }
}