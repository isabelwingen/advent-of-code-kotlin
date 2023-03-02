package aoc2019

import aoc2019.util.IntCode
import getInput
import util.Day

class Day13: Day("13") {

    override fun executePart1(name: String): Long {
        val program = IntCode("Day13", name)
        val res = mutableListOf<Long>()
        while(!program.isHalted()) {
            res.add(program.execute())
        }
        return res
            .chunked(3)
            .filter { it.size == 3 }
            .map { (it[0] to it[1]) to it[2] }
            .groupBy { it.first.second }
            .toSortedMap()
            .mapValues { it.value.sortedBy { t -> t.first.first }.map { t -> t.second } }
            .values
            .flatten()
            .count { it == 2L }
            .toLong()
    }

    private fun matchToString(tile: Int): String {
        return when (tile) {
            0  -> " "
            1   -> "#"
            2  -> "≈"
            3 -> "-"
            4   -> "o"
            else -> throw java.lang.IllegalStateException()
        }
    }

    private fun isComplete(screen: MutableList<IntArray>): Boolean {
        return screen.map { it.toList() }.flatten().all { it != -1 }
    }

    private fun hasBall(screen: MutableList<IntArray>): Boolean {
        return screen.map { it.toList() }.flatten().any { it == BALL }
    }

    private fun hasPaddle(screen: MutableList<IntArray>): Boolean {
        return screen.map { it.toList() }.flatten().any { it == PADDLE }
    }


    private fun xCoordOf(id: Int, screen: MutableList<IntArray>): Int {
        return screen
            .first { it.toList().contains(id) }
            .toList()
            .indexOf(id)
    }

    private fun done(screen: MutableList<IntArray>): Boolean {
        return isComplete(screen) && screen.map { it.toList() }.flatten().all { it != BLOCK }
    }

    private fun printScreen(screen: MutableList<IntArray>) {
        if (isComplete(screen) && hasBall(screen) && hasPaddle(screen)) {
            val s = screen.joinToString("\n") { it.toList().joinToString("") { t -> matchToString(t) } }
            println(s)
            println()
        }
    }

    override fun executePart2(name: String): Long {
        val intCode = getInput(name)
            .trim()
            .split(",")
            .map { it.toLong() }
            .toLongArray()
        intCode[0] = 2
        val program = IntCode("Day13", intCode)
        val screen = MutableList(20) { IntArray(40) { -1 } }
        var score = 0L
        var joystick = 0
        while(!program.isHalted()) {
            val x = program.execute(joystick).toInt()
            val y = program.execute(joystick).toInt()
            val v = program.execute(joystick)
            program.input.clear()
            if (x == -1 && y == 0) {
                score = v
            } else if (!done(screen)) {
                screen[y][x] = v.toInt()
                if (isComplete(screen) && hasBall(screen) && hasPaddle(screen)) {
                    val paddle = xCoordOf(PADDLE, screen)
                    val ball = xCoordOf(BALL, screen)
                    joystick = if (paddle == ball) {
                        0
                    } else if (paddle > ball) {
                        -1
                    } else {
                        1
                    }
                }
            }
        }
        return score
    }

    companion object {
        const val PADDLE: Int = 3
        const val BALL: Int = 4
        const val BLOCK: Int = 2
    }
}