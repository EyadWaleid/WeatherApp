package com.example.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.datasource.local.dao.AlarmDao
import com.example.weatherapp.data.datasource.local.dao.FavDao
import com.example.weatherapp.data.datasource.local.dao.ForecastDao
import com.example.weatherapp.data.model.ConuntryWeatherConvertor
import com.example.weatherapp.data.model.entity.UserAlerts
import com.example.weatherapp.data.model.entity.CountryForecast
import com.example.weatherapp.data.model.entity.FavCity


@Database(
    entities = [CountryForecast::class, FavCity::class, UserAlerts::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConuntryWeatherConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
    abstract fun favDao(): FavDao
    abstract  fun alarmDao(): AlarmDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
