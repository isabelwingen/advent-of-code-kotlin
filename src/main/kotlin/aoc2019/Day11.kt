package aoc2019

import algorithm.IntCode
import java.util.Comparator

class Robot(var orientation: String = "UP", var position: Pair<Int, Int> = 0 to 0) {
    override fun toString() = "Robot is facing $orientation at position $position"

    private fun turnLeft() {
        when (orientation) {
            "UP" -> orientation = "LEFT"
            "LEFT" -> orientation = "DOWN"
            "DOWN" -> orientation = "RIGHT"
            "RIGHT" -> orientation = "UP"
        }
    }

    private fun turnRight() {
        when (orientation) {
            "UP" -> orientation = "RIGHT"
            "RIGHT" -> orientation = "DOWN"
            "DOWN" -> orientation = "LEFT"
            "LEFT" -> orientation = "UP"
        }
    }

    private fun goForward() {
        val (x,y) = position
        when (orientation) {
            "UP" -> position = x to (y - 1)
            "DOWN" -> position = x to (y + 1)
            "RIGHT" -> position = (x + 1) to y
            "LEFT" -> position = (x - 1) to y
        }
    }

    fun action(colorToPaint: Int, turn: Int): Pair<Pair<Int, Int>, Int> {
        val res = position to colorToPaint
        if (turn == 0) {
            turnLeft()
        } else {
            turnRight()
        }
        goForward()
        return res
    }
}

class Day11: Day {
    override fun executePart1(name: String): Int {
        val program = IntCode("Robot", name)
        val robot = Robot()
        val paintedTiles = mutableMapOf<Pair<Int, Int>, Int>()

        while (!program.isHalted()) {
            val colorToPaint = program.execute(paintedTiles.getOrDefault(robot.position, 0))
            val turn = program.execute()
            val next = robot.action(colorToPaint.toInt(), turn.toInt())
            paintedTiles[next.first] = next.second
            println(robot)
        }
        return paintedTiles.keys.size
    }

    override fun expectedResultPart1() = 2441

    override fun executePart2(name: String): Any {
        val program = IntCode("Robot", name)
        val robot = Robot()
        val paintedTiles = mutableMapOf((0 to 0) to 1)

        while (!program.isHalted()) {
            val input = paintedTiles.getOrDefault(robot.position, 0)
            val colorToPaint = program.execute(input)
            val turn = program.execute(input)
            val next = robot.action(colorToPaint.toInt(), turn.toInt())
            paintedTiles[next.first] = next.second
            println(robot)
        }
        val res = mutableListOf<MutableList<String>>()
        for (i in 0 until 6) {
            res.add(MutableList(43) { " " })
        }

        paintedTiles
            .filter { it.value == 1 }
            .keys
            .forEach { res[it.second][it.first] = "#" }

        return res
            .map { t -> t.joinToString("") { it } }
            .joinToString("\n") { it }
    }

    override fun expectedResultPart2() = " ###  #### ###  #### ###  ###  #  #  ##    \n" +
            " #  #    # #  # #    #  # #  # # #  #  #   \n" +
            " #  #   #  #  # ###  #  # #  # ##   #      \n" +
            " ###   #   ###  #    ###  ###  # #  #      \n" +
            " #    #    # #  #    #    # #  # #  #  # # \n" +
            " #    #### #  # #    #    #  # #  #  ##    "

    override fun key(): String = "11"
}