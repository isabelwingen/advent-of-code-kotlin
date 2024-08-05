package aoc2017

import getInputAsLines
import util.Day

class Day4: Day("4") {
    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)
            .map { it.split(" ") }
            .count { it.toSet().size == it.size }
    }

    override fun executePart2(name: String): Any {
        return getInputAsLines(name, true)
            .map { it.split(" ").map { x -> x.toList().sorted().joinToString() }}
            .count { it.toSet().size == it.size }
    }
}