package util

data class Position(val row: Int, val col: Int) {

    fun move(direction: Direction, steps: Int = 1): Position {
        return when (direction) {
            Direction.LEFT -> copy(col = col - steps)
            Direction.RIGHT -> copy(col = col + steps)
            Direction.DOWN -> copy(row = row + steps)
            Direction.UP -> copy(row = row - steps)
        }
    }

    fun getNeighbours(rowRange: IntRange? = null, colRange: IntRange? = null): Set<Position> {
        return setOf(move(Direction.LEFT), move(Direction.RIGHT), move(Direction.UP), move(Direction.DOWN))
            .filter { rowRange?.contains(it.row) ?: true }
            .filter { colRange?.contains(it.col) ?: true }
            .toSet()
    }
}
