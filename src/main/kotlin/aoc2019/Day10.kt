package aoc2019

import getInputAsLines
import util.Day
import kotlin.math.*

class Day10: Day("10") {

    private fun parseInput(name: String): Pair<Int, Set<Point>> {
        val lines = getInputAsLines(name)
            .filter { it.isNotBlank() }
        val n = lines.size
        val comets = lines.asSequence()
            .mapIndexed { row, s -> s.mapIndexedNotNull { col, c -> (col to row) to c } }
            .flatten()
            .filter { it.second == '#' }
            .map { it.first }
            .map { Point(it.first.toDouble(), it.second.toDouble()) }
            .toSet()
        return n to comets
    }

    data class Point(val x: Double, val y: Double)

    data class Vector(val degree: Point, val length: Double)

    fun line(start: Point, end: Point): Vector {
        val x = end.x - start.x
        val y = end.y - start.y
        val length = sqrt(x*x + y*y)
        val degree = if (abs(x) == 0.0) {
            Point(x, y / abs(y))
        } else if (x < 0.0) {
            Point(x / abs(x),y / abs(x))
        } else {
            Point(x / x, y / x)
        }
        return Vector(degree, length)
    }

    private fun getAsteroidsInView(asteroid: Point, asteroids: Collection<Point>): Int {
        return asteroids
            .filter { it != asteroid }
            .map { line(asteroid, it).degree }
            .distinct()
            .count()
    }

    override fun executePart1(name: String): Long {
        val (_, asteroids) = parseInput(name)
        return asteroids.maxOf { getAsteroidsInView(it, asteroids) }.toLong()
    }

    private fun getDegree(point: Point): Double {
        val degree = Math.toDegrees(atan(point.y / point.x))
        return if (point.x == -1.0) {
            degree + 270
        } else {
            degree + 90
        }
    }

    override fun executePart2(name: String): Long {
        val (_, asteroids) = parseInput(name)
        val station = asteroids.maxByOrNull { getAsteroidsInView(it, asteroids) }!!
        val map = asteroids
            .filter { it != station }
            .map { it to line(station, it) }
            .groupBy { it.second.degree }
            .mapValues { it.value.sortedBy { t -> t.second.length }.map { t -> t.first } }
            .mapKeys { getDegree(it.key) }
            .toSortedMap()
        val point = map.toList()[199].second.first()
        return (point.x * 100 + point.y).toLong()
    }

}