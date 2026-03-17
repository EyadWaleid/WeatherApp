package com.example.weatherapp.utils.geoCoder

// Step 1 — create interface
interface IGeocoder {
    suspend fun getAddressFromLocation(lat: Double, lon: Double): String
}