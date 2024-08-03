package aoc2018

import getInputAsLines
import util.Day
import kotlin.math.abs

class Day25: Day("25") {

    data class Vector(val x: Long, val y: Long, val z: Long, val t: Long) {

        private fun manhattanDistance(other: Vector): Long {
            return abs(x - other.x) + abs(y - other.y) + abs(z - other.z) + abs(t - other.t)
        }

        fun inSameCluster(other: Vector) = manhattanDistance(other) <= 3L

        companion object {
            fun from(line: String): Vector {
                val (x, y, z, t) = line.split(",").filter { it.isNotBlank() }.map { Integer.valueOf(it).toLong() }
                return Vector(x, y, z, t)
            }
        }
    }

    override fun executePart1(name: String): Any {
        val vectors = getInputAsLines(name, true).map { Vector.from(it) }
        val clusters = mutableSetOf<Set<Vector>>()
        for (vector in vectors) {
            val matchingClusters = clusters.filter { cluster -> cluster.any { it.inSameCluster(vector)} }.toSet()
            if (matchingClusters.isEmpty()) {
                clusters.add(setOf(vector))
            } else {
                clusters.removeAll(matchingClusters)
                clusters.add(matchingClusters.flatten().toSet() + vector)
            }
        }
        return clusters.size
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}