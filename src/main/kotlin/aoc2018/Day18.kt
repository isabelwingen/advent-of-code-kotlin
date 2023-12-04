package aoc2018

import getInputAsLines
import util.Day

private const val SIZE = 50

class Day18: Day("18") {

    private fun neighbours(row: Int, col: Int, field: Array<CharArray>): List<Char> {
        return setOf(
            row-1 to col-1, row-1 to col, row-1 to col+1,
            row to col-1, row to col+1,
            row+1 to col-1, row+1 to col, row+1 to col+1)
            .filter { it.first in 0 until SIZE }
            .filter { it.second in 0 until SIZE }
            .map { field[it.first][it.second] }
    }

    private fun step(field: Array<CharArray>): Array<CharArray> {
        val newField = Array(SIZE) { r -> CharArray(SIZE) { c -> field[r][c] } }
        for (row in field.indices) {
            for (col in field.first().indices) {
                val value = field[row][col]
                if (value == '.' && neighbours(row, col, field).count { it == '|' } >= 3) {
                    newField[row][col] = '|'
                }
                if (value == '|' && neighbours(row, col, field).count { it == '#' } >= 3) {
                    newField[row][col] = '#'
                }
                if (value == '#') {
                    neighbours(row, col, field).let { n ->
                        if (n.count { it == '#' } < 1 || n.count { it == '|' } < 1) {
                            newField[row][col] = '.'
                        }
                    }
                }
            }
        }
        return newField
    }

    private fun score(field: Array<CharArray>): Long {
        field.flatMap { it.toList() }.let { acres ->
            return acres.count { it == '|' }.toLong() * acres.count { it == '#' }
        }
    }


    override fun executePart1(name: String): Any {
        var field = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.toCharArray() }
            .toTypedArray()
        for (i in 0..9) {
            field = step(field)
        }
        return score(field)

    }

    override fun executePart2(name: String): Any {
        var field = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.toCharArray() }
            .toTypedArray()
        val seen = mutableListOf<String>()
        while (true) {
            field = step(field)
            val s = field.joinToString("\n") { it.toList().joinToString("") }
            if (seen.contains(s)) {
                seen.add(s)
                break
            }
            seen.add(s)
        }
        val cycleLength = seen.lastIndex - seen.indexOf(seen.last())
        val stepsUntilNow = seen.size
        val cycleTimes = (1000000000L - stepsUntilNow) / cycleLength
        val remainingSteps = 1000000000L - stepsUntilNow - cycleLength * cycleTimes
        for (i in 1..remainingSteps) {
            field = step(field)
        }
        return score(field)
    }
}