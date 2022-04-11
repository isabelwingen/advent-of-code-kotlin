package aoc2019

import algorithm.IntCode
import getResourceAsText

private fun parseInput(name: String): IntArray {
    return getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
}

private fun combinations(lower: Int = 0, upper: Int = 4): Set<IntArray> {
    val res = mutableSetOf<IntArray>()
    for (a in lower until upper + 1) {
        for (b in lower until upper + 1) {
            for (c in lower until upper + 1) {
                for (d in lower until upper + 1) {
                    for (e in lower until upper + 1) {
                        res.add(listOf(a, b, c, d, e).toIntArray())
                    }
                }
            }
        }
    }
    return res
        .filter { it.toList().distinct() == it.toList() }
        .toSet()
}

fun executeDay7Part1(name: String = "2019/day7.txt"): Long {
    val combs = combinations()
    val input = parseInput(name)
    val res = mutableSetOf<Long>()
    for (comb in combs) {
        val ampA = IntCode(input)
        ampA.reset(listOf(comb[0], 0))
        val resA = ampA.execute().first()

        val ampB = IntCode(input)
        ampB.reset(listOf(comb[1], resA.toInt()))
        val resB = ampB.execute().first()

        val ampC = IntCode(input)
        ampC.reset(listOf(comb[2], resB.toInt()))
        val resC = ampC.execute().first()

        val ampD = IntCode(input)
        ampD.reset(listOf(comb[3], resC.toInt()))
        val resD = ampD.execute().first()

        val ampE = IntCode(input)
        ampE.reset(listOf(comb[4], resD.toInt()))
        val resE = ampE.execute().first()
        res.add(resE)
    }
    return res.maxOrNull()!!
}