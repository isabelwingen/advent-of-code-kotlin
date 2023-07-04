package aoc2022

import getInputAsLines
import splitBy
import util.Day

const val RIGHT = 0
const val DOWN = 1
const val LEFT = 2
const val UP = 3

//    _ _
//   |1|4|
//    2
//  3|6
//  5

class State(var area: Int, var row: Int, var col: Int, var dir: Int) {
    override fun toString() = "$area, $row, $col, $dir"

    override fun equals(other: Any?): Boolean {
        return if (other is State) {
            this.area == other.area && this.row == other.row && this.col == other.col && this.dir == other.dir
        } else {
            false
        }
    }
}

val areaConnectionMap = mapOf<Int, Map<Int, (Int, Int) -> State>>(
    1 to mapOf(
        RIGHT to { r, c -> State(4, r, 0, RIGHT) }, //to 4
        LEFT to { r, c -> State(3, 49 - r, 0, RIGHT) }, //to 3 upside down
        DOWN to { r, c -> State(2, 0, c, DOWN) }, //to 2
        UP to { r, c -> State(5, c, 0, RIGHT) }, //to 5 sideways
    ),
    2 to mapOf(
        RIGHT to { r, c -> State(4, 49, r, UP) }, //to 4 upwards
        LEFT to { r, c -> State(3, 0, r, DOWN) }, //to 3 downwards
        DOWN to { r, c -> State(6, 0, c, DOWN) }, //to 6 downwards
        UP to { r, c -> State(1, 49, c, UP) }, // to 1 upwards
    ),
    3 to mapOf(
        RIGHT to { r, c -> State(6, r, 0, RIGHT) }, //to 6
        LEFT to { r, c -> State(1, 49-r, 0, RIGHT) }, //to 1 upside down from left to right
        DOWN to { r, c -> State(5, 0, c, DOWN) }, //to 5
        UP to { r, c -> State(2, c, 0, RIGHT) }, // to 2 left to right
    ),
    4 to mapOf(
        RIGHT to { r, c -> State(6, 49 - r, 49, LEFT) }, //to 6 backwards
        LEFT to { r, c -> State(1, r, 49, LEFT) }, //to 1 from right
        DOWN to { r, c -> State(2, c, 49, LEFT) }, //to 2 from right
        UP to { r, c -> State(5, 49, c, UP) }, // to 5 upwards
    ),
    5 to mapOf(
        RIGHT to { r, c -> State(6, 49, r, UP) }, //to 6 upwards
        LEFT to { r, c -> State(1, 0, r, DOWN) }, // to 1, downwards
        DOWN to { r, c -> State(4, 0, c, DOWN) }, // to 4, top to botton
        UP to { r, c -> State(3, 49, c, UP) }, //to 3, bottom to top
    ),
    6 to mapOf(
        RIGHT to { r, c -> State(4, 49 - r, 49, LEFT) }, //to 4, upside down
        LEFT to { r, c -> State(3, r, 49, LEFT) }, // to 3
        DOWN to { r, c -> State(5, c, 49, LEFT) }, // to 5, right to left
        UP to { r, c -> State(2, 49, c, UP) }, //to 2, bottom to top
    )
)


class Day22 : Day("22") {

    class Line(val offset: Int, val lastIndex: Int) {
        override fun toString() = "offset: $offset, lastIndex: $lastIndex"

        override fun equals(other: Any?): Boolean {
            return if (other is Line) {
                this.offset == other.offset && this.lastIndex == other.lastIndex
            } else {
                false
            }
        }

        override fun hashCode(): Int {
            return listOf(offset, lastIndex).hashCode()
        }
    }

    class Game(val leftAndRight: OneDimension, val upAndDown: OneDimension, val operation: List<Any>)

    class OneDimension(val field: List<Line>, val obstacles: List<List<Int>>)

    class Part1 {

        private fun getOneDimension(fieldLines: List<String>): OneDimension {
            val field = fieldLines
                .map { it.indexOfFirst { c -> c != ' ' } to it.indexOfLast { c -> c != ' ' } }
                .map { Line(it.first, it.second - it.first) }
            val obstacles = fieldLines
                .map { line -> line.mapIndexed { index, c -> index to c }.filter { it.second == '#' }.map { it.first } }
                .mapIndexed { index, ints -> ints.map { it - field[index].offset } }
            return OneDimension(field, obstacles)
        }

        private fun transpose(matrix: List<String>): List<String> {
            val numRows = matrix.size
            val numCols = matrix.maxOf { it.length }

            val transposedMatrix = mutableListOf<MutableList<Char>>()
            for (col in 0 until numCols) {
                val transposedRow = mutableListOf<Char>()
                for (row in 0 until numRows) {
                    transposedRow.add(matrix[row].getOrElse(col) { ' ' })
                }
                transposedMatrix.add(transposedRow)
            }
            val p = transposedMatrix.map { it.joinToString("").trimEnd() }
            return p
        }

        fun parseInput(name: String): Game {
            val (fieldLines, _, operationLines, _) = getInputAsLines(name).splitBy { it.isBlank() }
            val operations = operationLines.first().toCharArray().toList()
                .splitBy { it == 'R' || it == 'L' }
                .map { if (it.first() == 'R' || it.first() == 'L') it.first() else it.joinToString("").toInt() }

            return Game(getOneDimension(fieldLines), getOneDimension(transpose(fieldLines)), operations)
        }

        fun goRight(oneDimension: OneDimension, steps: Int, col: Int, row: Int): Int {
            var newCol = col + steps
            val lastIndex = oneDimension.field[row].lastIndex
            if (newCol <= lastIndex) {
                if (oneDimension.obstacles[row].any { IntRange(col, newCol).contains(it) }) {
                    newCol = oneDimension.obstacles[row].first { IntRange(col, newCol).contains(it) } - 1
                }
            } else if (steps > lastIndex) {
                // we do more than one complete round or go to the next obstacle
                if (oneDimension.obstacles[row].isNotEmpty()) {
                    newCol = (oneDimension.obstacles[row].firstOrNull { it > col } ?: oneDimension.obstacles[row].first()) - 1
                } else {
                    newCol %= (lastIndex + 1)
                }
            } else {
                // we don't go a whole round but we are going over the edge
                newCol %= (lastIndex + 1)
                if (oneDimension.obstacles[row].any { IntRange(col, lastIndex).contains(it) }) {
                    newCol = oneDimension.obstacles[row].first { IntRange(col, lastIndex).contains(it) } - 1
                } else if (oneDimension.obstacles[row].any { IntRange(0, newCol).contains(it) }) {
                    newCol = oneDimension.obstacles[row].first { IntRange(0, newCol).contains(it) } - 1
                }
            }
            if (newCol == -1) {
                newCol = lastIndex
            }
            return oneDimension.field[row].offset + newCol
        }

        fun goLeft(oneDimension: OneDimension, steps: Int, col: Int, row: Int): Int {
            var newCol = col - steps
            val lastIndex = oneDimension.field[row].lastIndex
            if (newCol >= 0) {
                if (oneDimension.obstacles[row].any { IntRange(newCol, col).contains(it) }) {
                    newCol = oneDimension.obstacles[row].last { IntRange(newCol, col).contains(it) } + 1
                }
            } else if (steps > lastIndex) {
                // we do more than one complete round
                if (oneDimension.obstacles[row].isNotEmpty()) {
                    newCol = (oneDimension.obstacles[row].lastOrNull { it < col } ?: oneDimension.obstacles[row].last()) + 1
                } else {
                    newCol %= (lastIndex + 1)
                    newCol += lastIndex + 1
                }
            } else {
                // we don't go a whole round but we are going over the edge
                newCol %= (lastIndex + 1)
                newCol += lastIndex + 1
                if (oneDimension.obstacles[row].any { IntRange(0, col).contains(it) }) {
                    newCol = oneDimension.obstacles[row].last { IntRange(0, col).contains(it) } + 1
                } else if (oneDimension.obstacles[row].any { IntRange(newCol, lastIndex).contains(it) }) {
                    newCol = oneDimension.obstacles[row].last { IntRange(newCol, lastIndex).contains(it) } + 1
                }
            }
            if (newCol == lastIndex + 1) {
                newCol = 0
            }
            return oneDimension.field[row].offset + newCol
        }

        fun solve(name: String): Int {
            val game = parseInput(name)
            var absoluteRow = 0
            var absoluteCol = 8
            var dir = RIGHT

            val operations = game.operation
            for (op in operations) {
                when (op) {
                    'R' -> {
                        when (dir) {
                            RIGHT -> dir = DOWN
                            DOWN -> dir = LEFT
                            LEFT -> dir = UP
                            UP -> dir = RIGHT
                        }
                    }
                    'L' -> {
                        when (dir) {
                            RIGHT -> dir = UP
                            UP -> dir = LEFT
                            LEFT -> dir = DOWN
                            DOWN -> dir = RIGHT
                        }
                    }
                    else -> if (op is Int) {
                        when (dir) {
                            RIGHT -> {
                                absoluteCol = goRight(
                                    game.leftAndRight,
                                    op.toInt(),
                                    absoluteCol - game.leftAndRight.field[absoluteRow].offset,
                                    absoluteRow
                                )
                            }
                            LEFT -> {
                                absoluteCol = goLeft(
                                    game.leftAndRight,
                                    op.toInt(),
                                    absoluteCol - game.leftAndRight.field[absoluteRow].offset,
                                    absoluteRow
                                )
                            }
                            DOWN -> {
                                absoluteRow =
                                    goRight(game.upAndDown, op.toInt(), absoluteRow - game.upAndDown.field[absoluteCol].offset, absoluteCol)
                            }
                            UP -> {
                                absoluteRow =
                                    goLeft(game.upAndDown, op.toInt(), absoluteRow - game.upAndDown.field[absoluteCol].offset, absoluteCol)
                            }
                        }
                    } else {
                        throw IllegalStateException("Not allowed: $op")
                    }
                }
            }
            return (absoluteRow + 1) * 1000 + (absoluteCol + 1) * 4 + dir
        }

    }

    override fun executePart1(name: String): Long {
        return Part1().solve(name).toLong()
    }


    fun getAreas(name: String): Map<Int, List<List<Char>>> {
        val lines = getInputAsLines(name)
        val p1 = lines.take(50).map { it.dropWhile { c -> c == ' ' } }.map { it.take(50).toCharArray().toList() }
        val p4 = lines.take(50).map { it.takeLast(50).toCharArray().toList() }
        val p2 = lines.drop(50).take(50).map { it.takeLast(50).toCharArray().toList() }
        val p3 = lines.drop(100).take(50).map { it.take(50).toCharArray().toList() }
        val p6 = lines.drop(100).take(50).map { it.takeLast(50).toCharArray().toList() }
        val p5 = lines.drop(150).take(50).map { it.take(50).toCharArray().toList() }
        return mapOf(
            1 to p1,
            4 to p4,
            2 to p2,
            3 to p3,
            6 to p6,
            5 to p5)
    }
    
    private fun getOperations(name: String): List<Any> {
        return getInputAsLines(name)
            .drop(201)
            .first()
            .toCharArray().toList()
            .splitBy { it == 'R' || it == 'L' }
            .map { if (it.first() == 'R' || it.first() == 'L') it.first() else it.joinToString("").toInt() }
    }
    
    fun getObstaclesForArea(area: List<List<Char>>): Map<String, List<List<Int>>> {
        val obstacles = mapOf<String, MutableList<MutableList<Int>>>("lr" to MutableList(50) { mutableListOf() }, "ud" to MutableList(50) { mutableListOf() })
        for (row in 0 until 50) {
            for (col in 0 until 50) {
                if (area[row][col] == '#') {
                    obstacles["lr"]!![row].add(col)
                    obstacles["ud"]!![col].add(row)
                }
            }
        }
        return obstacles.map { it.key to it.value.map { l -> l.toList() } }.toMap()
    }

    private fun goRight(state: State, steps: Int, obstacles: Map<Int, Map<String, List<List<Int>>>>): State {
        var newCol = state.col+steps
        if (newCol < 50) {
            if (obstacles[state.area]!!["lr"]!![state.row].any { IntRange(state.col, newCol).contains(it) }) {
                newCol = obstacles[state.area]!!["lr"]!![state.row].first { IntRange(state.col, newCol).contains(it) } - 1
            }
            return State(state.area, state.row, newCol, RIGHT)
        } else if (obstacles[state.area]!!["lr"]!![state.row].any { IntRange(state.col, 49).contains(it) }) {
            newCol = obstacles[state.area]!!["lr"]!![state.row].first { IntRange(state.col, 49).contains(it) } - 1
            return State(state.area, state.row, newCol, RIGHT)
        } else {
            //transfer to new area
            val transfer = areaConnectionMap[state.area]!![RIGHT]!!(state.row, 49)
            return if (obstacles[transfer.area]!!["lr"]!![transfer.row].contains(transfer.col)) {
                State(state.area, state.row, 49, RIGHT)
            } else {
                val stepsToNewStart = 50-state.col
                val remainingSteps = steps-stepsToNewStart
                if (remainingSteps == 0) {
                    transfer
                } else {
                    nextSteps(remainingSteps, transfer, obstacles)
                }
            }
        }
    }

    private fun goDown(state: State, steps: Int, obstacles: Map<Int, Map<String, List<List<Int>>>>): State {
        var newRow = state.row+steps
        if (newRow < 50) {
            if (obstacles[state.area]!!["ud"]!![state.col].any { IntRange(state.row, newRow).contains(it) }) {
                newRow = obstacles[state.area]!!["ud"]!![state.col].first { IntRange(state.row, newRow).contains(it) } - 1
            }
            return State(state.area, newRow, state.col, DOWN)
        } else if (obstacles[state.area]!!["ud"]!![state.col].any { IntRange(state.row, 49).contains(it) }) {
            newRow = obstacles[state.area]!!["ud"]!![state.col].first { IntRange(state.row, 49).contains(it) } - 1
            return State(state.area, newRow, state.col, DOWN)
        } else {
            //transfer to new area
            val transfer = areaConnectionMap[state.area]!![DOWN]!!(49, state.col)
            return if (obstacles[transfer.area]!!["ud"]!![transfer.col].contains(transfer.row)) {
                State(state.area, 49, state.col, DOWN)
            } else {
                val stepsToNewStart = 50-state.row
                val remainingSteps = steps-stepsToNewStart
                if (remainingSteps < 0) {
                    transfer
                } else {
                    nextSteps(remainingSteps, transfer, obstacles)
                }
            }
        }
    }

    private fun goLeft(state: State, steps: Int, obstacles: Map<Int, Map<String, List<List<Int>>>>): State {
        var newCol = state.col-steps
        if (newCol >= 0) {
            if (obstacles[state.area]!!["lr"]!![state.row].any { IntRange(newCol, state.col).contains(it) }) {
                newCol = obstacles[state.area]!!["lr"]!![state.row].last { IntRange(newCol, state.col).contains(it) } + 1
            }
            return State(state.area, state.row, newCol, LEFT)
        } else if (obstacles[state.area]!!["lr"]!![state.row].any { IntRange(0, state.col).contains(it) }) {
            newCol = obstacles[state.area]!!["lr"]!![state.row].last { IntRange(0, state.col).contains(it) } + 1
            return State(state.area, state.row, newCol, LEFT)
        }else {
            //transfer to new area
            val transfer = areaConnectionMap[state.area]!![LEFT]!!(state.row, 0)
            return if (obstacles[transfer.area]!!["lr"]!![transfer.row].contains(transfer.col)) {
                State(state.area, state.row, 0, LEFT)
            } else {
                val stepsToNewStart = state.col + 1
                val remainingSteps = steps-stepsToNewStart
                if (remainingSteps < 0) {
                    transfer
                } else {
                    nextSteps(remainingSteps, transfer, obstacles)
                }
            }
        }
    }

    private fun goUp(state: State, steps: Int, obstacles: Map<Int, Map<String, List<List<Int>>>>): State {
        var newRow = state.row-steps
        if (newRow >= 0) {
            if (obstacles[state.area]!!["ud"]!![state.col].any { IntRange(newRow, state.row).contains(it) }) {
                newRow = obstacles[state.area]!!["ud"]!![state.col].last { IntRange(newRow, state.row).contains(it) } + 1
            }
            return State(state.area, newRow, state.col, UP)
        } else if (obstacles[state.area]!!["ud"]!![state.col].any { IntRange(0, state.row).contains(it) }) {
            newRow = obstacles[state.area]!!["ud"]!![state.col].last { IntRange(0, state.row).contains(it) } + 1
            return State(state.area, newRow, state.col, UP)
        } else {
            //transfer to new area
            val transfer = areaConnectionMap[state.area]!![UP]!!(0, state.col)
            return if (obstacles[transfer.area]!!["ud"]!![transfer.col].contains(transfer.row)) {
                State(state.area, 0, state.col, UP)
            } else {
                val stepsToNewStart = state.row + 1
                val remainingSteps = steps-stepsToNewStart
                if (remainingSteps == 0) {
                    transfer
                } else {
                    nextSteps(remainingSteps, transfer, obstacles)
                }
            }
        }
    }
    
     fun nextSteps(steps: Int, state: State, obstacles: Map<Int, Map<String, List<List<Int>>>>): State {
        return when (state.dir) {
            RIGHT -> {
                goRight(state, steps, obstacles)
            }
            LEFT -> {
                goLeft(state, steps, obstacles)
            }
            DOWN -> {
                goDown(state, steps, obstacles)
            }
            UP -> {
                goUp(state, steps, obstacles)
            }
            else -> {
                throw IllegalStateException("")
            }
        }
    }


    private fun stateLegal(state: State, obstacles: Map<Int, Map<String, List<List<Int>>>>) {
        if (obstacles[state.area]!!["lr"]!![state.row].contains(state.col)) {
            throw IllegalStateException("State $state is not possible")
        }
    }

    override fun executePart2(name: String): Long {
        val areas = getAreas(name)
        val operations = getOperations(name)
        val obstacles = areas.map { it.key to getObstaclesForArea(it.value) }.toMap()

        var state = State(1, 0, 0, RIGHT)

        for (op in operations) {
            stateLegal(state, obstacles)
            when (op) {
                'R' -> {
                    when (state.dir) {
                        RIGHT -> state.dir = DOWN
                        DOWN -> state.dir = LEFT
                        LEFT -> state.dir = UP
                        UP -> state.dir = RIGHT
                    }
                }
                'L' -> {
                    when (state.dir) {
                        RIGHT -> state.dir = UP
                        UP -> state.dir = LEFT
                        LEFT -> state.dir = DOWN
                        DOWN -> state.dir = RIGHT
                    }
                }
                else -> {
                    if (op is Int) {
                        state = nextSteps(op, state, obstacles)
                    }
                }
            }
        }
        val row = 100 + state.row + 1
        val col = 50 + state.col + 1
        return (1000 * row + 4 * col + state.dir).toLong()
    }
}