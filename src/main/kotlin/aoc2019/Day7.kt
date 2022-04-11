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
        val ampA = IntCode("A", input)
        ampA.init(listOf(comb[0]))
        val ampB = IntCode("B", input)
        ampB.init(listOf(comb[1]))
        val ampC = IntCode("C", input)
        ampC.init(listOf(comb[2]))
        val ampD = IntCode("D", input)
        ampD.init(listOf(comb[3]))
        val ampE = IntCode("E", input)
        ampE.init(listOf(comb[4]))

        val resA = ampA.execute(0)
        val resB = ampB.execute(resA.toInt())
        val resC = ampC.execute(resB.toInt())
        val resD = ampD.execute(resC.toInt())
        val resE = ampE.execute(resD.toInt())

        res.add(resE)
    }
    return res.maxOrNull()!!
}


fun executeDay7Part2(name: String = "2019/day7.txt"): Long {
    val combs = combinations(lower = 5, upper = 9)
    val input = parseInput(name)
    val res = mutableSetOf<Long>()
    for (comb in combs) {
        val combStr = comb.toList().map { it.toString() }.joinToString("") { it }
        val ampA = IntCode("$combStr A", input)
        ampA.init(listOf(comb[0]))
        val ampB = IntCode("$combStr B", input)
        ampB.init(listOf(comb[1]))
        val ampC = IntCode("$combStr C", input)
        ampC.init(listOf(comb[2]))
        val ampD = IntCode("$combStr D", input)
        ampD.init(listOf(comb[3]))
        val ampE = IntCode("$combStr E", input)
        ampE.init(listOf(comb[4]))


        var resA: Long
        var resB: Long
        var resC: Long
        var resD: Long
        var resE = 0L
        while (!ampE.isHalted()) {
            resA = ampA.execute(resE.toInt())
            resB = ampB.execute(resA.toInt())
            resC = ampC.execute(resB.toInt())
            resD = ampD.execute(resC.toInt())
            resE = ampE.execute(resD.toInt())
        }
        res.add(resE)
    }
    return res.maxOrNull()!!
}