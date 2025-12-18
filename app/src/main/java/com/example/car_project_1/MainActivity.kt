package com.example.car_project_1

import GameManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.car_project_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val ROWS = 6
    private val COLS = 3
    private var mistakes = 0
    private val maxMistakes = 3
    private var idxVisibleCar: Int = 1
    private var isLoopRunning = false
    private lateinit var car_map: Array<ImageView>
    private lateinit var rock_map: Array<Array<ImageView>>
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var gameLoop: Runnable
    val game = GameManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startGame();
    }

    override fun onPause() {
        super.onPause()
        Log.d("GameDebug", "onPause → stopping loop")
        handler.removeCallbacks(gameLoop)
        isLoopRunning = false
    }

    override fun onResume() {
        super.onResume()
        Log.d("GameDebug", "onResume → resuming loop")

        if (!isLoopRunning && mistakes < maxMistakes) {
            handler.postDelayed(gameLoop, 1000)
            isLoopRunning = true
        }
    }


    private fun startGame() {
        initBorad();
        binding.leftBtn.setOnClickListener { moveCar(-1) }
        binding.rightBtn.setOnClickListener { moveCar(1) }
        car_map[0].visibility = View.INVISIBLE
        car_map[2].visibility = View.INVISIBLE
        game.shiftBoardDownAndSpawnNewRow()
        updateUI()
        startGameLoop()
    }

    private fun initBorad() {
        rock_map = arrayOf(
            arrayOf(binding.imgRock00, binding.imgRock01, binding.imgRock02),
            arrayOf(binding.imgRock10, binding.imgRock11, binding.imgRock12),
            arrayOf(binding.imgRock20, binding.imgRock21, binding.imgRock22),
            arrayOf(binding.imgRock30, binding.imgRock31, binding.imgRock32),
            arrayOf(binding.imgRock40, binding.imgRock41, binding.imgRock42),
            arrayOf(binding.imgRock50, binding.imgRock51, binding.imgRock52),
        )

        car_map = arrayOf(binding.imgCar00, binding.imgCar01, binding.imgCar02)
    }

    private fun moveCar(dir: Int) {
        if (idxVisibleCar + dir >= 0 && idxVisibleCar + dir <= 2) {
            idxVisibleCar += dir

            for (i in 0..2) {
                car_map[i].visibility = View.INVISIBLE
            }
            car_map[idxVisibleCar].visibility = View.VISIBLE
        }
    }

    private fun updateUI() {
        val map = game.getLogicalMap()
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                val img = rock_map[r][c]
                img.visibility =
                    if (map[r][c] == 1) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    private fun startGameLoop() {
        gameLoop = object : Runnable {
            override fun run() {
                if (game.hasCollision(idxVisibleCar)) {
                    mistakes++
                    updateHearts();
                    vibrateOnCrash();
                    if (mistakes >= maxMistakes) {
                        return
                    }
                    showUserMsg("Crash! Life lost.");
                }

                game.shiftBoardDownAndSpawnNewRow()
                updateUI()
                handler.postDelayed(this, 1000)
            }
        }
    }

    fun updateHearts() {
        when (mistakes) {
            1 -> binding.heart3.visibility = View.INVISIBLE
            2 -> binding.heart2.visibility = View.INVISIBLE
            3 -> {
                binding.heart1.visibility = View.INVISIBLE
                showUserMsg("No more lives");
                onGameOver()
            }
        }
    }

    fun showUserMsg(txt: String) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
    }

    private fun onGameOver() {
        handler.removeCallbacks(gameLoop)
        binding.rightBtn.isEnabled = false
        binding.leftBtn.isEnabled = false
        isLoopRunning = false
    }


    private fun vibrateOnCrash(durationMs: Long = 120L) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    durationMs,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        durationMs,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        }
    }

}