package com.example.weatherapp.data.repo.settings

import android.app.Application
import com.example.weatherapp.data.datasource.local.datasource.UserPreferences
import kotlinx.coroutines.flow.Flow

class SettingsRepo(val context: Application):ISettingsRepo {
    override fun getTempUnit(): Flow<String> = UserPreferences.getTempUnit(context)

    override    fun getWindUnit(): Flow<String> = UserPreferences.getWindUnit(context)

    override fun getLanguage(): Flow<String> = UserPreferences.getLanguage(context)
    override  fun getLocationSource(): Flow<String> = UserPreferences.getLocationSource(context)
    override    fun getMapLocation(): Flow<Pair<Double, Double>> = UserPreferences.getMapLocation(context)

    override    suspend fun setTempUnit(unit: String) = UserPreferences.setTempUnit(context, unit)

    override suspend fun setWindUnit(unit: String) = UserPreferences.setWindUnit(context, unit)

    override    suspend fun setLanguage(lang: String) = UserPreferences.setLanguage(context, lang)


    override suspend fun setLocationSource(source: String) = UserPreferences.setLocationSource(context, source)
    override suspend fun setMapLocation(lat: Double, lon: Double) = UserPreferences.setMapLocation(context, lat, lon)
}