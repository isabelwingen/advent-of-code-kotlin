package aoc2018

import getInputAsLines
import util.Day

class Day2: Day("2") {
    override fun executePart1(name: String): Any {
        val input = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.groupBy { c -> c } }
        val a = input.count { it.any { (_, v) -> v.size == 2 } }
        val b = input.count { it.any { (_, v) -> v.size == 3 } }
        return a*b
    }

    override fun executePart2(name: String): Any {
        val ids = getInputAsLines(name)
           .filter { it.isNotBlank() }
           .map { s -> s.map { it.code } }
        for (i in ids.indices) {
            for (j in i+1 until ids.size) {
                var strikes = 0
                val id1 = ids[i]
                val id2 = ids[j]
                for (k in id1.indices) {
                    if (id1[k] != id2[k]) {
                        strikes++
                        if (strikes > 1) {
                            break
                        }
                    }
                }
                if (strikes == 1) {
                    return id1.indices.mapNotNull { if (id1[it] == id2[it]) id1[it] else null }.map { it.toChar() }.joinToString("")
                }
            }
        }
        return 0
    }
}