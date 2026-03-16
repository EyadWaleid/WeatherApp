package com.example.weatherapp.data.datasource.local.datasource
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.constants.TempUnits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class  UserPreferences{

    companion object {

        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSettings")
        val LOCATION_SOURCE = stringPreferencesKey("location_source")
        val MAP_LAT = doublePreferencesKey("map_lat")
        val MAP_LON = doublePreferencesKey("map_lon")
        val LANGUAGE = stringPreferencesKey("lang")
        val TEMPUNIT = stringPreferencesKey("temp_unit")
        val WINDUNIT = stringPreferencesKey("wind_speed_unit")
        suspend fun setLanguage(context: Context, lang: String) {
            context.dataStore.edit { prefs ->
                prefs[LANGUAGE] = lang
            }
        }

        suspend fun setTempUnit(context: Context, unit: String) {
            context.dataStore.edit { prefs ->
                prefs[TEMPUNIT] = unit
            }
        }
        suspend fun setMapLocation(context: Context, lat: Double, lon: Double) {
            context.dataStore.edit { prefs ->
                prefs[MAP_LAT] = lat
                prefs[MAP_LON] = lon
            }
        }

        suspend fun setWindUnit(context: Context, unit: String) {
            context.dataStore.edit { prefs ->
                prefs[WINDUNIT] = unit
            }
        }

        suspend fun setLocationSource(context: Context, source: String) {
            context.dataStore.edit { prefs -> prefs[LOCATION_SOURCE] = source }
        }

        fun getLocationSource(context: Context): Flow<String> {
            return context.dataStore.data.map { prefs -> prefs[LOCATION_SOURCE] ?: LocationSource.GPS.displayName }
        }

        fun getLanguage(context: Context): Flow<String> {
            return context.dataStore.data.map { prefs ->
                prefs[LANGUAGE] ?: "en"
            }
        }
        fun getWindUnit(context: Context): Flow<String> {
            return context.dataStore.data.map { prefs ->
                prefs[WINDUNIT] ?: "m/s"
            }
        }

        fun getMapLocation(context: Context): Flow<Pair<Double, Double>> {
            return context.dataStore.data.map { prefs ->
                Pair(prefs[MAP_LAT] ?: 0.0, prefs[MAP_LON] ?: 0.0)
            }
        }
        fun getTempUnit(context: Context): Flow<String> {
            return context.dataStore.data.map { prefs ->
                prefs[TEMPUNIT] ?: TempUnits.CELSIUS.displayName
            }
        }

    }
}