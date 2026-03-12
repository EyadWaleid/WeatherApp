package com.example.weatherapp.data.repo

import android.app.Application
import com.example.weatherapp.data.datasource.local.UserSettings
import kotlinx.coroutines.flow.Flow

class SettingsRepo(val context: Application) {
    fun getTempUnit(): Flow<String> = UserSettings.getTempUnit(context)

    fun getWindUnit(): Flow<String> = UserSettings.getWindUnit(context)

    fun getLanguage(): Flow<String> = UserSettings.getLanguage(context)

    suspend fun setTempUnit(unit: String) = UserSettings.setTempUnit(context, unit)

    suspend fun setWindUnit(unit: String) = UserSettings.setWindUnit(context, unit)

    suspend fun setLanguage(lang: String) = UserSettings.setLanguage(context, lang)
}