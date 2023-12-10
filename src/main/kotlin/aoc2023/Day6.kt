package aoc2023

import getInputAsLines
import util.Day
import kotlin.math.sqrt

class Day6: Day("6") {

    private fun calculateRecordBeating(time: Long, distance: Long): Long {
        val squareRoot = sqrt(time * time - 4 * distance.toDouble())
        val a = (0.5 * (time - squareRoot)).toLong() + 1
        val b = (0.5 * (time + squareRoot)).toLong()
        return b - a + 1
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)
            .map { it.split(" ") }
            .map { it.filter { e -> e.isNotBlank() } }
            .map { it.drop(1) }
            .let { (times,distances) ->
                (0..3).map { times[it].toLong() to distances[it].toLong() }
            }
            .map { calculateRecordBeating(it.first, it.second) }
            .reduce { a, b -> a * b }
    }

    override fun executePart2(name: String): Any {
        val (time, distance) = getInputAsLines(name, true)
            .map { it.split(" ").drop(1).joinToString("").trim().toLong() }
        return calculateRecordBeating(time, distance)
    }
}