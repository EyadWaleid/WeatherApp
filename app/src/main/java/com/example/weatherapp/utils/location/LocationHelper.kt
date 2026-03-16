package com.example.weatherapp.utils.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationHelper (val context: Context){
    private val fusedClient =
        LocationServices.getFusedLocationProviderClient(context)

   fun checkPermissions(): Boolean{
       val permission = ContextCompat.checkSelfPermission(
           context,
           Manifest.permission.ACCESS_FINE_LOCATION
       ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
           context, Manifest.permission.ACCESS_COARSE_LOCATION
       ) == PackageManager.PERMISSION_GRANTED
        return  permission
    }
    fun isLocationEnabled(): Boolean{
        val location: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return  location.isProviderEnabled(LocationManager.GPS_PROVIDER)|| location.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
    @SuppressLint("MissingPermission")
    suspend fun getUserLocation(): Location? =
        suspendCancellableCoroutine { cont ->
            fusedClient.lastLocation
                .addOnSuccessListener { lastLocation ->
                    if (lastLocation != null) {
                        cont.resume(lastLocation)
                        { cause, _, _ -> {} }
                    } else {
                        val cancellationToken =
                            CancellationTokenSource().token
                        fusedClient.getCurrentLocation(
                            Priority.PRIORITY_HIGH_ACCURACY,
                            cancellationToken
                        ).addOnSuccessListener { freshLocation ->
                            cont.resume(freshLocation)
                            { cause, _, _ -> {} }
                        }.addOnFailureListener {
                            cont.resume(null) {}
                        }
                    }

                }
                .addOnFailureListener {
                    cont.resume(null) {}
                }
        }
}