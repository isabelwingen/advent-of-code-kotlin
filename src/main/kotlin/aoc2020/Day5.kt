package aoc2020

import getInputAsLines
import util.Day

class Day5: Day("5") {
    private fun parseSeat(seat: String): Int {
        val row = seat
            .substring(0, 7)
            .map { if (it == 'F') 0 else 1 }
            .map { it.toString() }
            .joinToString(separator = "")
            .toInt(radix = 2)
        val column = seat
            .substring(7, 10)
            .map { if (it == 'L') 0 else 1 }
            .map { it.toString() }
            .joinToString(separator = "")
            .toInt(radix = 2)
        return row * 8 + column
    }

    override fun executePart1(name: String): Int {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .maxOf { parseSeat(it) }
    }

    override fun expectedResultPart1() = 959

    override fun executePart2(name: String): Any {
        val coll = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { parseSeat(it) }
            .sorted()
        val minSeat = coll[0]
        for (i in coll.indices) {
            if (coll[i] != minSeat + i) {
                return coll[i] - 1
            }
        }
        return -1
    }

    override fun expectedResultPart2() = 527
}