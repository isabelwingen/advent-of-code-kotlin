package aoc2019

import algorithm.IntCode
import getResourceAsText

class Day5: Day {
    override fun key(): String = "5"

    override fun executePart1(name: String): Long {
        val intCode = IntCode("Day5", name)
        intCode.init(listOf(1))
        var res = 0L
        while (res == 0L) {
            res =  intCode.execute()
        }
        return res
    }

    override fun executePart2(name: String): Long {
        val numbers = getResourceAsText(name)!!
            .trim()
            .split(",")
            .map { it.toInt() }
            .toIntArray()
        val intCode = IntCode("Day5", numbers)
        intCode.init(listOf(5))
        return intCode.execute()
    }

    override fun expectedResultPart1() = 13087969L

    override fun expectedResultPart2() = 14110739L
}
