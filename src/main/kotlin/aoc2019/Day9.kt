package aoc2019

import aoc2019.util.IntCode
import util.Day

class Day9: Day("9") {

    override fun executePart1(name: String): Long {
        val prog = IntCode("Day9", name)
        var res = prog.execute(1)
        while(!prog.isHalted()) {
            res = prog.execute()
        }
        return res
    }

    override fun expectedResultPart1() = 3013554615L

    override fun executePart2(name: String): Long {
        val prog = IntCode("Day9", name)
        var res = prog.execute(2)
        while(!prog.isHalted()) {
            res = prog.execute()
        }
        return res
    }

    override fun expectedResultPart2()= 50158L
}