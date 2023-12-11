package aoc2023

import getInputAsLines
import util.Day
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day11: Day("11") {

    private fun parse(name: String): Pair<List<CharArray>, List<Pair<Int, Int>>> {
        val field = getInputAsLines(name, true).map { it.toCharArray() }
        val galaxies = field.indices.flatMap { row ->
            field[row].indices.mapNotNull { col ->
                field[row][col].let {
                    if (it == '#') {
                        row to col
                    } else {
                        null
                    }
                }
            }
        }
        return field to galaxies
    }

    private fun shortestPathWithExpansion(field: List<CharArray>, galaxies: List<Pair<Int, Int>>, expansion: Int = 2): Long {
        val emptyRows = field.indices.filter { field[it].all { c -> c == '.' } }
        val emptyCols = field[0].indices.filter { field.map { line -> line[it] }.all { c -> c == '.' } }
        return galaxies.indices.sumOf { i ->
            ((i + 1)..galaxies.lastIndex).sumOf { j ->
                val (y0, x0) = galaxies[i]
                val (y1, x1) = galaxies[j]
                val emptyColsInBetween = emptyCols.count { it in min(x0, x1) .. max(x0, x1) }
                val emptyRowsInBetween = emptyRows.count { it in min(y0, y1) .. max(y0, y1) }
                abs(y0-y1) + abs(x0-x1) + (expansion.toLong()-1) * (emptyColsInBetween + emptyRowsInBetween)
            }
        }
    }

    override fun executePart1(name: String): Any {
        val (field, galaxies) = parse(name)
        return shortestPathWithExpansion(field, galaxies, 2)
    }

    override fun executePart2(name: String): Any {
        val (field, galaxies) = parse(name)
        return shortestPathWithExpansion(field, galaxies, 1_000_000)
    }
}