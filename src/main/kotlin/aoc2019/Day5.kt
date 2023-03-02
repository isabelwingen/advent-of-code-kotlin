package aoc2019

import aoc2019.util.IntCode
import util.Day

class Day5: Day("5") {

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
        val intCode = IntCode("Day5", name)
        intCode.init(listOf(5))
        return intCode.execute()
    }

}
