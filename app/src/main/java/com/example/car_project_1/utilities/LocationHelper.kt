package com.example.car_project_1.utilities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationHelper(private val context: Context) {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    fun getSingleLocation(onResult: (lat: Double, lon: Double) -> Unit) {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onResult(0.0, 0.0)
            return
        }

        fusedClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onResult(location.latitude, location.longitude)
            } else {
                val request = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    1000
                ).setMaxUpdates(1).build()

                fusedClient.requestLocationUpdates(
                    request,
                    object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            val loc = result.lastLocation
                            if (loc != null) {
                                onResult(loc.latitude, loc.longitude)
                            } else {
                                onResult(0.0, 0.0)
                            }
                            fusedClient.removeLocationUpdates(this)
                        }
                    },
                    Looper.getMainLooper()
                )
            }
        }
    }
}