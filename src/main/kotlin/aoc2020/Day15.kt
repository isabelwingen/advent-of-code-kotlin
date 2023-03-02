package aoc2020

import util.Day

private const val INPUT = "1,0,16,5,17,4"

class Day15 : Day("15") {

    private fun execute(limit: Int): Long {
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

        return nextNumber.toLong()
    }

    override fun executePart1(name: String): Long {
        return execute(2020)
    }

    override fun executePart2(name: String): Long {
        return execute(30000000)
    }

}

