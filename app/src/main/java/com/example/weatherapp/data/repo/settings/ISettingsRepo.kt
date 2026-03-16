package com.example.weatherapp.data.repo.settings

import kotlinx.coroutines.flow.Flow

interface ISettingsRepo {
        fun getTempUnit(): Flow<String>
        fun getWindUnit(): Flow<String>
        fun getLanguage(): Flow<String>
        fun getLocationSource(): Flow<String>
        fun getMapLocation(): Flow<Pair<Double, Double>>

        suspend fun setTempUnit(unit: String)
        suspend fun setWindUnit(unit: String)
        suspend fun setLanguage(lang: String)
        suspend fun setLocationSource(source: String)
        suspend fun setMapLocation(lat: Double, lon: Double)

}