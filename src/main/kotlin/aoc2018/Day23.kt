package aoc2018

import getInputAsLines
import util.Day
import kotlin.math.abs
import kotlin.math.pow


class Day23: Day("23") {

    data class Nanobot(val pos: Vector, val radius: Int) {
        fun distance(other: Nanobot) = distance(other.pos)

        private fun distance(other: Vector) = abs(pos.x - other.x) +
                    abs(pos.y - other.y) +
                    abs(pos.z - other.z)

        fun isInDistance(pos: Vector): Boolean {
            return distance(pos) <= radius
        }
    }

    private fun parseLine(line: String): Nanobot {
        val (a, b) = line.split(">,")
        val pos = a.drop(5).split(",").map { it.trim().toInt() }
            .let { Vector(it[0], it[1], it[2]) }
        val radius = b.split("=")[1].trim().toInt()
        return Nanobot(pos, radius)
    }

    data class Pos(val x: Int, val y: Int, val z: Int) {
        fun minus(diff: Pos): Pos {
            return Pos(x - diff.x, y - diff.y, z - diff.z)
        }

        fun plus(diff: Pos): Pos {
            return Pos(x + diff.x, y + diff.y, z + diff.z)
        }
    }

    data class Vector(val x: Int, val y: Int, val z: Int)

    data class Cube(val corner: Vector, val length: Int) {
        fun numberOfIntersection(bots: List<Nanobot>): Int {
            return bots.count { contains(it.pos) || intersects(it) }
        }

        fun contains(vector: Vector): Boolean {
            return vector.let { (x, y, z) ->
                x in corner.x until corner.x + length &&
                        y in corner.y until  corner.y + length &&
                        z in corner.z until  corner.z + length
            }
        }

        fun intersects(bot: Nanobot): Boolean {
            var distSquared: Double = (length * length).toDouble()
            val c2 = Vector(corner.x + length, corner.y + length, corner.z + length)
            if (bot.pos.x < corner.x) 
                distSquared -= (bot.pos.x - corner.x).toDouble().pow(2)
            else if (bot.pos.x > c2.x)
                distSquared -= (bot.pos.x - c2.x).toDouble().pow(2)

            if (bot.pos.y < corner.y)
                distSquared -= (bot.pos.y - corner.y).toDouble().pow(2)
            else if (bot.pos.y > c2.y)
                distSquared -= (bot.pos.y - c2.y).toDouble().pow(2)

            if (bot.pos.z < corner.z)
                distSquared -= (bot.pos.z - corner.z).toDouble().pow(2)
            else if (bot.pos.z > c2.z)
                distSquared -= (bot.pos.z - c2.z).toDouble().pow(2)
            return distSquared >= 1
        }
    }


    override fun executePart1(name: String): Any {
        val bots = getInputAsLines(name, true).map(::parseLine)
        val botWithBiggestRadius = bots.maxByOrNull { it.radius }!!
        return bots.count { botWithBiggestRadius.isInDistance(it.pos) }
    }

    override fun executePart2(name: String): Any {
        return 0
    }
}