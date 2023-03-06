package aoc2022

import getInputAsLines
import util.Day
import java.util.LinkedList
import kotlin.math.abs

class Day15: Day("15") {

    private fun calculateManhattenDistance(p1: Pair<Long, Long>, p2: Pair<Long, Long>): Long {
        return abs(p1.first - p2.first) + abs(p1.second - p2.second)
    }

    private fun transformLine(line: String): Pair<Pair<Long, Long>, Pair<Long, Long>> {
        val splitted = line.split(" ")
            .filterIndexed { i, _ -> setOf(2, 3, 8, 9).contains(i) }
            .mapIndexed { i, s -> if (i == 3) s else s.dropLast(1)  }
            .map { it.split("=")[1].toLong() }
        val (sensor_x, sensor_y, beacon_x, beacon_y) = splitted
        return (sensor_x to sensor_y) to (beacon_x to beacon_y)
    }

    fun getPointsThatAreInTheSensorRange(sensor: Pair<Long, Long>, beam: Pair<Long, Long>, y: Long): LongRange {
        val manhattenDistance = calculateManhattenDistance(sensor, beam)
        val yDiff = abs(sensor.second - y)
        val maxXDiff = manhattenDistance - yDiff
        return if (maxXDiff > 0) {
            LongRange(sensor.first-maxXDiff, sensor.first+maxXDiff)
        } else {
            LongRange.EMPTY
        }
    }

    fun LongRange.join(other: LongRange): Set<LongRange> {
        return if ((other.first < first && other.last < first) || (other.first > last && other.last > last)) { //no overlap
            setOf(this, other)
        } else if (other.first >= first && other.last <= last) {
            setOf(this)
        } else if (first >= other.first && last <= other.last) {
            setOf(other)
        } else if (other.first <= first) {
            setOf(LongRange(other.first, last))
        } else {
            setOf(LongRange(first, other.last))
        }
    }

    private fun simplifyRangesStep(ranges: Set<LongRange>): Set<LongRange> {
        return ranges.map { setOf(it) }.reduce { a, b -> a.flatMap { aa -> b.flatMap { bb -> bb.join(aa) } }.toSet() }
    }

    private fun simplifyRanges(ranges: Set<LongRange>): Set<LongRange> {
        var res = ranges
        while (true) {
            val newRes = simplifyRangesStep(res)
            if (newRes == res) {
                break
            }
            res = newRes
        }
        return res
    }

    private fun findDetectionRangesForLine(manhattanMap: Map<Pair<Long, Long>, Pair<Long, Long>>, y: Long): Set<LongRange> {
        val res = manhattanMap
            .map { (k, v) -> getPointsThatAreInTheSensorRange(k, v, y) }
            .filter { !it.isEmpty() }
            .toSet()
        return simplifyRanges(res)

    }

    override fun executePart1(name: String): Long {
        val manhattanMap = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .associate { transformLine(it) }
        val y = 2_000_000L
        val beamsOnYLine = manhattanMap.values.toSet().count { it.second == y }
        val res = findDetectionRangesForLine(manhattanMap, y)
        return res.first().count() - beamsOnYLine.toLong()
    }

    override fun executePart2(name: String): Long {
        val manhattanMap = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .associate { transformLine(it) }
        val line = LongRange(0, 4_000_000L)
            .mapIndexed { i, it -> i to findDetectionRangesForLine(manhattanMap, it) }.first { it.second.count() == 2 }
        val y = line.first
        val x = line.second.maxByOrNull { it.first }!!.first - 1
        return x * 4_000_000L + y
    }
}