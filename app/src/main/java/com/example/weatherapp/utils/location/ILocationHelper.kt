package com.example.weatherapp.utils.location

import android.location.Location

interface ILocationHelper {
    fun checkPermissions(): Boolean

    fun isLocationEnabled(): Boolean

    suspend fun getUserLocation(): Location?
}