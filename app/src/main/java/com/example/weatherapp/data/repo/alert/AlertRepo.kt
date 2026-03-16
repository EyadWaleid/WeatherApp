package com.example.weatherapp.data.repo.alert

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherapp.NotificationWorkManager
import com.example.weatherapp.WorkerKeys
import com.example.weatherapp.data.datasource.local.datasource.AlarmDataSource
import com.example.weatherapp.data.model.entity.UserAlerts
import com.example.weatherapp.utils.constants.TimeUtils
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class AlertRepo(private val context: Application) {
    private val alarmDataSource = AlarmDataSource(context)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertAlarm(userAlerts: UserAlerts) {
        val  alertid = alarmDataSource.insertAlarm(userAlerts)
        scheduleAlarm(userAlerts.copy(
             id = alertid
        )
        )
    }

    suspend fun getAllAlarms(): Flow<List<UserAlerts>> = alarmDataSource.getAllAlarms()
    suspend fun deleteAlarm(userAlerts: UserAlerts) {
        alarmDataSource.deleteAlarm(userAlerts)
        cancelAlarm(userAlerts.id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleAlarm(alarm: UserAlerts) {

        val delay = TimeUtils.calculateDelay(alarm.from)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val inputData = Data.Builder()
            .putInt(WorkerKeys.ALARM_ID.key, alarm.id.toInt())
            .putString(WorkerKeys.ALARM_TYPE.key, alarm.type)
            .putInt(WorkerKeys.NOTIFICATION_ID.key, alarm.id.toInt())
            .putString(WorkerKeys.ALERT_NAME.key, alarm.name)
            .putString(WorkerKeys.TO.key, alarm.to)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorkManager>(15, TimeUnit.MINUTES)
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setInputData(inputData)
            .addTag(alarm.id.toString())
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            alarm.id.toString(),
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )    }



    suspend fun turnOffAlarm(alarmId: Long) {
        val alarm = alarmDataSource.getAlarmById(alarmId)
        alarm?.let { it ->
            alarmDataSource.updateAlarm(it.copy(isOn = false))
        }
        cancelAlarm(alarmId)
    }
   private fun cancelAlarm(alarmId: Long) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(alarmId.toString())
    }


}