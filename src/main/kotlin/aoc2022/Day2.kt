package aoc2022

import getInputAsLines
import util.Day

class Day2: Day("2") {

    private fun determineScorePart1(pair: Pair<Char, Char>): Int {
        return when (pair) {
            'A' to 'X' -> 3 + 1
            'A' to 'Y' -> 6 + 2
            'A' to 'Z' -> 0 + 3
            'B' to 'X' -> 0 + 1
            'B' to 'Y' -> 3 + 2
            'B' to 'Z' -> 6 + 3
            'C' to 'X' -> 6 + 1
            'C' to 'Y' -> 0 + 2
            'C' to 'Z' -> 3 + 3
            else -> throw java.lang.IllegalStateException()
        }
    }

    override fun executePart1(name: String): Long {
        return getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.toCharArray() }
            .map { it[0] to it[2] }
            .sumOf { determineScorePart1(it) }.toLong()

    }

    private fun determineScorePart2(pair: Pair<Char, Char>): Int {
        val rock = 1
        val paper = 2
        val scissors = 3
        val lost = 0
        val draw = 3
        val won = 6
        return when (pair) {
            'A' to 'X' -> lost + scissors
            'A' to 'Y' -> draw + rock
            'A' to 'Z' -> won + paper
            'B' to 'X' -> lost + rock
            'B' to 'Y' -> draw + paper
            'B' to 'Z' -> won + scissors
            'C' to 'X' -> lost + paper
            'C' to 'Y' -> draw + scissors
            'C' to 'Z' -> won + rock
            else -> throw java.lang.IllegalStateException()
        }
    }

    override fun executePart2(name: String): Long {
        return getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.toCharArray() }
            .map { it[0] to it[2] }
            .sumOf { determineScorePart2(it) }.toLong()
    }

}