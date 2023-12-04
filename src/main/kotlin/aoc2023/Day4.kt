package aoc2023

import getInputAsLines
import util.Day
import kotlin.math.pow

class Day4: Day("4") {

    private fun parseLine(line: String): List<List<Int>> {
        val parsePart = fun(part: String) = part.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
        return line.split("|").map(parsePart)
    }

    private fun parseInput(name: String): Sequence<Int> {
        val countMatchingNumbers = fun(list: List<List<Int>>) = list[1].count { list[0].contains(it) }
        return getInputAsLines(name, true)
            .asSequence()
            .map { it.split(":")[1] }
            .map(::parseLine)
            .map(countMatchingNumbers)
    }

    override fun executePart1(name: String): Any {
        return parseInput(name)
            .filterNot { it == 0 }
            .sumOf { 2.0.pow(it - 1) }
            .toLong()
    }

    override fun executePart2(name: String): Any {
        val cards = parseInput(name).toList()
        val cardCount = IntArray(cards.size) { 1 }
        cards.forEachIndexed { i, matchingNumbers ->
            (i + 1 .. i + matchingNumbers)
                .filter { cardCount.indices.contains(it) }
                .forEach { cardCount[it] += cardCount[i] }
        }
        return cardCount.sum()
    }
}