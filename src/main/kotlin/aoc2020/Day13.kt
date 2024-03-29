package aoc2020

import getInputAsLines
import util.Day
import kotlin.math.abs

class Day13 : Day("13") {

    private fun parseInput(path: String): Pair<Int, List<Int>> {
        val coll = getInputAsLines(path)
            .filter { it.isNotBlank() }
        return coll[0].toInt() to coll[1].split(",").filter { it != "x" }.map { it.toInt() }
    }

    override fun executePart1(name: String): Long {
        val (a, busIDs) = parseInput(name)
        return busIDs
            .map { it to ((a / it) + 1) * it - a }
            .sortedBy { it.second }
            .map { it.first * it.second }
            .first()
            .toLong()
    }

    // t+id_1  mod a1 == 0
    // t+id_2  mod a2 == 0
    // t+id_3  mod a3 == 0
    // t+id_4  mod a4 == 0
    override fun executePart2(name: String): Long {
        val busIDs = getInputAsLines(name)
            .filter { it.isNotBlank() }[1]
            .split(",")
            .mapIndexed { id, x -> id to x }
            .filter { it.second != "x" }
            .map { it.first to it.second.toLong() }
        val m = lcm(busIDs.map { it.second })
        val ms = busIDs.map { m / it.second }
        val e = ms.mapIndexed { id, x -> x * extented_euclid(busIDs[id].second, x).third }
        val s = -e
            .mapIndexed { id, x -> x * busIDs[id].first }
            .reduceRight { a, b -> a + b }
        val r = e.reduceRight { a, b -> a + b }
        return (s / r) % m
    }

}

fun extented_euclid(a: Long, b: Long): Triple<Long, Long, Long> {
    if (b == 0L) {
        return Triple(a, 1, 0)
    } else {
        val (dd, ss, tt) = extented_euclid(b, a % b)
        val t = ss - (a / b) * tt
        return Triple(dd, tt, t)
    }
}

fun lcm(a: Long, b: Long): Long {
    return abs(a * b) / extented_euclid(a, b).first
}

fun lcm(numbers: List<Long>): Long {
    return numbers.reduceRight { a, b -> lcm(a, b)}
}

