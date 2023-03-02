package aoc2022

import getInputAsLines
import util.Day

class Day9: Day("9") {

    private fun getInput(name: String): List<Pair<String, Int>> {
        return getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.split(" ") }
            .map { it[0] to it[1].toInt() }
    }

    override fun executePart1(name: String): Any {
        val commands = getInput(name)
        var head = 0 to 0
        for ((dir,steps) in commands) {
            head = when (dir) {
                "U" -> head.first to head.second + steps
                "D" -> head.first to head.second - steps
                "R" -> head.first + steps to head.second
                "L" -> head.first - steps to head.second
                else -> throw java.lang.IllegalStateException()
            }
        }
        return head
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }

}

