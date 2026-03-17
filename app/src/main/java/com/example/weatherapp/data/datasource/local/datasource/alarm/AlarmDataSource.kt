package com.example.weatherapp.data.datasource.local.datasource.alarm

import android.app.Application
import com.example.weatherapp.data.datasource.local.dao.AlarmDao
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.entity.UserAlerts
import kotlinx.coroutines.flow.Flow

class AlarmDataSource(private val alarmDao : AlarmDao): IAlarmDataSource {


    override suspend fun insertAlarm(userAlerts: UserAlerts): Long {
        return alarmDao.insertUserAlarm(userAlerts)
    }

     override fun getAllAlarms(): Flow<List<UserAlerts>> {
        return alarmDao.getUserAlarms()
    }

    override suspend fun deleteAlarm(userAlerts: UserAlerts) {
        alarmDao.deleteCity(userAlerts)
    }

    override suspend fun updateAlarm(userAlerts: UserAlerts) {
        alarmDao.updateAlarm(userAlerts)
    }

    override suspend fun getAlarmById(alarmId: Long): UserAlerts? {
      return  alarmDao.getAlarmById(alarmId)
    }
}