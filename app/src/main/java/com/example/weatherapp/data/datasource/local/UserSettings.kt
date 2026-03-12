package com.example.weatherapp.data.datasource.local
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.utils.TempUnits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class  UserSettings{

    companion object {

        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSettings")

        val LANGUAGE = stringPreferencesKey("lang")
        val TEMPUNIT = stringPreferencesKey("temp_unit")
        val WINDUNIT = stringPreferencesKey("wind_speed_unit")
        suspend fun setLanguage(context: Context, lang: String) {
            context.dataStore.edit { prefs ->
                prefs[LANGUAGE] = lang
            }
        }

        fun getLanguage(context: Context): Flow<String> {
            return context.dataStore.data.map { prefs ->
                prefs[LANGUAGE] ?: "en"
            }
        }
        suspend fun setTempUnit(context: Context, unit: String) {
            context.dataStore.edit { prefs ->
                prefs[TEMPUNIT] = unit
            }
        }
        fun getTempUnit(context: Context): Flow<String> {
            return context.dataStore.data.map { prefs ->
                prefs[TEMPUNIT] ?: TempUnits.CELSIUS.displayName
            }
        }
        suspend fun setWindUnit(context: Context, unit: String) {
            context.dataStore.edit { prefs ->
                prefs[WINDUNIT] = unit
            }
        }
        fun getWindUnit(context: Context): Flow<String> {
            return context.dataStore.data.map { prefs ->
                prefs[WINDUNIT] ?: "m/s"
            }
        }

    }
}