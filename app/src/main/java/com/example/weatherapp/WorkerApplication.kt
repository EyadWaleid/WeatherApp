package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.data.datasource.local.datasource.fav.FavLocalDataSource
import com.example.weatherapp.data.datasource.local.datasource.usersettings.UserPreferences
import com.example.weatherapp.data.datasource.local.datasource.weather.WeatherLocalDatasource
import com.example.weatherapp.data.datasource.remote.WeatherDataSource
import com.example.weatherapp.data.repo.homeRepo.IWeatherHomeRepo
import com.example.weatherapp.data.repo.homeRepo.WeatherHomeRepo
import com.example.weatherapp.data.repo.settings.ISettingsRepo
import com.example.weatherapp.data.repo.settings.SettingsRepo
import com.example.weatherapp.utils.connectivity.INetworkMonitor
import com.example.weatherapp.utils.connectivity.NetworkMonitor
import com.example.weatherapp.utils.geoCoder.GeocoderHelper
import com.example.weatherapp.utils.geoCoder.IGeocoder
import com.example.weatherapp.utils.localization.AppLocalization
import com.example.weatherapp.utils.localization.IAppLocalization
import com.example.weatherapp.utils.location.ILocationHelper
import com.example.weatherapp.utils.location.LocationHelper

class WorkerApplication : Application() {

    val userPreferences by lazy { UserPreferences(this) }
    val settingsRepo: ISettingsRepo by lazy { SettingsRepo(userPreferences) }
    val weatherDataSource by lazy { WeatherDataSource() }
    val localDatasource by lazy { WeatherLocalDatasource(this) }
    val favLocalDataSource by lazy { FavLocalDataSource(this) }
    val homeRepo: IWeatherHomeRepo by lazy {
        WeatherHomeRepo(
            weatherDataSource, localDatasource,
            favLocalDataSource =favLocalDataSource
        )
    }

    val networkMonitor: INetworkMonitor by lazy { NetworkMonitor(this) }
    val locationHelper: ILocationHelper by lazy { LocationHelper(this) }
    val geocoderHelper: IGeocoder by lazy { GeocoderHelper(this) }
}