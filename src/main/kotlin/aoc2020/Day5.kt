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

    override fun executePart1(name: String): Long {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .maxOf { parseSeat(it) }
            .toLong()
    }

    override fun executePart2(name: String): Long {
        val coll = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { parseSeat(it) }
            .sorted()
        val minSeat = coll[0]
        for (i in coll.indices) {
            if (coll[i] != minSeat + i) {
                return coll[i] - 1L
            }
        }
        return -1
    }

}