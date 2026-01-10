package com.example.car_project_1.logic

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.WindowManager
import com.example.car_project_1.callback.TiltCallback
import kotlin.math.abs

class TiltDetector(
    private val context: Context,
    private val tiltCallback: TiltCallback
) {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val windowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastMoveTime = 0L

    private val MOVE_THRESHOLD = 3.0f
    private val DEAD_ZONE = 1.5f
    private val MOVE_COOLDOWN_MS = 250L

    private val sensorListener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {

            val horizontal = getHorizontalTilt(event)
            val forward = getForwardTilt(event)

            handleHorizontalMove(horizontal)
            handleSpeed(forward)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    // ---------------- MOVE LEFT / RIGHT ----------------

    private fun handleHorizontalMove(value: Float) {
        if (abs(value) < DEAD_ZONE) return

        val now = System.currentTimeMillis()
        if (now - lastMoveTime < MOVE_COOLDOWN_MS) return
        lastMoveTime = now

        when {
            value > MOVE_THRESHOLD -> tiltCallback.onMove(1)
            value < -MOVE_THRESHOLD -> tiltCallback.onMove(-1)
        }
    }

    // ---------------- SPEED CONTROL ----------------

    private fun handleSpeed(value: Float) {
        val normalized = (value.coerceIn(-5f, 5f)) / 5f

        val speedFactor = 1f + normalized * 0.5f

        tiltCallback.onSpeedChange(speedFactor)
    }

    // ---------------- AXIS HELPERS ----------------

    private fun getHorizontalTilt(event: SensorEvent): Float {
        return when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> event.values[0]        // Portrait
            Surface.ROTATION_90 -> -event.values[1]      // Landscape left
            Surface.ROTATION_180 -> -event.values[0]     // Portrait upside down
            Surface.ROTATION_270 -> event.values[1]      // Landscape right
            else -> event.values[0]
        }
    }

    private fun getForwardTilt(event: SensorEvent): Float {
        return when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> -event.values[1]
            Surface.ROTATION_90 -> -event.values[0]
            Surface.ROTATION_180 -> event.values[1]
            Surface.ROTATION_270 -> event.values[0]
            else -> -event.values[1]
        }
    }

    // ---------------- LIFECYCLE ----------------

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(
                sensorListener,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(sensorListener)
    }
}
