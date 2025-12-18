import kotlin.collections.copyOf
import kotlin.collections.forEach
import kotlin.collections.shuffled
import kotlin.collections.take
import kotlin.ranges.downTo
import kotlin.ranges.random
import kotlin.ranges.until

class GameManager {
    private val ROWS = 6
    private val COLS = 3
    private var isEmptyTurn = false
    private var logicalRockMap: Array<Array<Int>> =
        Array(ROWS) { Array(COLS) { 0 } }

    fun getLogicalMap(): Array<Array<Int>> = logicalRockMap
    fun shiftBoardDownAndSpawnNewRow() {
        for (row in ROWS - 1 downTo 1) {
            logicalRockMap[row] = logicalRockMap[row - 1].copyOf()
        }
        if (isEmptyTurn) {
            logicalRockMap[0] = Array(COLS) { 0 }
        } else {
            logicalRockMap[0] = generateRandomStonesRow()
        }
        isEmptyTurn = !isEmptyTurn
    }

    private fun generateRandomStonesRow(): Array<Int> {
        val row = Array(COLS) { 0 }
        val stoneCount = (1..2).random()
        val stonePositions = (0 until COLS).shuffled().take(stoneCount)
        stonePositions.forEach { col ->
            row[col] = 1
        }
        return row
    }

    fun hasCollision(carCol: Int): Boolean {
        return logicalRockMap[ROWS - 1][carCol] == 1
    }
}
