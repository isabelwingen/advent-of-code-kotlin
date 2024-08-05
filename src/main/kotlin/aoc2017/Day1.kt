package aoc2017

import getInputAsLines
import util.Day

class Day1: Day("1") {
    override fun executePart1(name: String): Any {
        var digits = getInputAsLines(name, true)
            .first()
            .map { it.digitToInt() }
        digits = digits + digits.first()
        return digits.dropLast(1)
            .filterIndexed { index, value -> value == digits[index + 1] }
            .sum()
    }

    override fun executePart2(name: String): Any {
        var digits = getInputAsLines(name, true)
            .first()
            .map { it.digitToInt() }
        val length = digits.size
        digits = digits + digits
        return digits.take(length)
            .filterIndexed { index, value -> value == digits[index + length/2] }
            .sum()
    }
}