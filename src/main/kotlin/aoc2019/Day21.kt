package aoc2019

import aoc2019.util.IntCode
import util.Day

class Day21: Day("21") {

    private fun toProgram(instructions: List<String>): List<Int> {
        val p = instructions
            .joinToString("\n")
            .map { it.code }
            .toMutableList()
        p.add(10)
        return p.toList()
    }

    override fun executePart1(name: String): Long {
        val prog = IntCode("Day21", name)
        val code = toProgram(listOf(
            "NOT B T",
            "NOT C J",
            "OR T J",
            "AND D J",
            "NOT A T",
            "OR T J",
            "WALK"))
        var x = 0L
        while (!prog.isHalted()) {
            x = prog.execute(code.map { it.toLong() })
            if (x < Int.MAX_VALUE) {
                print(x.toInt().toChar())
            }
        }
        return x
    }

    override fun expectedResultPart1() = 19362259L

    override fun executePart2(name: String): Long {
        val prog = IntCode("Day21", name)
        val code = toProgram(listOf(
            "NOT B T", // B is hole
            "NOT C J", // C is hole
            "OR T J",  // B or C is hole
            "AND D J", // (B or C is hole) and D is ground
            "AND H J", // H is also ground
            "NOT A T",
            "OR T J", // or A is hole
            "RUN"))
        var x = 0L
        while (!prog.isHalted()) {
            x = prog.execute(code.map { it.toLong() })
            if (x < Int.MAX_VALUE) {
                print(x.toInt().toChar())
            }
        }
        return x
    }

    override fun expectedResultPart2() = 1141066762L
}