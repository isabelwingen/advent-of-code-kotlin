package aoc2020

import getResourceAsList

private fun parseInput(name: String = "day9.txt"): List<Long> {
    return getResourceAsList(name)
        .filter { it.isNotBlank() }
        .map { it.toLong() }
}

fun isSumOfPair(list: List<Long>, index: Int): Boolean {
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

fun executeDay9Part1(name: String = "day9.txt"): Long {
    val numbers = parseInput(name)
    for (i in 25 until numbers.size) {
        if (!isSumOfPair(numbers, i)) {
            return numbers[i]
        }
    }
    return -1
}

private fun findWeaknessIndex(numbers: List<Long>): Long {
    for (i in 25 until numbers.size) {
        if (!isSumOfPair(numbers, i)) {
            return numbers[i]
        }
    }
    return -1
}

fun executeDay9Part2(name: String = "day9.txt"): Long {
    val numbers = parseInput(name)
    val goal = findWeaknessIndex(numbers)
    for (i in numbers.indices) {
        var currentSum = numbers[i]
        var j = i + 1
        while (currentSum < goal) {
            currentSum += numbers[j]
            if (currentSum == goal) {
                val min = numbers.subList(i, j + 1).minOrNull()
                val max = numbers.subList(i, j + 1).maxOrNull()
                return min!! + max!!
            }
            j += 1
        }
    }
    return -1
}

