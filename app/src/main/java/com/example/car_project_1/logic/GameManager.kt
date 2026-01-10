package com.example.car_project_1.logic

class GameManager {

    private val ROWS = 6
    private val COLS = 5

    // Cell types
    companion object {
        const val EMPTY = 0
        const val ROCK = 1
        const val COIN = 2
    }

    private var isEmptyTurn = false

    private var logicalRockMap: Array<Array<Int>> =
        Array(ROWS) { Array(COLS) { EMPTY } }

    fun getLogicalMap(): Array<Array<Int>> = logicalRockMap

    fun shiftBoardDownAndSpawnNewRow() {
        for (row in ROWS - 1 downTo 1) {
            logicalRockMap[row] = logicalRockMap[row - 1].copyOf()
        }

        logicalRockMap[0] =
            if (isEmptyTurn) {
                Array(COLS) { EMPTY }
            } else {
                generateRandomRow()
            }

        isEmptyTurn = !isEmptyTurn
    }

    // ---------- ROW GENERATION ----------

    private fun generateRandomRow(): Array<Int> {
        val row = Array(COLS) { EMPTY }

        // 1–3 rocks
        val rockCount = (1..3).random()
        val availablePositions = (0 until COLS).shuffled().toMutableList()

        repeat(rockCount) {
            val col = availablePositions.removeAt(0)
            row[col] = ROCK
        }

        // 30% chance to spawn a coin
        if ((1..100).random() <= 30 && availablePositions.isNotEmpty()) {
            val coinCol = availablePositions.random()
            row[coinCol] = COIN
        }

        return row
    }

    // ---------- COLLISION CHECKS ----------

    fun hasRockCollision(carCol: Int): Boolean {
        return logicalRockMap[ROWS - 1][carCol] == ROCK
    }

    fun hasCoinCollision(carCol: Int): Boolean {
        return logicalRockMap[ROWS - 1][carCol] == COIN
    }

    fun collectCoin(carCol: Int) {
        logicalRockMap[ROWS - 1][carCol] = EMPTY
    }
}