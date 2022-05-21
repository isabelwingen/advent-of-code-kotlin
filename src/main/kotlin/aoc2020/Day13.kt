package aoc2020

import extented_euclid
import getInputAsLines

import lcm
import util.Day

class Day13 : Day("13") {

    private fun parseInput(path: String): Pair<Int, List<Int>> {
        val coll = getInputAsLines(path)
            .filter { it.isNotBlank() }
        return coll[0].toInt() to coll[1].split(",").filter { it != "x" }.map { it.toInt() }
    }

    override fun executePart1(name: String): Int {
        val (a, busIDs) = parseInput(name)
        return busIDs
            .map { it to ((a / it) + 1) * it - a }
            .sortedBy { it.second }
            .map { it.first * it.second }
            .first()
    }

    override fun expectedResultPart1() = 4315

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

    override fun expectedResultPart2() = 556100168221141L
}

