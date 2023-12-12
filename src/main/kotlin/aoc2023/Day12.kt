package aoc2023

import getInputAsLines
import splitBy
import util.Day
import java.util.*
import kotlin.math.min

class Day12: Day("12") {

    data class Line(val pattern: List<Char>, val numbers: List<Int>) {

        fun valid(): Boolean {
            return if (pattern.contains('?')) {
                false
            } else {
                pattern.joinToString("").split(".").filter { it.isNotBlank() }.map { it.length }.toList() == numbers
            }
        }
    }

    data class Line2(val groups: List<String>, val numbers: List<Int>)

    fun parseLine(line: String): Line {
        val (part1, part2) = line.split(" ")
        val numbers = part2.split(",").map { it.trim().toInt() }.toList()
        val pattern = part1.trim().toCharArray().toList()
        return Line(pattern, numbers)
    }

    fun parseLinePart2(line: String): Line2 {
        val (pattern, numbers) = parseLine(line)
        val p = pattern + listOf('?')
        val l = (p + p + p + p + p).dropLast(1).splitBy { c -> c == '.' }.filterNot { x -> x.all { c -> c == '.' } }.map { it.joinToString("") }
        return Line2(l, numbers + numbers + numbers + numbers + numbers)
    }

    private fun findLastPossiblePosition(line: Line): Int {
        val paddingRight = line.numbers.drop(1).let { it.sum() + it.size }
        return line.pattern.size - paddingRight - line.numbers.first()
    }

    private fun isPossible(line: Line, i: Int): Line? {
        val newPattern = line.pattern.toMutableList()
        for (k in 0 .. min((i + line.numbers.first()), line.pattern.lastIndex)) {

            val char = line.pattern[k]
            if (k < i-1) {
                if (char == '?' || char == '.') {
                    newPattern[k] = '.'
                } else {
                    return null
                }
            } else if (k == i-1 || k == i + line.numbers.first()) {
               if (char == '#') {
                   return null
               } else {
                   newPattern[k] = '.'
               }
            } else if (k in i until i + line.numbers.first()) {
                if (char != '?' && char != '#') {
                    return null
                } else {
                    newPattern[k] = '#'
                }
            }
        }
        return Line(newPattern.drop(i + line.numbers.first() + 1), line.numbers.drop(1))
    }


    fun findAllCombinations(line: Line): Int {
        val queue = LinkedList<Line>()
        queue.add(line)
        var count = 0
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.numbers.isEmpty()) {
                if (current.pattern.none { it == '#' }) {
                    count++
                }
            } else {
                (0..findLastPossiblePosition(current))
                    .mapNotNull { isPossible(current, it) }
                    .forEach { queue.addLast(it) }
            }
        }
        return count
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)
            .map { parseLine(it) }
            .sumOf { findAllCombinations(it).toLong() }
    }

    private fun findAllCombinations2(line: Line2): Int {
        val queue = LinkedList<Line2>()
        queue.add(line)
        var count = 0
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.numbers.isEmpty()) {
                if (current.groups.none { s -> s.any { c -> c == '#' } }) {
                    count++
                }
            } else if (current.groups.isNotEmpty()) {
                val firstGroup = current.groups.first()
                val firstNumber = current.numbers.first()
                if (firstGroup.length < firstNumber) {
                    queue.add(Line2(current.groups.drop(1), current.numbers))
                } else {
                    val possiblePositions = 0 .. (firstGroup.length - firstNumber)
                    possiblePositions.forEach { i ->
                        val newFirstGroup = firstGroup.drop(i + firstNumber)
                        if (newFirstGroup.isEmpty()) {
                            queue.add(Line2(current.groups.drop(1), current.numbers.drop(1)))
                        } else {
                            queue.add(Line2(listOf(newFirstGroup) + current.groups.drop(1), current.numbers.drop(1)))
                        }
                    }
                }
            }
        }
        return count
    }

    override fun executePart2(name: String): Any {
        return getInputAsLines(name, true)
            .asSequence()
            .map { parseLinePart2(it) }
            .toList()
    }
}