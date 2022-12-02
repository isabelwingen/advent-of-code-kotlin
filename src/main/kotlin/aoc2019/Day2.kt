package aoc2019

import aoc2019.util.IntCode
import getInput
import util.Day

class Day2: Day("2") {
    override fun executePart1(name: String): Long {
        val numbers = getInput(name)
            .trim()
            .split(",")
            .map { it.toLong() }
            .toLongArray()
        numbers[1] = 12
        numbers[2] = 2
        val intCode = IntCode("Day2", numbers)
        return intCode.execute()
    }

    override fun expectedResultPart1() = 11590668L

    override fun executePart2(name: String): Any {
        val res2 = 19690720
        val i = (res2 - 1870666) / 810000
        val j = (res2 - 1870666) % 810000

        return 100 * i + j
    }

    override fun expectedResultPart2() = 2254
}
