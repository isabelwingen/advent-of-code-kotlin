package aoc2019

import algorithm.IntCode
import util.Day

class Day19: Day("19") {
    override fun executePart1(name: String): Any {
        var sum = 0
        for (x in 0 until 50) {
            for (y in 0 until 50) {
                val prog = IntCode("Day19", name)
                sum += prog.execute(listOf(x, y)).toInt()
            }
        }
        return sum

    }

    override fun expectedResultPart1(): Any {
        TODO("Not yet implemented")
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }

    override fun expectedResultPart2(): Any {
        TODO("Not yet implemented")
    }
}