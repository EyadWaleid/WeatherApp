package com.example.weatherapp.utils.geoCoder

interface IGeocoder {
    suspend fun getAddressFromLocation(lat: Double, lon: Double): String
}