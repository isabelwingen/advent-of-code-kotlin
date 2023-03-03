package aoc2019

import aoc2019.util.IntCode
import util.Day

class Day19: Day("19") {

    fun testCoordinates(pair: Pair<Int, Int>): Int {
        return IntCode("Day19", "2019/day19.txt")
            .execute(pair.toList().map { it.toLong() })
            .toInt()
    }

    override fun executePart1(name: String): Long {
        var sum = 0L
        for (x in 0 until 50) {
            for (y in 0 until 50) {
                val prog = testCoordinates(x to y)
                sum += prog
            }
        }
        return sum
    }


    override fun executePart2(name: String): Long {
        val left = hashMapOf(5 to 4)
        var i = 5
        while (true) {
            // get left border
            var j = 0
            while (true) {
                val testCoordinate = left[i]!!+j to i+1
                if (testCoordinates(testCoordinate) == 1) {
                    left[i+1] = left[i]!!+j
                    break
                }
                j++
            }
            if (testCoordinates(left[i]!!+99 to i) == 1) {
                // bottom row would fit.
                // bottom left corner is at y=i, x=left[i]
                // check if top right corner would also fit
                // top right corner would be at y=i-99, x=left[i]+99
                if (testCoordinates(left[i]!!+99 to i-99) == 1) {
                    break
                }
            }
            i++
        }
        return (i-99)+left[i]!!*10_000L
    }

}