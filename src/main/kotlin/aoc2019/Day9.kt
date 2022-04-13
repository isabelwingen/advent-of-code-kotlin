package aoc2019

import algorithm.IntCode
import getResourceAsText

class Day9: Day {

    override fun executePart1(name: String): Long {
        val prog = IntCode("Day9", name)
        var res = prog.execute(1)
        println("Result: $res")
        while(!prog.isHalted()) {
            res = prog.execute()
            println("Result: $res")
        }
        return res
    }

    override fun expectedResultPart1() = 3013554615L

    override fun executePart2(name: String): Long {
        val prog = IntCode("Day9", name)
        var res = prog.execute(2)
        println("Result: $res")
        while(!prog.isHalted()) {
            res = prog.execute()
            println("Result: $res")
        }
        return res
    }

    override fun expectedResultPart2()= 50158L

    override fun key() = "9"
}