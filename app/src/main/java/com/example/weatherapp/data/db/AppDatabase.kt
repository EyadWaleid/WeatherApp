package com.example.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.datasource.local.FavDao
import com.example.weatherapp.data.datasource.local.ForecastDao
import com.example.weatherapp.data.model.ConuntryWeatherConvertor
import com.example.weatherapp.data.model.CountryForecast
import com.example.weatherapp.data.model.FavCity


@Database(
    entities = [CountryForecast::class, FavCity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConuntryWeatherConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
    abstract fun favDao(): FavDao
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
