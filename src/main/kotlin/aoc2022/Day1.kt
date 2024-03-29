package aoc2022

import getInputAsLines
import splitBy
import util.Day

class Day1: Day("1") {
    override fun executePart1(name: String): Long {
       return getInputAsLines(name)
           .splitBy { it.isBlank() }
           .filter { it.isNotEmpty() && it.all { s -> s.isNotEmpty() }}
           .maxOf { it.sumOf { s -> s.toInt() } }.toLong()
    }

    override fun executePart2(name: String): Long {
        return getInputAsLines(name)
            .splitBy { it.isBlank() }
            .asSequence()
            .filter { it.isNotEmpty() && it.all { s -> s.isNotEmpty() }}
            .map { it.sumOf { s -> s.toInt() } }
            .sortedDescending()
            .take(3)
            .sum().toLong()
    }

}