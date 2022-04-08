package aoc2019

import getResourceAsList

fun executeDay1Part1(name: String = "2019/day1.txt"): Long {
    return getResourceAsList(name)
        .asSequence()
        .filter { it.isNotBlank() }
        .map { it.toLong() }
        .map { it / 3 }
        .sumOf { it - 2 }
}

private fun recursiveFuel(value: Long): Long {
    val next = (value / 3) - 2
    return if (next <= 0) {
        0L
    } else {
        next + recursiveFuel(next)
    }
}

fun executeDay1Part2(name: String = "2019/day1.txt"): Long {
    return getResourceAsList(name)
        .asSequence()
        .filter { it.isNotBlank() }
        .map { it.toLong() }
        .map { it / 3 }
        .map { it - 2 }
        .map { it + recursiveFuel(it) }
        .sum()
}