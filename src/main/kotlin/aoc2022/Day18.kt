package aoc2022

import getInputAsLines
import util.Day

class Day18: Day("18") {

    private fun neighbours(p: Triple<Int, Int, Int>): Set<Triple<Int, Int, Int>> {
        return setOf(
            p.copy(first = p.first + 1),
            p.copy(first = p.first - 1),
            p.copy(second = p.second + 1),
            p.copy(second = p.second - 1),
            p.copy(third = p.third + 1),
            p.copy(third = p.third - 1))
    }

    override fun executePart1(name: String): Long {
        val points = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map {  it.split(",").map { c -> c.toInt() } }
            .map { Triple(it[0], it[1], it[2]) }
            .toSet()
        var exposed = 0L
        points.forEach { p ->
            exposed += neighbours(p).count { !points.contains(it) }
        }
        return exposed
    }

    private fun allPoints(x: IntRange, y: IntRange, z: IntRange) =
        x.flatMap { xx -> y.flatMap { yy -> z.map { zz -> Triple(xx, yy, zz) }} }.toSet()

    override fun executePart2(name: String): Long {
        val blocks = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map {  it.split(",").map { c -> c.toInt() } }
            .map { Triple(it[0], it[1], it[2]) }
            .toSet()

        val x = IntRange(-1, blocks.maxOf { it.first }+1)
        val y = IntRange(-1, blocks.maxOf { it.second }+1)
        val z = IntRange(-1, blocks.maxOf { it.third }+1)
        val allPoints = allPoints(x,y,z)
        val outside = mutableSetOf(Triple(-1, -1, -1))
        while (true) {
            val newOuties = outside
                .flatMap { o ->
                    neighbours(o)
                        .filter { allPoints.contains(it) }
                        .filter { !outside.contains(it) }
                        .filter { !blocks.contains(it) } }
                .toSet()
            outside.addAll(newOuties)
            if (newOuties.isEmpty()) break
        }
        return blocks.sumOf { b -> neighbours(b).count { outside.contains(it) } }.toLong()
    }
}