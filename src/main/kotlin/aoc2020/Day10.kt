package aoc2020

import getInputAsLines
import util.Day
import kotlin.math.pow

class Day10: Day("10") {
    private fun parseInput(path: String): IntArray {
        return getInputAsLines(path)
            .filter { it.isNotBlank() }
            .map { it.toInt() }
            .sorted()
            .toIntArray()
    }

    override fun executePart1(name: String): Long {
        val list = parseInput(name)
        var ones = 1
        var threes = 1
        for (i in 0 until list.size -1) {
            if (list[i + 1] - list[i] == 1) {
                ones += 1
            } else {
                threes += 1
            }
        }
        return ones * threes.toLong()
    }

    private fun getCombinations(s: Int): Int {
        return when (s) {
            3 -> 2
            4 -> 4
            5 -> 7
            else -> 0
        }
    }

    private fun addMinAndMax(l: IntArray): IntArray {
        val coll = l.toMutableList()
        coll.add(0)
        coll.add(l.last() + 3)
        return coll.sorted().toIntArray()
    }

    override fun executePart2(name: String): Long {
        val list = addMinAndMax(parseInput(name))
        val pairs = list
            .toList()
            .windowed(size = 2)
            .map { it[0] to it[1] }

        val res = mutableListOf<List<Int>>()
        var acc = mutableSetOf<Int>()
        for (pair in pairs) {
            acc = if (pair.second - pair.first == 3) {
                res.add(acc.toList().sorted())
                mutableSetOf()
            } else {
                acc.union(setOf(pair.first, pair.second)).toMutableSet()
            }
        }
        return res
            .asSequence()
            .filter { it.isNotEmpty() }
            .filter { it.size > 2 }
            .groupBy { it.size }
            .mapValues { getCombinations(it.key).toDouble().pow(it.value.size.toDouble()) }
            .map { it.value.toLong() }
            .reduceRight { a, b -> a * b}
    }

}