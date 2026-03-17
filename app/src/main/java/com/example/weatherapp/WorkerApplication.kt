package com.example.weatherapp

import android.app.Application
import androidx.work.WorkManager
import com.example.weatherapp.data.datasource.local.datasource.alarm.AlarmDataSource
import com.example.weatherapp.data.datasource.local.datasource.fav.FavLocalDataSource
import com.example.weatherapp.data.datasource.local.datasource.usersettings.UserPreferences
import com.example.weatherapp.data.datasource.local.datasource.weather.WeatherLocalDatasource
import com.example.weatherapp.data.datasource.remote.WeatherDataSource
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.repo.alert.AlertRepo
import com.example.weatherapp.data.repo.homeRepo.IWeatherHomeRepo
import com.example.weatherapp.data.repo.homeRepo.WeatherHomeRepo
import com.example.weatherapp.data.repo.settings.ISettingsRepo
import com.example.weatherapp.data.repo.settings.SettingsRepo
import com.example.weatherapp.utils.connectivity.INetworkMonitor
import com.example.weatherapp.utils.connectivity.NetworkMonitor
import com.example.weatherapp.utils.geoCoder.GeocoderHelper
import com.example.weatherapp.utils.geoCoder.IGeocoder
import com.example.weatherapp.utils.location.ILocationHelper
import com.example.weatherapp.utils.location.LocationHelper

class WorkerApplication : Application() {
    val workManager by lazy { WorkManager.getInstance(this) }
    val database by lazy { AppDatabase.getInstance(this)}
    val userPreferences by lazy { UserPreferences(this) }
    val settingsRepo: ISettingsRepo by lazy { SettingsRepo(userPreferences) }
    val weatherDataSource by lazy { WeatherDataSource() }
    val  alarmDatasource by lazy{ AlarmDataSource(database.alarmDao()) }

    val alertRepo by lazy { AlertRepo(alarmDatasource,workManager) }
    val localDatasource by lazy { WeatherLocalDatasource(database.forecastDao()) }
    val favLocalDataSource by lazy { FavLocalDataSource(database.favDao()) }
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