package com.example.car_project_1.ui

import com.example.car_project_1.logic.GameManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.car_project_1.utilities.LocationHelper
import com.example.car_project_1.R
import com.example.car_project_1.data.TopTenStorage
import com.example.car_project_1.utilities.VibrationHelper
import com.example.car_project_1.databinding.ActivityGameBinding
import com.example.car_project_1.logic.TiltDetector
import com.example.car_project_1.utilities.SoundEffectPlayer
import com.example.car_project_1.callback.TiltCallback
import com.example.car_project_1.utilities.BackgroundMusicPlayer


class GameActivity : AppCompatActivity() {

    companion object {
        const val GAME_TYPE_KEY = "GAME_TYPE_KEY"
        const val GAME_TYPE_BUTTONS = 0
        const val GAME_TYPE_SENSORS = 1

        private const val NORMAL_DELAY = 500L
        private const val FAST_DELAY = 300L
        private const val MIN_DELAY = 150L
        private const val MAX_DELAY = 700L
    }

    private lateinit var binding: ActivityGameBinding
    private val game = GameManager()
    private lateinit var tiltDetector: TiltDetector

    private val ROWS = 6
    private val COLS = 5

    private var idxVisibleCar = 2
    private var mistakes = 0
    private val maxMistakes = 3
    private var score = 0
    private var km = 0.0
    private var playerName: String = ""

    private lateinit var carMap: Array<ImageView>
    private lateinit var rockMap: Array<Array<ImageView>>

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var gameLoop: Runnable
    private var isGameRunning = false

    private var inputMode = GAME_TYPE_BUTTONS
    private var isFastMode = false
    private var currentDelay = NORMAL_DELAY

    // 🔹 Helpers
    private lateinit var locationHelper: LocationHelper
    private lateinit var vibrationHelper: VibrationHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputMode = intent.getIntExtra(GAME_TYPE_KEY, GAME_TYPE_BUTTONS)
        isFastMode = intent.getBooleanExtra("EXTRA_FAST_MODE", false)
        currentDelay = if (isFastMode) FAST_DELAY else NORMAL_DELAY

        // init helpers
        locationHelper = LocationHelper(this)
        SoundEffectPlayer.init(this) //
        SoundEffectPlayer.load(this, R.raw.car_crash) //
        SoundEffectPlayer.load(this, R.raw.coin_collect) //
        vibrationHelper = VibrationHelper(this)

        initTiltDetector()
        initBoard()
        setupInput()
        initGameLoop()

    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(this, object : TiltCallback {

            override fun onMove(direction: Int) {
                moveCar(direction)
            }

            override fun onSpeedChange(factor: Float) {
                currentDelay = (NORMAL_DELAY / factor)
                    .toLong()
                    .coerceIn(MIN_DELAY, MAX_DELAY)
            }
        })
    }


    // ---------------- INPUT ----------------



    private fun setupInput() {
        val isRTL = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

        if (inputMode == GAME_TYPE_BUTTONS) {

            binding.leftBtn.setOnClickListener {
                moveCar(if (isRTL) 1 else -1)
            }
            binding.rightBtn.setOnClickListener {
                moveCar(if (isRTL) -1 else 1)
            }
        } else {
            initTiltDetector()
            binding.leftBtn.visibility = View.INVISIBLE
            binding.rightBtn.visibility = View.INVISIBLE
        }
    }

    // ---------------- GAME LOOP ----------------

    private fun initGameLoop() {
        gameLoop = object : Runnable {
            override fun run() {

                if (game.hasRockCollision(idxVisibleCar)) {
                    mistakes++
                    updateHearts()
                    vibrationHelper.vibrate()
                    SoundEffectPlayer.playExclusive(R.raw.car_crash)
                    if (mistakes >= maxMistakes) {
                        stopGame()
                        promptForPlayerName()
                        return
                    }
                    showUserMsg("Crash! Life lost.");
                }

                if (game.hasCoinCollision(idxVisibleCar)) {
                    score++
                    binding.lblScore.text = "$score COINS"
                    SoundEffectPlayer.playExclusive(R.raw.coin_collect)
                    game.collectCoin(idxVisibleCar)
                }

                game.shiftBoardDownAndSpawnNewRow()
                km += 0.1
                updateKmUI()
                updateUI()

                handler.postDelayed(this, currentDelay)
            }
        }
    }

    private fun startTicker() {
        if (!isGameRunning) {
            handler.post(gameLoop)
            isGameRunning = true
        }
    }


    private fun promptForPlayerName() {
        val editText = EditText(this)
        editText.hint = "Enter your name"
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("Enter your name")
            .setView(editText)
            .setCancelable(false)
            .setPositiveButton("Save") { _, _ ->
                playerName = editText.text.toString().trim()
                addPlayerToTopTen()
                showGameOverDialog()
            }
            .show()
    return
    }
    private fun stopGame() {
        handler.removeCallbacks(gameLoop)
        isGameRunning = false
        BackgroundMusicPlayer.getInstance().pauseMusic()
        return
    }

    // ---------------- LOCATION & SAVE ----------------

    private fun addPlayerToTopTen() {
        locationHelper.getSingleLocation { lat, lon ->
            saveScore(lat, lon, playerName)
        }
    }

    private fun saveScore(lat: Double, lon: Double, playerName: String) {
        TopTenStorage.addScore(this, km, lat, lon, playerName)
    }

    // ---------------- UI ----------------


    private fun initBoard() {
        rockMap = arrayOf(
            arrayOf(
                binding.imgRock00,
                binding.imgRock01,
                binding.imgRock02,
                binding.imgRock03,
                binding.imgRock04
            ),
            arrayOf(
                binding.imgRock10,
                binding.imgRock11,
                binding.imgRock12,
                binding.imgRock13,
                binding.imgRock14
            ),
            arrayOf(
                binding.imgRock20,
                binding.imgRock21,
                binding.imgRock22,
                binding.imgRock23,
                binding.imgRock24
            ),
            arrayOf(
                binding.imgRock30,
                binding.imgRock31,
                binding.imgRock32,
                binding.imgRock33,
                binding.imgRock34
            ),
            arrayOf(
                binding.imgRock40,
                binding.imgRock41,
                binding.imgRock42,
                binding.imgRock43,
                binding.imgRock44
            ),
            arrayOf(
                binding.imgRock50,
                binding.imgRock51,
                binding.imgRock52,
                binding.imgRock53,
                binding.imgRock54
            )
        )

        carMap = arrayOf(
            binding.imgCar00, binding.imgCar01, binding.imgCar02,
            binding.imgCar03, binding.imgCar04
        )

        updateCarUI()
    }

    private fun updateUI() {
        val map = game.getLogicalMap()
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                val img = rockMap[r][c]
                when (map[r][c]) {
                    GameManager.ROCK -> {
                        img.setImageResource(R.drawable.img_rock)
                        img.visibility = View.VISIBLE
                    }

                    GameManager.COIN -> {
                        img.setImageResource(R.drawable.img_coin)
                        img.visibility = View.VISIBLE
                    }

                    else -> img.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun updateKmUI() {
        binding.lblKm.text = String.format("%.1f KM", km)
    }

    private fun updateCarUI() {
        for (i in 0 until COLS) {
            carMap[i].visibility =
                if (i == idxVisibleCar) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun moveCar(dir: Int) {
        val newIndex = idxVisibleCar + dir
        if (newIndex in 0 until COLS) {
            idxVisibleCar = newIndex
            updateCarUI()
        }
    }

    private fun updateHearts() {
        when (mistakes) {
            1 -> binding.heart3.visibility = View.INVISIBLE
            2 -> binding.heart2.visibility = View.INVISIBLE
            3 -> {
                binding.heart1.visibility = View.INVISIBLE
                showUserMsg("No more lives");
            }
        }
    }



    private fun showGameOverDialog() {
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("Score: $score Coins\nDistance: ${String.format("%.1f", km)} KM")
            .setCancelable(false)
            .setPositiveButton("Play Again") { _, _ -> recreate() }
            .setNegativeButton("Main Menu") { _, _ -> finish() }
            .show()
    }



    fun showUserMsg(txt: String) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
        if (inputMode == GAME_TYPE_SENSORS) {
            tiltDetector.start()
        }
        BackgroundMusicPlayer.getInstance().playMusic()
        startTicker()

    }

    override fun onPause() {
        super.onPause()
        if (inputMode == GAME_TYPE_SENSORS) {
            tiltDetector.stop()
        }
        stopGame()
    }

}