package aoc2022

import getInputAsLines
import util.Day
import kotlin.math.abs

class Day15A: Day("15") {

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

    override fun executePart1(name: String): Any {
        TODO("Not yet implemented")
    }

    private fun Pair<Long, Long>.contains(l: Long): Boolean {
        return this.first <= l && l <= this.second
    }

    fun addNoZoneToField(field: MutableMap<Long, MutableList<Pair<Long, Long>>>, rowIndex: Long, zone: Pair<Long, Long>): Any {
        if (!field.containsKey(rowIndex)) {
            field[rowIndex] = mutableListOf(zone)
        } else {
            val row = field[rowIndex]!!
            if (row.any { existingZone -> existingZone.contains(zone.first) && existingZone.contains(zone.second) }) {
                // do nothing
            } else if (row.any { existingZone -> existingZone.contains(zone.first) } && row.any { existingZone -> existingZone.contains(zone.second) }) {
                // lower limit is in one zone, upper limit is in another zone.
                val i = row.indexOfLast { it.contains(zone.first) }
                val j = row.indexOfLast { it.contains(zone.second) }
                row[i] = row[i].first to row[j].second
                repeat(IntRange(i+1, j).count()) { row.removeAt(i + 1) }
            } else if (row.all { it.second < zone.first }) {
                // adding at the end
                row.add(row.lastIndex+1, zone)
            } else if (row.all { it.first > zone.second }) {
                // adding at the beginning
                row.add(0, zone)
            } else if (row.any { it.contains(zone.first) } && row.any { it.second < zone.second }) {
                // left inside, right outside
                val i = row.indexOfLast { it.contains(zone.first) }
                val j = row.indexOfLast { it.second < zone.second }
                row[i] = row[i].first to zone.second
                repeat(IntRange(i+1, j).count()) { row.removeAt(i + 1) }
            } else if (row.any { it.contains(zone.second) } && row.any { it.first > zone.first }) {
                // left outside, right inside
                val i = row.indexOfFirst { it.first > zone.first }
                val j = row.indexOfLast { it.contains(zone.second) }
                row[j] = zone.first to row[j].second
                repeat(IntRange(i, j-1).count()) { row.removeAt(i) }
            } else if (row.any { zone.first < it.first } && row.any { it.second < zone.second }) {
                // left outside, right outside
                val i = row.indexOfFirst { zone.first < it.first }
                val j = row.indexOfLast { it.second < zone.second }
                if (j < i) {
                    row.add(j+1, zone)
                } else {
                    row[i] = zone.first to zone.second
                    repeat(IntRange(i + 1, j).count()) { row.removeAt(i + 1) }
                }
            }
        }
        return 0
    }

    override fun executePart2(name: String): Any {
        val sensorToBeacon = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .associate { transformLine(it) }
        val field = mutableMapOf<Long, List<Pair<Long, Long>>>()
        sensorToBeacon.forEach { sensor, beacon ->
            val manhattenDiff = calculateManhattenDistance(sensor, beacon)

        }
        return 0
    }


}