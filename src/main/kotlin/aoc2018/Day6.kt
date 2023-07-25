package aoc2018

import getInputAsLines
import util.Day
import kotlin.math.abs

class Day6: Day("6") {

    data class Point(val x: Int, val y: Int) {
        fun distanceTo(other: Point): Int =
            abs(x - other.x) + abs(y - other.y)

        fun isOnBorder(xRange: IntRange, yRange: IntRange): Boolean {
            return x == xRange.first || x == xRange.last || y == yRange.first || y == yRange.last

        }
    }

    private fun getPoints(name: String): List<Point> {
        return getInputAsLines(name)
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.split(",") }
            .map { it.map { s -> s.trim().toInt() } }
            .map { Point(it[0], it[1]) }
            .toList()
    }

    override fun executePart1(name: String): Any {
        val points = getPoints(name)
        val xRange: IntRange = (points.minByOrNull { it.x }!!.x..points.maxByOrNull { it.x }!!.x)
        val yRange: IntRange = (points.minByOrNull { it.y }!!.y..points.maxByOrNull { it.y }!!.y)
        val res = points.associateWith { 0 }.toMutableMap()

        for (x in xRange) {
            for (y in yRange) {
                val p = Point(x, y)
                val potentialNeighbors = points.map { it to it.distanceTo(p) }
                val min = potentialNeighbors.minOf { it.second }
                val neighbors = potentialNeighbors.filter { it.second == min }.map { it.first }
                if (p.isOnBorder(xRange, yRange)) {
                    res.remove(neighbors.first())
                }
                if (neighbors.size == 1 && res.containsKey(neighbors.first())) {
                    res[neighbors.first()] = res[neighbors.first()]!!+1
                }
            }
        }
        return res.values.maxOf { it }
    }


    override fun executePart2(name: String): Any {
        val points = getPoints(name)
        val xRange: IntRange = (points.minByOrNull { it.x }!!.x..points.maxByOrNull { it.x }!!.x)
        val yRange: IntRange = (points.minByOrNull { it.y }!!.y..points.maxByOrNull { it.y }!!.y)
        var res = 0
        val limit = 10000
        for (x in xRange) {
            for (y in yRange) {
                val p = Point(x,y)
                val sumOfDistance = points.sumOf { it.distanceTo(p) }
                if (sumOfDistance < limit) {
                    res++
                }
            }
        }
        return res
    }
}