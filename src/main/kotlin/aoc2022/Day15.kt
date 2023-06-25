package aoc2022

import getInputAsLines
import util.Day
import kotlin.math.abs

private const val MAX_ROW = 4_000_000L

class Day15 : Day("15") {

    private fun calculateManhattanDistance(p1: Pair<Long, Long>, p2: Pair<Long, Long>): Long {
        return abs(p1.first - p2.first) + abs(p1.second - p2.second)
    }

    private fun transformLine(line: String): Pair<Pair<Long, Long>, Pair<Long, Long>> {
        val splitted = line.split(" ")
            .filterIndexed { i, _ -> setOf(2, 3, 8, 9).contains(i) }
            .mapIndexed { i, s -> if (i == 3) s else s.dropLast(1) }
            .map { it.split("=")[1].toLong() }
        val (sensor_x, sensor_y, beacon_x, beacon_y) = splitted
        return (sensor_x to sensor_y) to (beacon_x to beacon_y)
    }

    override fun executePart1(name: String): Any {
        val sensorToManhattan = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { transformLine(it) }
            .map { Triple(it.first, it.second, calculateManhattanDistance(it.first, it.second)) }

        val y = 2_000_000L
        val l = sensorToManhattan.map { (s, _, d) ->
            val yDiff = abs(s.second - y)
            if (yDiff > d) {
                null
            } else {
                LongRange(s.first-(d-yDiff), s.first+(d-yDiff))
            }
        }.filterNotNull()
            .sortedBy { it.first }
            .toMutableList()
        var i = 0
        while (i < l.lastIndex) {
            if (l[i+1].first <= l[i].last) {
                if (l[i+1].last <= l[i].last) {
                    l.removeAt(i+1)
                } else {
                    l[i] = LongRange(l[i].first, l[i+1].last)
                    l.removeAt(i+1)
                }
            } else {
                i++
            }
        }
        return l.first().count().toLong() - sensorToManhattan.map { it.second }.toSet().count { it.second == 2_000_000L }
    }


    private fun inSensorRange(sensor: Pair<Long, Long>, manhattanDistance: Long, point: Pair<Long, Long>): Boolean {
        return calculateManhattanDistance(sensor, point) <= manhattanDistance
    }

    private fun findSinglePointNotFound(sensorToManhattan: Map<Pair<Long, Long>, Long>): Pair<Long, Long> {
        sensorToManhattan.forEach { (sensor, manhattanDistance) ->
            val lowestRow = maxOf(0L, sensor.second-manhattanDistance)
            val highestRow = minOf(MAX_ROW, sensor.second+manhattanDistance)
            val lowest = sensor.first to lowestRow-1
            if (sensorToManhattan.none { inSensorRange(it.key, it.value, lowest) }) {
                return lowest
            }
            val highest = sensor.first to highestRow+1
            if (sensorToManhattan.none { inSensorRange(it.key, it.value, highest) }) {
                return highest
            }
            for (row in lowestRow .. highestRow) {
                val rowDistance = abs(sensor.second-row)
                val colDistance = manhattanDistance-rowDistance
                val left = sensor.first-colDistance-1 to row
                if (left.first > 0) {
                    if (sensorToManhattan.none { inSensorRange(it.key, it.value, left) }) {
                        return left
                    }
                }
                val right = sensor.first+colDistance+1 to row
                if (right.first < MAX_ROW) {
                    if (sensorToManhattan.none { inSensorRange(it.key, it.value, right) }) {
                        return right
                    }
                }
            }
        }
        throw IllegalStateException()
    }

    override fun executePart2(name: String): Any {
        val sensorToManhattan = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { transformLine(it) }
            .associate { it.first to calculateManhattanDistance(it.first, it.second) }

        val (x,y) = findSinglePointNotFound(sensorToManhattan)

        return x * MAX_ROW + y
    }

}