package aoc2022

import getInputAsLines
import util.Day

class Day8: Day("8") {

    private fun readInput(name: String): List<List<Int>> {
        return getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.map { s -> s.digitToInt() } }
    }

    private fun visible(line: List<Int>, index: Int): Boolean {
        val value = line[index]
        val left = line.subList(0, index).none { it >= value }
        val right = line.subList(index + 1, line.size).none { it >= value }
        return left || right
    }

    override fun executePart1(name: String): Any {
        val board = readInput(name)
        return board.flatMapIndexed { row, l -> List(l.size) { col -> row to col } }
            .count { visible(board[it.first], it.second) || visible(board.map { t -> t[it.second] }, it.first) }
    }

    override fun expectedResultPart1() = 1715

    private fun scenicScoreOfPart(line: List<Int>, value: Int): Int {
        return if (line.any { it >= value }) {
            line.indexOfFirst { it >= value }.inc()
        } else {
            line.size
        }
    }

    private fun scenicScore(board: List<List<Int>>, row: Int, col: Int): Int {
        val value = board[row][col]
        val left = board[row].subList(0, col)
        val right = board[row].subList(col + 1, board[row].size)
        val up = board.map { it[col] }.subList(0, row)
        val down = board.map { it[col] }.subList(row+1, board.size)
        return scenicScoreOfPart(left.reversed(), value) *
                scenicScoreOfPart(right, value) *
                scenicScoreOfPart(up.reversed(), value) *
                scenicScoreOfPart(down, value)
    }

    override fun executePart2(name: String): Int {
        val board = readInput(name)
        return board.flatMapIndexed { row, l -> List(l.size) { col -> row to col } }
            .maxOf { scenicScore(board, it.first, it.second) }
    }

    override fun expectedResultPart2() = 374400
}