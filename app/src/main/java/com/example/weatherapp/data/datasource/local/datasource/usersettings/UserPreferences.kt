package com.example.weatherapp.data.datasource.local.datasource.usersettings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.constants.TempUnits
import com.example.weatherapp.utils.constants.Units
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) : IUserPreferences {

    private val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore(name = "userSettings")

    companion object {
        val LOCATION_SOURCE = stringPreferencesKey("location_source")
        val MAP_LAT = doublePreferencesKey("map_lat")
        val MAP_LON = doublePreferencesKey("map_lon")
        val LANGUAGE = stringPreferencesKey("lang")
        val TEMPUNIT = stringPreferencesKey("temp_unit")
        val WINDUNIT = stringPreferencesKey("wind_speed_unit")
    }

    override fun getTempUnit(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[TEMPUNIT] ?: TempUnits.CELSIUS.displayName
        }
    }

    override fun getWindUnit(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[WINDUNIT] ?: Units.METERS_PER_SECOND.displayName
        }
    }

    override fun getLanguage(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[LANGUAGE] ?: "en"
        }
    }

    override fun getLocationSource(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[LOCATION_SOURCE] ?: LocationSource.GPS.displayName
        }
    }

    override fun getMapLocation(): Flow<Pair<Double, Double>> {
        return context.dataStore.data.map { prefs ->
            Pair(prefs[MAP_LAT] ?: 0.0, prefs[MAP_LON] ?: 0.0)
        }
    }

    override suspend fun setTempUnit(unit: String) {
        context.dataStore.edit { prefs ->
            prefs[TEMPUNIT] = unit
        }
    }

    override suspend fun setWindUnit(unit: String) {
        context.dataStore.edit { prefs ->
            prefs[WINDUNIT] = unit
        }
    }

    override suspend fun setLanguage(lang: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE] = lang
        }
    }

    override suspend fun setLocationSource(source: String) {
        context.dataStore.edit { prefs ->
            prefs[LOCATION_SOURCE] = source
        }
    }

    override suspend fun setMapLocation(lat: Double, lon: Double) {
        context.dataStore.edit { prefs ->
            prefs[MAP_LAT] = lat
            prefs[MAP_LON] = lon
        }
    }
}