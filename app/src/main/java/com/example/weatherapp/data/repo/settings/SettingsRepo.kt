package com.example.weatherapp.data.repo.settings

import android.app.Application
import com.example.weatherapp.data.datasource.local.datasource.usersettings.IUserPreferences
import com.example.weatherapp.data.datasource.local.datasource.usersettings.UserPreferences
import kotlinx.coroutines.flow.Flow

class SettingsRepo(private val userPreferences: IUserPreferences) : ISettingsRepo {
    override fun getTempUnit() = userPreferences.getTempUnit()
    override fun getWindUnit() = userPreferences.getWindUnit()
    override fun getLanguage() = userPreferences.getLanguage()
    override fun getLocationSource() = userPreferences.getLocationSource()
    override fun getMapLocation() = userPreferences.getMapLocation()
    override suspend fun setTempUnit(unit: String) = userPreferences.setTempUnit(unit)
    override suspend fun setWindUnit(unit: String) = userPreferences.setWindUnit(unit)
    override suspend fun setLanguage(lang: String) = userPreferences.setLanguage(lang)
    override suspend fun setLocationSource(source: String) = userPreferences.setLocationSource(source)
    override suspend fun setMapLocation(lat: Double, lon: Double) = userPreferences.setMapLocation(lat, lon)
}