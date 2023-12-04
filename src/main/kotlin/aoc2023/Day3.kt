package aoc2023

import getInputAsLines
import util.Day

class Day3: Day("3") {

    private fun neighbours(field: List<String>, only: Char? = null): (Int, Int, Int) -> Set<Pair<Int, Int>> {
        return fun(row: Int, startCol: Int, endCol: Int): Set<Pair<Int, Int>> {
            val n = mutableSetOf(row to startCol-1, row to endCol+1)
            (startCol - 1 .. endCol + 1).forEach {
                n.add(row-1 to it)
                n.add(row+1 to it)
            }
            return n
                .asSequence()
                .filter { it.first in field.indices }
                .filter { it.second in 0 until field[0].length }
                .filter { field[it.first][it.second] != '.' }
                .filter { if (only == null) true else field[it.first][it.second] == only }
                .toSet()
        }
    }

    override fun executePart1(name: String): Any {
        val lines = getInputAsLines(name, true)
        val getSymbolNeighbours = neighbours(lines)

        var selectedNumbers = 0
        for (row in lines.indices) {
            var col = 0
            while (col <= lines[0].lastIndex) {
                val current = lines[row][col]
                if (current.isDigit()) {
                    val numberAsString = lines[row].drop(col).takeWhile { it.isDigit() }
                    if (getSymbolNeighbours(row, col, col + numberAsString.length - 1).isNotEmpty()) {
                        selectedNumbers += numberAsString.toInt()
                    }
                    col += numberAsString.length
                } else {
                    col++
                }
            }
        }
        return selectedNumbers
    }

    override fun executePart2(name: String): Any {
        val lines = getInputAsLines(name, true)
        val getNeighbouringGears = neighbours(lines, '*')

        val gearMap = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        for (row in lines.indices) {
            var col = 0
            while (col <= lines[0].lastIndex) {
                val current = lines[row][col]
                if (current.isDigit()) {
                    val numberAsString = lines[row].drop(col).takeWhile { it.isDigit() }
                    getNeighbouringGears(row, col, col + numberAsString.length - 1)
                        .forEach { gearKey ->
                            gearMap.putIfAbsent(gearKey, mutableListOf())
                            gearMap[gearKey]!!.add(numberAsString.toInt())
                        }
                    col += numberAsString.length
                } else {
                    col++
                }
            }
        }
        return gearMap.filter { it.value.size == 2 }.map { it.value[0].toLong() * it.value[1] }.sum()
    }
}