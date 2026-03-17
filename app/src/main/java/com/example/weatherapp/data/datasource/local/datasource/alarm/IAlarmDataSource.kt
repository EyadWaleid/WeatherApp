package com.example.weatherapp.data.datasource.local.datasource.alarm

import com.example.weatherapp.data.model.entity.UserAlerts
import kotlinx.coroutines.flow.Flow

interface IAlarmDataSource {
    suspend fun insertAlarm(userAlerts: UserAlerts): Long
    fun getAllAlarms(): Flow<List<UserAlerts>>
    suspend fun deleteAlarm(userAlerts: UserAlerts)
    suspend fun updateAlarm(userAlerts: UserAlerts)
    suspend fun getAlarmById(alarmId: Long): UserAlerts?

}