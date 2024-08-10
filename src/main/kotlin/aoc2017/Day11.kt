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

    private data class Vector3d(val sToN: Int, val seToNw: Int, val swToNe: Int) {
        fun honeycombDistance(): Int {
            return (abs(sToN) + abs(seToNw) + abs(swToNe))/2
        }
    }

    private fun next(dir: Dir, c: Vector3d): Vector3d {
        return when (dir) {
            Dir.N -> c.copy(swToNe = c.swToNe + 1, seToNw = c.seToNw - 1)
            Dir.S -> c.copy(swToNe = c.swToNe - 1, seToNw = c.seToNw + 1)
            Dir.SW -> c.copy(sToN = c.sToN - 1, seToNw = c.seToNw +  1)
            Dir.SE -> c.copy(sToN = c.sToN + 1, swToNe = c.swToNe-  1)
            Dir.NE -> c.copy(sToN = c.sToN + 1, seToNw = c.seToNw - 1)
            Dir.NW -> c.copy(sToN = c.sToN - 1, swToNe = c.swToNe + 1)
        }
    }

    private fun runPath(dirs: List<Dir>): Vector3d {
        var c = Vector3d(0, 0, 0)
        for (dir in dirs) {
            c = next(dir, c)
        }
        return c
    }

    private fun getAllTilesFromPaths(dirs: List<Dir>): List<Vector3d> {
        return dirs.fold(listOf(Vector3d(0, 0, 0))) { acc, dir -> acc + next(dir, acc.last())}
    }


    override fun executePart1(name: String): Any {
        val dirs = getInput(name).trim().split(",").map { Dir.parse(it.trim()) }.toList()
        return runPath(dirs).honeycombDistance()
    }

    override fun executePart2(name: String): Any {
        val dirs = getInput(name).trim().split(",").map { Dir.parse(it.trim()) }.toList()
        return getAllTilesFromPaths(dirs).maxOf { it.honeycombDistance() }
    }
}