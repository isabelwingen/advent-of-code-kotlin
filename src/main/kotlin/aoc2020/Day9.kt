package aoc2020

import getResourceAsList

private fun parseInput11(name: String = "day9.txt"): List<Long> {
    return getResourceAsList(name)
        .filter { it.isNotBlank() }
        .map { it.toLong() }
}

private fun isSumOfPair(list: List<Long>, index: Int): Boolean {
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
    val numbers = parseInput11(name)
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
    val numbers = parseInput11(name)
    val goal = findWeaknessIndex(numbers)
    for (i in numbers.indices) {
        var currentSum = numbers[i]
        var j = i + 1
        while (currentSum < goal) {
            currentSum += numbers[j]
            if (currentSum == goal) {
                return numbers.subList(i, j + 1).minOrNull()!! + numbers.subList(i, j + 1).maxOrNull()!!
            }
            j += 1
        }
    }
    return -1
}

