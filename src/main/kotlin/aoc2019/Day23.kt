package aoc2019

import aoc2019.util.IntCode
import util.Day
import java.util.LinkedList

class Day23: Day("23") {
    override fun executePart1(name: String): Any {
        val prog = IntCode("0", name)
        prog.execute(input = 0)
        return prog.execute()
    }

    private fun find(output: HashMap<Int, LinkedList<Int>>, index: Int): Pair<Int, List<Int>> {
        val res =  output.entries.firstOrNull { it.value.isNotEmpty() && it.value.first() == index }
        return res?.toPair() ?: (-1 to emptyList())
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
