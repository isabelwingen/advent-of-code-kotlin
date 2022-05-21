package aoc2019

import getInputAsLines
import util.Day

class Day1: Day("1") {

    override fun executePart1(name: String): Long {
        return getInputAsLines(name)
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .map { it / 3 }
            .sumOf { it - 2 }
    }

    override fun expectedResultPart1() = 3210097L

    override fun executePart2(name: String): Long {
        return getInputAsLines(name)
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .map { it / 3 }
            .map { it - 2 }
            .map { it + recursiveFuel(it) }
            .sum()
    }

    override fun expectedResultPart2() = 4812287L

    private fun recursiveFuel(value: Long): Long {
        val next = (value / 3) - 2
        return if (next <= 0) {
            0L
        } else {
            next + recursiveFuel(next)
        }
    }

}