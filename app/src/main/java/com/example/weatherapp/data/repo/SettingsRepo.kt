package com.example.weatherapp.data.repo

import android.app.Application
import com.example.weatherapp.data.datasource.local.UserPreferences
import kotlinx.coroutines.flow.Flow

class SettingsRepo(val context: Application) {
    fun getTempUnit(): Flow<String> = UserPreferences.getTempUnit(context)

    fun getWindUnit(): Flow<String> = UserPreferences.getWindUnit(context)

    fun getLanguage(): Flow<String> = UserPreferences.getLanguage(context)
    fun getLocationSource(): Flow<String> = UserPreferences.getLocationSource(context)
    fun getMapLocation(): Flow<Pair<Double, Double>> = UserPreferences.getMapLocation(context)

    suspend fun setTempUnit(unit: String) = UserPreferences.setTempUnit(context, unit)

    suspend fun setWindUnit(unit: String) = UserPreferences.setWindUnit(context, unit)

    suspend fun setLanguage(lang: String) = UserPreferences.setLanguage(context, lang)


    suspend fun setLocationSource(source: String) = UserPreferences.setLocationSource(context, source)
    suspend fun setMapLocation(lat: Double, lon: Double) = UserPreferences.setMapLocation(context, lat, lon)
}