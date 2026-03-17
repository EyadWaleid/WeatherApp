package com.example.weatherapp.utils.geoCoder

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume

class GeocoderHelper(private val context: Context) : IGeocoder {
    override suspend fun getAddressFromLocation(lat: Double, lon: Double): String {
        return try {
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    geoCoder.getFromLocation(lat, lon, 1) { addresses ->
                        continuation.resume(addresses)
                    }
                }
            } else {
                geoCoder.getFromLocation(lat, lon, 1) ?: emptyList()
            }
            if (addresses.isNotEmpty()) formatAddress(addresses[0])
            else "Unknown Location"
        } catch (e: IOException) {
            "Unknown Location"
        }
    }

    private fun formatAddress(address: Address): String {
        val city = address.locality
        val country = address.countryName
        return when {
            city != null && country != null -> "$city, $country"
            city != null -> city
            country != null -> country
            else -> "Unknown Location"
        }
    }
}