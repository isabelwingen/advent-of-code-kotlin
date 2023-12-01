package aoc2023

import getInputAsLines
import util.Day

class Day1: Day("1") {

    private fun findFirstAndLastDigit(line: String): Int {
        val digits = line.toCharArray().filter { it.isDigit() }.map { it.toString().toInt() }
        return digits.first() * 10 + digits.last()
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .sumOf { findFirstAndLastDigit(it) }
    }

    private fun mapToInt(string: String): Int {
        return if (string.length == 1 && string.first().isDigit()) {
            string.toInt()
        } else {
            when(string) {
                "one" -> 1
                "two" -> 2
                "three" -> 3
                "four" -> 4
                "five" -> 5
                "six" -> 6
                "seven" -> 7
                "eight" -> 8
                "nine" -> 9
                else -> throw IllegalStateException()
            }
        }
    }

    private fun findFirstAndLastDigit2(line: String): Int {
        val l = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "1", "2", "3", "4", "5", "6", "7", "8", "9")
            .filter { line.contains(it) }
        val firstDigit = l.sortedBy { line.indexOf(it) }.first()
        val lastIndices = mutableMapOf<String, Int>()
        for (digit in l) {
            var lastIndex = line.indexOf(digit)
            var nextIndex = line.indexOf(digit, lastIndex+1)
            while (nextIndex != -1) {
                lastIndex = nextIndex
                nextIndex = line.indexOf(digit, lastIndex+1)
            }
            lastIndices[digit] = lastIndex
        }
        val lastDigit = lastIndices.entries.maxByOrNull { it.value }!!.key
        return mapToInt(firstDigit)*10 + mapToInt(lastDigit)
    }

    override fun executePart2(name: String): Any {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .sumOf { findFirstAndLastDigit2(it) }
    }
}