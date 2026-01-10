package com.example.car_project_1.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Data class for a single score record
data class ScoreRecord(
    val name: String,
    val km: Double,
    val lat: Double,
    val lon: Double
)

object TopTenStorage {

    private const val PREFS_NAME = "top_ten_prefs"
    private const val KEY_SCORES = "scores"

    fun addScore(context: Context, km: Double, lat: Double = 0.0, lon: Double = 0.0, playerName: String) {
        val scores = loadScores(context).toMutableList()

        val newScore = ScoreRecord(
            name = playerName,
            km = km,
            lat = lat,
            lon = lon
        )

        scores.add(newScore)

        // Keep only top 10 scores (highest km first)
        val topTen = scores
            .sortedByDescending { it.km }
            .take(10)

        saveScores(context, topTen)
    }

    // Load all saved scores
    fun loadScores(context: Context): List<ScoreRecord> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_SCORES, "[]")

        val type = object : TypeToken<List<ScoreRecord>>() {}.type
        return Gson().fromJson(json, type)
    }

    // Save scores list to SharedPreferences
    private fun saveScores(context: Context, scores: List<ScoreRecord>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_SCORES, Gson().toJson(scores))
            .commit()
    }


}