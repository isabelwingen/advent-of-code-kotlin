package aoc2019

import aoc2019.util.IntCode
import util.Day

class Day19: Day("19") {
    override fun executePart1(name: String): Int {
        var sum = 0
        for (x in 0 until 50) {
            for (y in 0 until 50) {
                val prog = IntCode("Day19", name).execute(listOf(x.toLong(), y.toLong())).toInt()
                if (x == 1 && prog == 1) {
                    println("$x, $y: $prog")
                }
                sum += prog
            }
        }
        return sum
    }

    override fun expectedResultPart1() = 226

    fun IntRange.intersect(other: IntRange): IntRange {
        val intersectSet = this.toSet().intersect(other)
        if (intersectSet.isEmpty()) {
            return IntRange(0, 0)
        } else {
            return IntRange(intersectSet.minOf { it }, intersectSet.maxOf { it })
        }
    }

    fun IntRange.size(): Int {
        return this.toList().size;
    }

    override fun executePart2(name: String): Any {
        val MAX = 1000
        val ranges = HashMap<Int, IntRange>()
        for (x in 0 until MAX) {
            val range = ranges.getOrPut(x) { findRange(x, name) }
            if (range.size() < 100) {
                continue
            }
            val goal = x + 99
            val otherRange = ranges.getOrPut(goal) { findRange(goal, name) }
            val intersection = range.intersect(otherRange)
            if (intersection.size() >= 100) {
                return x * 10_000 + intersection.first
            }
        }
        return false
    }

    private fun findRange(x: Int, name: String): IntRange {
        val start = findStart(x, 0, 1_000, name)
        if (start == -1) {
            return IntRange(0, 0)
        } else {
            return IntRange(start, findEnd(x, start, 1_200, name));
        }
    }

    private fun findStart(x: Int, start: Int, end: Int, name: String): Int {
        for (y in start until end + 1) {
            val res = IntCode("", name).execute(listOf(x.toLong(), y.toLong())).toInt()
            if (res == 1) {
                return y
            }
        }
        return -1
    }

    private fun findEnd(x: Int, start: Int, end: Int, name: String): Int {
        for (y in start until end + 1) {
            val res = IntCode("", name).execute(listOf(x.toLong(), y.toLong())).toInt()
            if (res == 0) {
                return y - 1
            }
        }
        return -1
    }

    override fun expectedResultPart2() = 7900946
}