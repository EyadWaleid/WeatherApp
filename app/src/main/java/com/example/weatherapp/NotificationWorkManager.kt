package com.example.weatherapp

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.weatherapp.data.model.entity.CountryForecast
import com.example.weatherapp.data.repo.SettingsRepo
import com.example.weatherapp.data.repo.WeatherHomeRepo
import com.example.weatherapp.utils.location.LocationHelper
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.WeatherMapper.getUnits
import kotlinx.coroutines.flow.first
import java.time.LocalTime
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)

class NotificationWorkManager(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val alarmId = inputData.getInt(WorkerKeys.ALARM_ID.key, -1)
        val notificationId = inputData.getInt(WorkerKeys.NOTIFICATION_ID.key, -1)
        val  alarmType=inputData.getString(WorkerKeys.ALARM_TYPE.key)
        val to = inputData.getString(WorkerKeys.TO.key)
        if (alarmId == -1 || notificationId == -1) return Result.failure()
        val settingsRepo = SettingsRepo(applicationContext as Application)
        val weatherRepo = WeatherHomeRepo(context = applicationContext as Application)

        return try {
            val (lon, lat) = getLocation(settingsRepo)
            val units = settingsRepo.getTempUnit().first()
            val lang = settingsRepo.getLanguage().first()
            val result = weatherRepo.loadCountryWeatherData(
                lon = lon,
                lat = lat,
                units = getUnits(units),
                lang = lang
            )
            result.fold(
                onSuccess = { forecast ->
                    showNotification(
                        forecast, notificationId,
                        alarmType = alarmType
                    )
                    if (isExceededToTime(to)) {
                        WorkManager.getInstance(applicationContext)
                            .cancelUniqueWork(alarmId.toString())

                    }
                    Result.success()
                },
                onFailure = {

                    Result.retry()
                }
            )
        } catch (e: Exception) {
            Result.retry()
        }
    }
    // check if  it will exceed time
    private fun isExceededToTime(to: String?): Boolean {
        if (to == null) return false

        val now = LocalTime.now()
        val toTime = LocalTime.parse(to)

        if (now.isAfter(toTime)) return true

        val nowSeconds = now.toSecondOfDay()
        val toSeconds = toTime.toSecondOfDay()
        val remainingSeconds = toSeconds - nowSeconds
        val fifteenMinutesSeconds = 15 * 60

        val cycles = remainingSeconds / fifteenMinutesSeconds
        return cycles <= 1
    }
   // show notification
    private fun showNotification(forecast: CountryForecast, notificationId: Int, alarmType: String?) {
        val today = forecast.weatherOfDays.first()
        val intent = Intent(applicationContext, NotificationService::class.java).apply {
            putExtra(WorkerKeys.ALERT_NAME.key, forecast.city)
            putExtra(WorkerKeys.TEMP_NAME.key,today.tempDescription)
            putExtra(WorkerKeys.ALARM_TYPE.key,alarmType )
            putExtra(WorkerKeys.TEMP.key, today.temp)
            putExtra(WorkerKeys.NOTIFICATION_ID.key, notificationId)
        }
        applicationContext.startForegroundService(intent)
    }
  // fetch Location
    private suspend fun getLocation(settingsRepo: SettingsRepo): Pair<Double, Double> {
        val source = settingsRepo.getLocationSource().first()
        return if (source == LocationSource.MAP.displayName) {
            settingsRepo.getMapLocation().first()
        } else {
            if (hasBackgroundLocationPermission()) {
                val locationHelper = LocationHelper(applicationContext as Application)
                val location = locationHelper.getUserLocation()
                if (location != null) {
                    Pair(location.longitude, location.latitude)
                } else {
                    getCachedLocation()
                }
            } else {
                getCachedLocation()
            }
        }
    }
  // get cachedLocation
    private suspend fun getCachedLocation(): Pair<Double, Double> {
        val weatherRepo = WeatherHomeRepo(

            context = applicationContext as Application,

            )
        val result = weatherRepo.getSavedWeatherForecast()
        return result.fold(
            onSuccess = { forecast ->
                Pair(forecast.long, forecast.lat)
            },
            onFailure = {
                Pair(0.0, 0.0)
            }
        )
    }
  // check permissions
    private fun hasBackgroundLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}

enum class WorkerKeys(val key: String) {
    ALARM_ID("ALARM_ID"),
    ALARM_TYPE("ALARM_TYPE"),
    NOTIFICATION_ID("NOTIFICATION_ID"),
    ALERT_NAME("ALERT_NAME"),
    TEMP_NAME("TEMP_NAME"),

    FROM("from"),
    TO("to"),
    TEMP("TEMP")
}