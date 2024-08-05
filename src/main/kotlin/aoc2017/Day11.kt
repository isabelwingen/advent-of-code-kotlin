package aoc2017

import getInput
import util.Day
import kotlin.math.abs

class Day11: Day("11") {

    private enum class Dir {
        S, SW, SE, N, NE, NW;

        companion object {
            fun parse(str: String): Dir {
                return when (str) {
                    "s" -> S
                    "sw" -> SW
                    "se" -> SE
                    "n" -> N
                    "ne" -> NE
                    "nw" -> NW
                    else -> throw IllegalStateException()
                 }
            }
        }
    }

    private data class Vector(val x: Int, val y: Int) {
        fun createPath(): List<Dir> {
            if (x < 0 && y < 0) {
                if (x > y) {
                    val northWestPath = List(abs(x)) { Dir.NW }
                }
            }
        }
    }

    private fun runPath(dirs: List<Dir>): Vector {
        var c = Vector(0, 0)
        for (dir in dirs) {
            c = if (c.x.mod(2) == 0) {
                when (dir) {
                    Dir.N -> c.copy(y = c.y-1)
                    Dir.S -> c.copy(y = c.y+1)
                    Dir.NE -> c.copy(x = c.x+1)
                    Dir.SE -> c.copy(x = c.x+1, y = c.y+1)
                    Dir.NW -> c.copy(x = c.x-1)
                    Dir.SW -> c.copy(x = c.x-1, y = c.y+1)
                }
            } else {
                when (dir) {
                    Dir.N -> c.copy(y = c.y-1)
                    Dir.S -> c.copy(y = c.y+1)

                    Dir.NE -> c.copy(x = c.x+1, y = c.y-1)
                    Dir.SE -> c.copy(x = c.x+1)
                    Dir.NW -> c.copy(x = c.x-1, y = c.y-1)
                    Dir.SW -> c.copy(x = c.x-1)
                }
            }
        }
        return c
    }


    override fun executePart1(name: String): Any {
        val dirs = getInput(name).trim().split(",").map { Dir.parse(it.trim()) }.toList()
        //val dirs = List(397) { Dir.NW } + List(323) { Dir.N }
        val goal = runPath(dirs)
        // as both values are negative, we want to move in a North/North West Direction
        val northwestOnly = runPath(List(abs(goal.x)) { Dir.NW })
        return abs(goal.x) + abs(abs(goal.y) - abs(northwestOnly.y))
    }

    override fun executePart2(name: String): Any {
        println(runPath(List(100) { Dir.NW }))
        println(runPath(List(101) { Dir.NW }))
        println(runPath(List(102) { Dir.NW }))
        println(runPath(List(103) { Dir.NW }))
        println(runPath(List(104) { Dir.NW }))
        println(runPath(List(50) { Dir.NW } + List(50) { Dir.NE }))
        return 0
    }
}