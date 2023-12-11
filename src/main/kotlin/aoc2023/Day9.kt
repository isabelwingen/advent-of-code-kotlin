package aoc2023

import getInputAsLines
import util.Day

class Day9: Day("9") {

    private fun process(line: List<Int>): List<List<Int>> {
        val lines = mutableListOf(line)
        while (!lines.last().all { it == 0 }) {
            lines.add(lines.last().windowed(2, 1).map { (a, b) -> b-a })
        }
        return lines.filter { it.isNotEmpty() }
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)
            .map { line -> line.split(" ").filterNot { it.isBlank() }.map { it.toInt() } }
            .sumOf { process(it).sumOf { l -> l.last() } }
    }

    override fun executePart2(name: String): Any {
        val lines = getInputAsLines(name, true)
            .map { line -> line.split(" ").filterNot { it.isBlank() }.map { it.toInt() } }
            .map { process(it) }
        var sum = 0L
        for (line in lines) {
            sum += line.mapIndexed { i, v -> v.first() * if (i.mod(2) == 0) 1L else -1L }.sum()
        }
        return sum
    }
}