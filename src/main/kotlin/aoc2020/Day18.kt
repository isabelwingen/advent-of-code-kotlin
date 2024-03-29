package aoc2020

import asLong
import getInputAsLines
import util.Day

class Day18: Day("18") {
    private fun parseInput(name: String): List<String> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.replace(" ", "") }
    }

    private fun solvePart1(equation: CharIterator): Long {
        val numbers = mutableListOf<Long>()
        var op = '+'
        while (equation.hasNext()) {
            when (val next = equation.nextChar()) {
                '(' -> numbers += solvePart1(equation)
                ')' -> break
                in setOf('+', '*') -> op = next
                else -> numbers += next.asLong()
            }
            if (numbers.size == 2) {
                val a = numbers.removeLast()
                val b = numbers.removeLast()
                numbers += if (op == '+') a + b else a * b
            }
        }
        return numbers.first()
    }

    private fun solvePart2(equation: CharIterator): Long {
        val multiplyThese = mutableListOf<Long>()
        var added = 0L
        while (equation.hasNext()) {
            when (val next = equation.nextChar()) {
                '(' -> added += solvePart2(equation)
                ')' -> break
                '*' -> {
                    multiplyThese += added
                    added = 0L
                }
                in setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') -> added += next.asLong()
            }
        }
        return (multiplyThese + added).reduceRight { a, b -> a * b }
    }

    override fun executePart1(name: String): Long {
        return parseInput(name)
            .map { it.toCharArray().iterator() }
            .sumOf { solvePart1(it) }
    }

    override fun executePart2(name: String): Long {
        return parseInput(name)
            .map { it.toCharArray().iterator() }
            .sumOf { solvePart2(it) }
    }

}