package aoc2022

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day24: Day("24") {

    data class Tornados(
        val left: Map<Int, MutableList<Int>>,
        val right: Map<Int, MutableList<Int>>,
        val up: Map<Int, MutableList<Int>>,
        val down: Map<Int, MutableList<Int>>,
        val width: Int,
        val height: Int
    )

    private fun getInput(name: String): Tornados {
        val lines = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .drop(1)
            .dropLast(1)
            .map { it.drop(1).dropLast(1) }
        val rights = (lines.indices).associateWith { mutableListOf<Int>() }
        val lefts = (lines.indices).associateWith { mutableListOf<Int>() }
        val downs = lines[0].indices.associateWith { mutableListOf<Int>() }
        val ups = lines[0].indices.associateWith { mutableListOf<Int>() }
        for (row in lines.indices) {
            for (col in lines[0].indices) {
                if (lines[row][col] == 'v') {
                    downs[col]!!.add(row)
                } else if (lines[row][col] == '^') {
                    ups[col]!!.add(row)
                } else if (lines[row][col] == '<') {
                    lefts[row]!!.add(col)
                } else if (lines[row][col] == '>') {
                    rights[row]!!.add(col)
                }
            }
        }
        return Tornados(lefts, rights, ups, downs, lines[0].length, lines.size)
    }

    data class State(val row: Int = -1, val col: Int = 0, val steps: Long = 0)

    private fun findPath(tornados: Tornados, start: State, endRow: Int, endCol: Int): Long {
        val (left, right, up, down, width, height) = tornados
        val queue = LinkedList<State>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            var edgeCaseStart = false
            val (row, col, steps) = queue.pop()
            if (row == endRow && col == endCol) {
                return steps
            }
            var possibleNextPosition = setOf(row-1 to col, row to col-1, row to col, row to col+1, row+1 to col)
            if (possibleNextPosition.contains(start.row to start.col)) {
                edgeCaseStart = true
            }
            if (possibleNextPosition.contains(endRow to endCol)) {
                return steps
            }
            possibleNextPosition = possibleNextPosition
                .filter { (it.first >= 0 && it.second >= 0 && it.second < width && it.first < height) }
                .toSet()

            val lefts = left
                .filterKeys { possibleNextPosition.any { p -> p.first == it } }
                .mapValues { it.value.map { v -> (v-steps).mod(width) } }
            val rights = right
                .filterKeys { possibleNextPosition.any { p -> p.first == it } }
                .mapValues { it.value.map { v -> (v+steps).mod(width) } }
            val ups = up
                .filterKeys { possibleNextPosition.any { p -> p.second == it } }
                .mapValues { it.value.map { v -> (v-steps).mod(height) } }
            val downs = down
                .filterKeys { possibleNextPosition.any { p -> p.second == it } }
                .mapValues { it.value.map { v -> (v+steps).mod(height) } }
            val validNextPositions = possibleNextPosition
                .filter { (row,col) -> !lefts[row]!!.contains(col) }
                .filter { (row,col) -> !rights[row]!!.contains(col) }
                .filter { (row,col) -> !ups[col]!!.contains(row) }
                .filter { (row,col) -> !downs[col]!!.contains(row) }
            if (edgeCaseStart) {
                queue.add(State(start.row, start.col, steps+1))
            }
            validNextPositions
                .map { (row, col) -> State(row, col, steps + 1) }
                .filter { !queue.contains(it) }
                .forEach { queue.add(it) }
        }
        return 0
    }

    override fun executePart1(name: String): Long {
        val tornados = getInput(name)
        return findPath(getInput(name), State(), tornados.height-1, tornados.width-1)+1
    }

    override fun executePart2(name: String): Long {
        val tornados = getInput(name)
        val startRow = -1
        val startCol = 0
        val endRow = tornados.height
        val endCol = tornados.width-1
        val firstTrip = findPath(tornados, State(), endRow, endCol)
        val secondTrip = findPath(tornados, State(endRow, endCol, firstTrip), startRow, startCol)
        return findPath(tornados, State(startRow, startCol, secondTrip), endRow, endCol)
    }
}

