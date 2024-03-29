package aoc2020

import getInputAsLines
import util.Day

class Day3: Day("3") {

    private fun readInput(name: String): List<CharArray> {
        return getInputAsLines(name)
            .filter { x -> x.isNotBlank() }
            .map { it.asIterable().toList() }
            .map { it.toCharArray() }

    }

    private fun calculateSlope(path: String, x: Int, y: Int): Long {
        val map = readInput(path)
        val steps = map.count() / y
        var treeCount = 0L
        for (step in 0 until steps) {
            val xx = (step*x) % map[0].count()
            val field = map[step*y][xx]
            if ('#' == field) {
                treeCount += 1
            }
        }
        return treeCount
    }

    override fun executePart1(name: String): Long {
        return calculateSlope(name, 3, 1)
    }

    override fun executePart2(name: String): Long {
        val a = calculateSlope(name, 1, 1)
        val b = calculateSlope(name, 3, 1)
        val c = calculateSlope(name, 5, 1)
        val d = calculateSlope(name, 7, 1)
        val e = calculateSlope(name, 1, 2)
        return a * b * c * d * e
    }

}


