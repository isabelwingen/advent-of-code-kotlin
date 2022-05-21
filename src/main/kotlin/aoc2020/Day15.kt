package aoc2020

import util.Day

private const val INPUT = "1,0,16,5,17,4"

class Day15 : Day("15") {

    private fun execute(limit: Int): Int {
        val input = INPUT.split(",").map { it.toInt() }
        val index = IntArray(30_000_000) { -1 }
        for (i in 0 until input.size - 1) {
            index[input[i]] = i
        }
        var nextNumber = input.last()
        var counter = input.size - 1;
        while (counter < limit - 1) {
            if (index[nextNumber] != -1) {
                val old = nextNumber
                nextNumber = counter - index[nextNumber]
                index[old] = counter
            } else {
                index[nextNumber] = counter
                nextNumber = 0
            }
            counter++
        }

        return nextNumber
    }

    override fun executePart1(name: String): Int {
        return execute(2020)
    }

    override fun expectedResultPart1() = 1294

    override fun executePart2(name: String): Int {
        return execute(30000000)
    }

    override fun expectedResultPart2() = 573522
}

