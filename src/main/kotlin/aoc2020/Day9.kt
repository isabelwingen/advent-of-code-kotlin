package aoc2020

import getInputAsLines
import util.Day

class Day9: Day("9") {
    private fun parseInput(name: String): LongArray {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .toLongArray()
    }

    private fun isSumOfPair(list: LongArray, index: Int): Boolean {
        val idMin = index - 25
        for (i in idMin until index) {
            for (j in i + 1 until index) {
                if (list[i] + list[j] == list[index]) {
                    return true
                }
            }
        }
        return false
    }

    override fun executePart1(name: String): Long {
        val numbers = parseInput(name)
        for (i in 25 until numbers.size) {
            if (!isSumOfPair(numbers, i)) {
                return numbers[i]
            }
        }
        return -1
    }

    override fun expectedResultPart1() = 133015568L

    private fun findWeaknessIndex(numbers: LongArray): Long {
        for (i in 25 until numbers.size) {
            if (!isSumOfPair(numbers, i)) {
                return numbers[i]
            }
        }
        return -1
    }

    override fun executePart2(name: String): Long {
        val numbers = parseInput(name)
        val goal = findWeaknessIndex(numbers)
        for (i in numbers.indices) {
            var currentSum = numbers[i]
            var j = i + 1
            while (currentSum < goal) {
                currentSum += numbers[j]
                if (currentSum == goal) {
                    val sublist = numbers.toList().subList(i, j + 1)
                    return sublist.minOrNull()!! + sublist.maxOrNull()!!
                }
                j += 1
            }
        }
        return -1
    }

    override fun expectedResultPart2() = 16107959L
}

