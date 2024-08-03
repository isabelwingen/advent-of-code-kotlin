package aoc2018

import getInputAsLines
import util.Day
import java.math.BigInteger
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.pow


class Day23: Day("23") {

    private data class Nanobot(val pos: Vector, val radius: Int) {
        fun distance(other: Nanobot) = distance(other.pos)

        private fun distance(other: Vector) =
            abs(pos.x - other.x) + abs(pos.y - other.y) + abs(pos.z - other.z)

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

    private data class Vector(val x: Int, val y: Int, val z: Int)


    override fun executePart1(name: String): Any {
        val bots = getInputAsLines(name, true).map(::parseLine)
        val botWithBiggestRadius = bots.maxByOrNull { it.radius }!!
        return bots.count { botWithBiggestRadius.isInDistance(it.pos) }
    }

    private data class Box(val xMin: BigInteger, val xMax: BigInteger, val yMin: BigInteger, val yMax: BigInteger, val zMin: BigInteger, val zMax: BigInteger) {

        fun manhattanDistance() = xMin.abs() + yMin.abs() + zMin.abs()

        private fun intervalChildren(min: BigInteger, max: BigInteger): Set<Pair<BigInteger, BigInteger>> {
            return when (val diff = (max - min)) {
                BigInteger.ZERO -> {
                    setOf(min to max)
                }
                BigInteger.ONE -> {
                    setOf(min to min, max to max)
                }
                else -> {
                    setOf(min to min + (diff / BigInteger.TWO), min + (diff / BigInteger.TWO) + BigInteger.ONE to max)
                }
            }
        }

        fun children(): List<Box> {
            if (xMin == xMax && yMin == yMax && zMin == zMax) {
                return emptyList()
            }

            return intervalChildren(xMin, xMax).flatMap { (xA, xB) ->
                intervalChildren(yMin, yMax).flatMap { (yA, yB) ->
                    intervalChildren(zMin, zMax).map { (zA, zB) ->
                        Box(xA, xB, yA, yB, zA, zB)
                    }
                }
            }
        }

        private fun intersect(sphere: Sphere): Boolean {
            val x = if (sphere.x <= xMin) {
                xMin
            } else if (sphere.x <= xMax) {
                sphere.x
            } else {
                xMax
            }
            val y = if (sphere.y <= yMin) {
                yMin
            } else if (sphere.y <= yMax) {
                sphere.y
            } else {
                yMax
            }
            val z = if (sphere.z <= zMin) {
                zMin
            } else if (sphere.z <= zMax) {
                sphere.z
            } else {
                zMax
            }
            return sphere.inSphere(x, y, z)
        }

        fun intersect(indices: Set<Int>, spheres: List<Sphere>): Set<Int> {
            return spheres.asSequence()
                .mapIndexed { index, sphere -> index to sphere }
                .filter { indices.contains(it.first) }
                .filter { intersect(it.second) }
                .map { it.first }
                .toSet()
        }

        companion object {
            fun initialBox(spheres: List<Sphere>): Box {
                val minX = spheres.minOf { it.x - it.radius }
                val maxX = spheres.maxOf { it.x + it.radius }
                val minY = spheres.minOf { it.y - it.radius }
                val maxY = spheres.maxOf { it.y + it.radius }
                val minZ = spheres.minOf { it.z - it.radius }
                val maxZ = spheres.maxOf { it.z + it.radius }
                return Box(minX, maxX, minY, maxY, minZ, maxZ)
            }
        }
    }

    private data class Sphere(val x: BigInteger, val y: BigInteger, val z: BigInteger, val radius: BigInteger) {
        fun inSphere(px: BigInteger, py: BigInteger, pz: BigInteger): Boolean {
            return (x - px).abs() + (y - py).abs() + (z - pz).abs() <= radius
        }

        companion object {
            fun toSphere(bot: Nanobot): Sphere {
                return Sphere(BigInteger.valueOf(bot.pos.x.toLong()), BigInteger.valueOf(bot.pos.y.toLong()), BigInteger.valueOf(bot.pos.z.toLong()), BigInteger.valueOf(bot.radius.toLong()))
            }
        }
    }

    private data class QueueObject(val box: Box, val spheres: Set<Int>)

    private fun findMaxNumberOfOverlappingSpheres(spheres: List<Sphere>, initialBox: Box, lowerLimit: Int = 900): Set<Box> {
        val stack = LinkedList<QueueObject>()
        stack.add(QueueObject(initialBox, spheres.indices.toSet()))
        var result = 0
        val resultObject = mutableSetOf<Pair<Box, Int>>()
        while (stack.isNotEmpty()) {
            println("${stack.size}, $result")
            val (box, indices) = stack.pollFirst()
            val children = box.children()
            if (children.isEmpty()) {
                if (indices.size > result) {
                    result = indices.size
                    resultObject.add(box to indices.size)
                }
            } else {
                val childrenToBeAdded = children.map { QueueObject(it, it.intersect(indices, spheres)) }
                    .filterNot { it.spheres.isEmpty() }
                    .filterNot { it.spheres.size < result }
                    .filterNot { it.spheres.size < 970 }
                childrenToBeAdded.forEach { stack.addFirst(it) }
            }
        }
        val maxResult = resultObject.maxOf { it.second }
        return resultObject.filter { it.second == maxResult }.map { it.first }.toSet()
    }

    override fun executePart2(name: String): Any {
        val spheres = getInputAsLines(name, true)
            .map(::parseLine)
            .map { Sphere.toSphere(it) }
            .toList()
        val initialBox = Box.initialBox(spheres)
        val res = findMaxNumberOfOverlappingSpheres(spheres, initialBox)
        return res.minOf { it.manhattanDistance() }
    }

}