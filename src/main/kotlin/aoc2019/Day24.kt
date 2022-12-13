package aoc2019

import getInputAsLines
import util.Day
import kotlin.math.pow

class Day24: Day("24") {

    override fun executePart1(name: String): Int {
        var board = getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.toCharArray().toMutableList() }
            .toMutableList()
        val seen = HashSet<String>()

        while (true) {
            val newBoard = MutableList(board.size) { MutableList(board[0].size) { '.' } }
            for (row in board.indices) {
                for (col in board[0].indices) {
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
                return newBoard.flatten().withIndex().sumOf { if (it.value == '#') 2.0.pow(it.index.toDouble()).toInt() else 0 }
            } else {
                seen.add(boardAsString)
            }
            board = newBoard
        }
    }

    override fun expectedResultPart1() = 18852849

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }

    override fun expectedResultPart2(): Any {
        TODO("Not yet implemented")
    }
}