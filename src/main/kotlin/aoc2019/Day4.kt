package aoc2019

import getInput
import splitBy
import util.Day

class Day4: Day("4") {

    private fun parseInput(name: String): Pair<Int, Int> {
        val coll = getInput(name)
            .trim()
            .split("-")
            .map { it.toInt() }
        return coll[0] to coll[1]
    }

    private fun isValidPassword(password: Int): Boolean {
        val windowed = password.toString().map { it.toString().toInt() }.windowed(2, 1)
        return windowed.any { it[0] == it[1] } && windowed.none { it[0] > it[1] }
    }

    private fun isValidPassword2(password: Int): Boolean {
        val windowed = password.toString().map { it.toString().toInt() }.windowed(2, 1)
        val cond1 = windowed.none { it[0] > it[1] }
        if (!cond1) {
            return false
        }
        val pairCandidates = windowed.filter { it[0] == it[1] }.map { it.first() }
        if (pairCandidates.isEmpty()) {
            return false
        }
        val illegalNumbers = password.toString().toList().splitBy { it }
            .filter { it.size >= 3 }
            .map { it.first().toString().toInt() }
        if (pairCandidates.all { illegalNumbers.contains(it) }) {
            return false
        }
        return true
    }

    override fun executePart1(name: String): Long {
        val (lower, upper) = parseInput(name)
        var res = 0L
        var number = lower
        while (number <= upper) {
            if (isValidPassword(number)) {
                res++
            }
            number++
        }
        return res
    }

    override fun executePart2(name: String): Long {
        val (lower, upper) = parseInput(name)
        var res = 0L
        var number = lower
        while (number <= upper) {
            if (isValidPassword2(number)) {
                res++
            }
            number++
        }
        return res
    }

}
