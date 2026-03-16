package com.example.weatherapp.data.datasource.local.datasource

import android.app.Application
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.entity.UserAlerts
import kotlinx.coroutines.flow.Flow

class AlarmDataSource(context: Application) {

    private val alarmDao = AppDatabase
        .getInstance(context)
        .alarmDao()

    suspend fun insertAlarm(userAlerts: UserAlerts): Long {
        return alarmDao.insertUserAlarm(userAlerts)
    }

    suspend fun getAllAlarms(): Flow<List<UserAlerts>> {
        return alarmDao.getUserAlarms()
    }

    suspend fun deleteAlarm(userAlerts: UserAlerts) {
        alarmDao.deleteCity(userAlerts)
    }

    suspend fun updateAlarm(userAlerts: UserAlerts) {
        alarmDao.updateAlarm(userAlerts)
    }

    suspend fun getAlarmById(alarmId: Long): UserAlerts? {
      return  alarmDao.getAlarmById(alarmId)
    }
}