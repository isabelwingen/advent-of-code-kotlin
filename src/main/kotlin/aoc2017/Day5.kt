package aoc2017

import getInputAsLines
import util.Day

class Day5: Day("5") {
    override fun executePart1(name: String): Any {
        val list = getInputAsLines(name, true)
            .map { Integer.valueOf(it.trim()) }
            .toMutableList()
        var i = 0
        var r = 0
        while (i in list.indices) {
            val steps = list[i]
            list[i] = steps + 1
            i += steps
            r++
        }
        return r
    }

    override fun executePart2(name: String): Any {
        val list = getInputAsLines(name, true)
            .map { Integer.valueOf(it.trim()) }
            .toMutableList()
        var i = 0
        var r = 0
        while (i in list.indices) {
            val steps = list[i]
            if (steps >= 3) {
                list[i] = steps - 1
            } else {
                list[i] = steps + 1

            }
            i += steps
            r++
        }
        return r
    }
}