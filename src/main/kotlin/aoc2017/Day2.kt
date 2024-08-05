package aoc2017

import getInputAsLines
import util.Day
import kotlin.math.abs

class Day2: Day("2") {
    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)
            .map { it.split("\t").map { s -> Integer.valueOf(s) } }
            .sumOf { line -> line.maxOf { it } - line.minOf { it } }

    }

    private fun findPair(line: List<Int>): Int {
        for (i in line.indices) {
            for (j in i+1..line.lastIndex) {
                val a = line[i]
                val b = line[j]
                if (a <= b && (b.toFloat() / a.toFloat()) == (b / a).toFloat() ) {
                    return b/a
                } else if (b <= a && (a.toFloat() / b.toFloat()) == (a / b).toFloat()) {
                    return a/b
                }
            }
        }
        return 0
    }

    override fun executePart2(name: String): Any {
        val lines = getInputAsLines(name, true)
            .map { it.split("\t", " ").map { s -> Integer.valueOf(s) } }
            .map { findPair(it) }
        return lines.sum()
    }
}