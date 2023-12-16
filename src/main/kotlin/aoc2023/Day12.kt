package aoc2023

import getInputAsLines
import util.Day

class Day12: Day("12") {

    private val cache = hashMapOf<String, HashMap<List<Int>, Long>>()

    fun count(s: String, n: List<Int>): Long {
        val func = fun(string: String, numbers: List<Int>): Long {
            if (string.isEmpty() || string.all { it == '.' }) {
                return if (numbers.isEmpty()) {
                    1
                } else {
                    0
                }
            } else if (numbers.isEmpty()) {
                return if (string.none { it == '#' }) {
                    1
                } else {
                    0
                }
            } else {
                val firstNumber = numbers.first()
                return if (firstNumber > string.length) {
                    0
                } else {
                    (0..string.length - firstNumber).sumOf { i ->
                        if (string.substring(0 until i).none { it == '#' } && string.getOrElse(i + firstNumber) { '?' } != '#' && string.substring(i until i + firstNumber).none { it == '.' }) {
                            count(string.drop(i + firstNumber + 1), numbers.drop(1))
                        } else {
                            0
                        }
                    }
                }
            }
        }
        cache.putIfAbsent(s, hashMapOf())
        return cache.getValue(s).getOrPut(n) { func(s, n) }
    }

    private fun parseLine(line: String): Pair<String, List<Int>> {
        val (a, b) = line.split(" ")
        return a.split(".").filterNot { it.isEmpty() }.joinToString(".") to b.split(",").map { it.toString().toInt() }
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