package com.example.car_project_1.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.car_project_1.utilities.PermissionHelper
import com.example.car_project_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var selectedGameType = 0
    private var isFastMode = false

    private lateinit var permissionHelper: PermissionHelper

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startGameDirectly(selectedGameType)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionHelper =
            PermissionHelper(this, requestPermissionLauncher)

        setupListeners()
    }

    private fun setupListeners() {

        binding.btnSensor.setOnClickListener {
            checkPermissionAndStart(GameActivity.GAME_TYPE_SENSORS)
        }

        binding.btnButtons.setOnClickListener {
            checkPermissionAndStart(GameActivity.GAME_TYPE_BUTTONS)
        }

        binding.btnTopTen.setOnClickListener {
            startActivity(Intent(this, TopTenActivity::class.java))
        }

        binding.switchFastMode.setOnCheckedChangeListener { _, isChecked ->
            isFastMode = isChecked
        }
    }

    private fun checkPermissionAndStart(gameType: Int) {
        selectedGameType = gameType

        permissionHelper.checkLocationPermission {
            startGameDirectly(gameType)
        }
    }

    private fun startGameDirectly(gameType: Int) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(GameActivity.GAME_TYPE_KEY, gameType)
        intent.putExtra(EXTRA_FAST_MODE, isFastMode)
        startActivity(intent)
    }

    companion object {
        private const val EXTRA_FAST_MODE = "EXTRA_FAST_MODE"
    }
}