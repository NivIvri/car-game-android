package com.example.car_project_1.utilities

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionHelper(
    private val activity: AppCompatActivity,
    private val permissionLauncher: ActivityResultLauncher<String>
) {

    fun checkLocationPermission(onGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onGranted()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}