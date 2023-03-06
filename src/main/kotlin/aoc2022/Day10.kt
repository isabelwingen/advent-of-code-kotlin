package aoc2022

import getInputAsLines
import util.Day
import java.util.LinkedList
import kotlin.math.abs

class Day10: Day("10") {

    private fun readInput(name: String): List<Pair<String, Int>> {
        return getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.split(" ") }
            .map { if (it.size > 1) it[0] to it[1].toInt() else it[0] to 0 }
    }

    override fun executePart1(name: String): Long {
        val commands = LinkedList(readInput(name))
        var c = 1
        var wait = false
        var currrentCommand = "noop" to 0
        var registerX = 1
        val importantCycles = listOf(20, 60, 100, 140, 180, 220)
        var result = 0L
        while (commands.isNotEmpty()) {
            if (wait) {
                wait = false
            } else {
                if (currrentCommand.first == "addx") {
                    registerX += currrentCommand.second
                }
                currrentCommand = commands.pop()
                if (currrentCommand.first == "addx") {
                    wait = true
                }
            }
            if (importantCycles.contains(c)) {
                result += c * registerX
            }
            c++
        }
        return result
    }

    override fun executePart2(name: String): String {
        val commands = LinkedList(readInput(name))
        var c = 0
        var wait = false
        var currrentCommand = "noop" to 0
        var registerX = 1
        val image = listOf(
            MutableList(40) {'.'},
            MutableList(40) {'.'},
            MutableList(40) {'.'},
            MutableList(40) {'.'},
            MutableList(40) {'.'},
            MutableList(40) {'.'})
        while (commands.isNotEmpty()) {
            if (wait) {
                wait = false
            } else {
                if (currrentCommand.first == "addx") {
                    registerX += currrentCommand.second
                }
                currrentCommand = commands.pop()
                if (currrentCommand.first == "addx") {
                    wait = true
                }
            }
            val row = c.floorDiv(40)
            val col = c.mod(40)
            if (abs(registerX - col) <= 1) {
                image[row][col] = '#'
            }
            c++
        }
        return "\n" + image.joinToString("\n") { it.joinToString("") }
    }

}