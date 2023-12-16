package aoc2023

import getInputAsLines
import util.Day

class Day12: Day("12") {

    private val cache = hashMapOf<String, HashMap<List<Int>, Long>>()

    private fun validPosition(string: String, startOfBlock: Int, lengthOfBlock: Int): Boolean {
        return string.substring(0 until startOfBlock).none { it == '#' } &&
                string.getOrElse(startOfBlock + lengthOfBlock) { '?' } != '#' &&
                string.substring(startOfBlock until startOfBlock + lengthOfBlock).none { it == '.' }
    }

    fun count(s: String, n: List<Int>): Long {
        val func = fun(string: String, numbers: List<Int>) =
            if (string.isEmpty() || string.all { it == '.' }) {
                if (numbers.isEmpty()) 1L else 0L
            } else if (numbers.isEmpty()) {
                if (string.none { it == '#' }) 1L else 0L
            } else {
                val firstNumber = numbers.first()
                (0 .. string.length - firstNumber)
                    .filter { validPosition(string, it, firstNumber) }
                    .sumOf { count(string.drop(it + firstNumber + 1), numbers.drop(1)) }
            }

        cache.putIfAbsent(s, hashMapOf())
        return cache.getValue(s).getOrPut(n) { func(s, n) }
    }

    private fun parseLine(line: String): Pair<String, List<Int>> {
        val (a, b) = line.split(" ")
        val string = a.split(".").filterNot { it.isEmpty() }.joinToString(".")
        val numbers = b.split(",").map { it.toInt() }
        return string to numbers
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)
            .map { parseLine(it) }
            .sumOf { count(it.first, it.second) }
    }

    private fun parseLinePart2(line: String): Pair<String, List<Int>> {
        val (a, b) = line.split(" ")
        val n = b.split(",").map { it.toInt() }
        val c = "$a?$a?$a?$a?$a".split(".").filterNot { it.isEmpty() }.joinToString(".")
        return c to (n + n + n + n + n)
    }

    override fun executePart2(name: String): Any {
        return getInputAsLines(name, true)
            .map { parseLinePart2(it) }
            .sumOf { count(it.first, it.second) }
    }
}