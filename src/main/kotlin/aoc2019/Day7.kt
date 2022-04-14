package aoc2019

import algorithm.IntCode
import getResourceAsText

class Day7: Day {

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

    override fun executePart1(name: String): Long {
        val combs = combinations()
        val res = mutableSetOf<Long>()
        for (comb in combs) {
            val ampA = IntCode("A", name)
            ampA.init(listOf(comb[0]))
            val ampB = IntCode("B", name)
            ampB.init(listOf(comb[1]))
            val ampC = IntCode("C", name)
            ampC.init(listOf(comb[2]))
            val ampD = IntCode("D", name)
            ampD.init(listOf(comb[3]))
            val ampE = IntCode("E", name)
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

    override fun expectedResultPart1() = 67023L

    override fun executePart2(name: String): Long {
        val combs = combinations(lower = 5, upper = 9)
        val res = mutableSetOf<Long>()
        for (comb in combs) {
            val combStr = comb.toList().map { it.toString() }.joinToString("") { it }
            val ampA = IntCode("$combStr A", name)
            ampA.init(listOf(comb[0]))
            val ampB = IntCode("$combStr B", name)
            ampB.init(listOf(comb[1]))
            val ampC = IntCode("$combStr C", name)
            ampC.init(listOf(comb[2]))
            val ampD = IntCode("$combStr D", name)
            ampD.init(listOf(comb[3]))
            val ampE = IntCode("$combStr E", name)
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

    override fun expectedResultPart2() = 7818398L

    override fun key() = "7"

}