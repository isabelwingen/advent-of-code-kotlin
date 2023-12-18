package util

import aoc2018.Day23
import aoc2022.UP

data class Position(val row: Int, val col: Int) {

    fun move(direction: Direction): Position {
        return when (direction) {
            Direction.LEFT -> copy(col = col - 1)
            Direction.RIGHT -> copy(col = col + 1)
            Direction.DOWN -> copy(row = row + 1)
            Direction.UP -> copy(row = row - 1)
        }
    }

    fun getNeighbours(rowRange: IntRange?, colRange: IntRange?): Set<Position> {
        return setOf(move(Direction.LEFT), move(Direction.RIGHT), move(Direction.UP), move(Direction.DOWN))
            .filter { rowRange?.contains(it.row) ?: true }
            .filter { colRange?.contains(it.col) ?: true }
            .toSet()
    }
}
