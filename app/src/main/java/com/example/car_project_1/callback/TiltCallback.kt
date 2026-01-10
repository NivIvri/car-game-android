package com.example.car_project_1.callback

interface TiltCallback {
    fun onMove(direction: Int)
    fun onSpeedChange(factor: Float)
}