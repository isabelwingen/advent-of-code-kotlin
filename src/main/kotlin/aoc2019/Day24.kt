package aoc2019

import getInputAsLines
import util.Day
import kotlin.math.pow

class Day24: Day("24") {

    private fun getNeighboursPart1(row: Int, col: Int, board: MutableList<MutableList<Char>>): List<Char> {
        val neighbors = mutableListOf<Char>()
        if (row != 0) {
            neighbors.add(board[row-1][col])
        }
        if (row != (board.size-1)) {
            neighbors.add(board[row+1][col])
        }
        if (col != 0) {
            neighbors.add(board[row][col-1])
        }
        if (col != board[0].size-1) {
            neighbors.add(board[row][col+1])
        }
        return neighbors.toList()
    }

    override fun executePart1(name: String): Long {
        var board = getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.toCharArray().toMutableList() }
            .toMutableList()
        val seen = HashSet<String>()

        while (true) {
            val newBoard = emptyBoard()
            for (row in board.indices) {
                for (col in board[0].indices) {
                    val neighbors = getNeighboursPart1(row, col, board)
                    val cell = board[row][col]
                    if (cell == '#') {
                        if (neighbors.count { it == '#' } == 1) {
                            newBoard[row][col] = '#'
                        } else {
                            newBoard[row][col] = '.'
                        }
                    } else {
                        if (neighbors.count { it == '#' } == 1 || neighbors.count { it == '#' } == 2) {
                            newBoard[row][col] = '#'
                        } else {
                            newBoard[row][col] = '.'
                        }
                    }
                }
            }
            val boardAsString = newBoard.flatten().joinToString("")
            if (seen.contains(boardAsString)) {
                return newBoard.flatten().withIndex().sumOf { if (it.value == '#') 2.0.pow(it.index.toDouble()).toLong() else 0L }
            } else {
                seen.add(boardAsString)
            }
            board = newBoard
        }
    }

    private fun emptyBoard(): MutableList<MutableList<Char>> {
        return MutableList(5) { MutableList(5) { '.' } }
    }

    private fun getNeighbourCoordinatesPart2(row: Int, col: Int, level: Int): List<Triple<Int, Int, Int>> {
        when (row to col) {
            0 to 0 -> return listOf(
                Triple(2, 1, level - 1), //12
                Triple(1, 2, level - 1), //8
                Triple(0, 1, level),
                Triple(1, 0, level))
            0 to 1 -> return listOf(
                Triple(1, 2, level - 1), //8
                Triple(0, 0, level),
                Triple(0, 2, level),
                Triple(1, 1, level))
            0 to 2 -> return listOf(
                Triple(1, 2, level - 1), //8
                Triple(0, 1, level),
                Triple(0, 3, level),
                Triple(1, 2, level))
            0 to 3 -> return listOf(
                Triple(1, 2, level - 1), //8
                Triple(0, 2, level),
                Triple(0, 4, level),
                Triple(1, 3, level))
            0 to 4 -> return listOf(
                Triple(1, 2, level - 1), //8
                Triple(2, 3, level - 1), //14
                Triple(0, 3, level),
                Triple(1, 4, level))
            1 to 0 -> return listOf(
                Triple(2, 1, level - 1), //12
                Triple(0, 0, level),
                Triple(1, 1, level),
                Triple(2, 0, level),)
            1 to 1 -> return listOf(
                Triple(0, 1, level),
                Triple(1, 2, level),
                Triple(2, 1, level),
                Triple(1, 0, level))
            1 to 2 -> return listOf(
                Triple(0, 2, level),
                Triple(1, 3, level),
                Triple(1, 1, level),
                Triple(0, 0, level + 1),
                Triple(0, 1, level + 1),
                Triple(0, 2, level + 1),
                Triple(0, 3, level + 1),
                Triple(0, 4, level + 1))
            1 to 3 -> return listOf(
                Triple(0, 3, level),
                Triple(1, 4, level),
                Triple(2, 3, level),
                Triple(1, 2, level))
            1 to 4 -> return listOf(
                Triple(0, 4, level),
                Triple(1, 3, level),
                Triple(2, 4, level),
                Triple(2, 3, level - 1)) //14
            2 to 0 -> return listOf(
                Triple(2, 1, level - 1), //12
                Triple(1, 0, level),
                Triple(2, 1, level),
                Triple(3, 0, level))
            2 to 1 -> return listOf(
                Triple(0, 0, level + 1),
                Triple(1, 0, level + 1),
                Triple(2, 0, level + 1),
                Triple(3, 0, level + 1),
                Triple(4, 0, level + 1),
                Triple(1, 1, level),
                Triple(2, 0, level),
                Triple(3, 1, level))
            2 to 3 -> return listOf(
                Triple(0, 4, level + 1),
                Triple(1, 4, level + 1),
                Triple(2, 4, level + 1),
                Triple(3, 4, level + 1),
                Triple(4, 4, level + 1),
                Triple(1, 3, level),
                Triple(2, 4, level),
                Triple(3, 3, level))
            2 to 4 -> return listOf(
                Triple(2, 3, level - 1), //14
                Triple(1, 4, level),
                Triple(2, 3, level),
                Triple(3, 4, level))
            3 to 0 -> return listOf(
                Triple(2, 1, level - 1), //12
                Triple(2, 0, level),
                Triple(3, 1, level),
                Triple(4, 0, level))
            3 to 1 -> return listOf(
                Triple(2, 1, level),
                Triple(3, 2, level),
                Triple(4, 1, level),
                Triple(3, 0, level))
            3 to 2 -> return listOf(
                Triple(4, 0, level + 1),
                Triple(4, 1, level + 1),
                Triple(4, 2, level + 1),
                Triple(4, 3, level + 1),
                Triple(4, 4, level + 1),
                Triple(3, 1, level),
                Triple(3, 3, level),
                Triple(4, 2, level))
            3 to 3 -> return listOf(
                Triple(3, 2, level),
                Triple(3, 4, level),
                Triple(2, 3, level),
                Triple(4, 3, level))
            3 to 4 -> return listOf(
                Triple(2, 3, level - 1), //14
                Triple(3, 3, level),
                Triple(2, 4, level),
                Triple(4, 4, level))
            4 to 0 -> return listOf(
                Triple(3, 2, level - 1), //18
                Triple(2, 1, level - 1), //12
                Triple(4, 1, level),
                Triple(3, 0, level))
            4 to 1 -> return listOf(
                Triple(3, 2, level - 1), //18
                Triple(4, 0, level),
                Triple(4, 2, level),
                Triple(3, 1, level))
            4 to 2 -> return listOf(
                Triple(3, 2, level - 1), //18
                Triple(4, 1, level),
                Triple(4, 3, level),
                Triple(3, 2, level))
            4 to 3 -> return listOf(
                Triple(3, 2, level - 1), //18
                Triple(4, 2, level),
                Triple(4, 4, level),
                Triple(3, 3, level))
            4 to 4 -> return listOf(
                Triple(3, 2, level - 1), //18
                Triple(2, 3, level - 1), //14
                Triple(4, 3, level),
                Triple(3, 4, level))

            else -> throw IllegalStateException("Could not find neighbours for ${row to col}")
        }
    }

    private fun getNeighboursPart2(row: Int, col: Int, level: Int, boards: MutableMap<Int, MutableList<MutableList<Char>>>): Int {
        return getNeighbourCoordinatesPart2(row, col, level)
            .map { boards.getOrDefault(it.third, emptyBoard())[it.first][it.second] }
            .count { it == '#'}
    }

    private fun emptyBoard(board: MutableList<MutableList<Char>>): Boolean {
        return board.flatten().all { it == '.' }
    }

    override fun executePart2(name: String): Long {
        val originalBoard = getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.toCharArray().toMutableList() }
            .toMutableList()
        var boards = mutableMapOf(0 to originalBoard)
        boards[1] = emptyBoard()
        boards[-1] = emptyBoard()
        for (i in 0 until 200) {
            val newBoards = boards.map { it.key to emptyBoard() }.toMap().toMutableMap()
            for (level in boards.keys) {
                val board = boards[level]!!
                val newBoard = newBoards[level]!!
                for (row in boards[level]!!.indices) {
                    for (col in boards[level]!![0].indices) {
                        if (row == 2 && col == 2) continue
                        val neighbors = getNeighboursPart2(row, col, level, boards)
                        val cell = board[row][col]
                        if (cell == '#') {
                            if (neighbors == 1) {
                                newBoard[row][col] = '#'
                            } else {
                                newBoard[row][col] = '.'
                            }
                        } else {
                            if (neighbors == 1 || neighbors == 2) {
                                newBoard[row][col] = '#'
                            } else {
                                newBoard[row][col] = '.'
                            }
                        }
                    }
                }
            }
            boards = newBoards
            val max = boards.keys.maxOf { it }
            if (!emptyBoard(boards[max]!!)) {
                boards[max+1] = emptyBoard()
            }
            val min = newBoards.keys.minOf { it }
            if (!emptyBoard(boards[min]!!)) {
                boards[min-1] = emptyBoard()
            }
        }
        return boards.values.flatMap { it.flatten() }.count { it == '#' }.toLong()
    }

}