package aoc2023

import getInputAsLines
import util.Day
import util.Position
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day22: Day("22") {

    data class XY(val x: Int, val y: Int)

    data class Position3d(val z: Int, val xy: XY)

    data class Brick(val id: Int, val start: Position3d, val end: Position3d) {
        companion object {
            fun parse(line: String, lineId: Int): Brick {
                val (left, right) = line.split("~")
                val (x1, y1, z1) = left.split(",").map { it.toInt() }
                val (x2, y2, z2) = right.split(",").map { it.toInt() }
                return Brick(lineId, Position3d(z1, XY(x1, y1)), Position3d(z2, XY(x2, y2)))
            }
        }

        fun getPositions(startingAt: Int = min(start.z, end.z)): List<Position3d> {
            return if (isVertical()) {
                val zDiff = abs(start.z - end.z)
                (startingAt..startingAt + zDiff).map { Position3d(it, XY(start.xy.x, start.xy.y)) }
            } else if(stretchingInXDirection()) {
                (min(start.xy.x, end.xy.x)..max(start.xy.x, end.xy.x))
                    .map { Position3d(startingAt, XY(it, start.xy.y)) }
            } else {
                (min(start.xy.y, end.xy.y)..max(start.xy.y, end.xy.y))
                    .map { Position3d(startingAt, XY(start.xy.x, it)) }
            }
        }

        private fun isVertical() = start.z != end.z

        private fun stretchingInXDirection() = start.xy.x != end.xy.x
    }

    data class Grid(val zCoordinates: IntRange) {


        private val planes: MutableMap<Int, MutableMap<XY, Int>> = zCoordinates.associateWith { mutableMapOf<XY, Int>() }.toMutableMap()

        override fun toString() = "$planes"

        private fun blocked(z: Int, xy: XY) = planes.containsKey(z) && planes[z]!!.containsKey(xy)

        fun enterFallingBrick(brick: Brick): Set<Int> {
            for (startingZ in planes.keys.sorted()) {
                val possiblePositions = brick.getPositions(startingZ)
                if (possiblePositions.none { (z, xy) -> blocked(z, xy) }) {
                    possiblePositions.forEach { (z, xy) ->
                        planes.putIfAbsent(z, mutableMapOf())
                        planes[z]!![xy] = brick.id
                    }
                    return possiblePositions.mapNotNull { (z, xy) -> planes.getOrDefault(z-1, mutableMapOf())[xy] }
                        .filter { it != brick.id }
                        .toSet()
                }
            }
            throw IllegalStateException("Found no possible position")
        }
    }

    override fun executePart1(name: String): Any {
        val bricks = getInputAsLines(name, true)
            .mapIndexed { index, line -> Brick.parse(line, index) }
            .toMutableList()
            .sortedBy { min(it.start.z, it.end.z) }
        val zRange = bricks.flatMap { setOf(it.end.z, it.start.z) }.let { zets ->
            zets.minOf { it }..zets.maxOf { it }
        }
        val grid = Grid(zRange)
        return bricks.map { it.id to grid.enterFallingBrick(it) }
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}