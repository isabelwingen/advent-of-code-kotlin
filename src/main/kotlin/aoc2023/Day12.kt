package aoc2023

import getInputAsLines
import splitBy
import util.Day
import java.util.*
import kotlin.math.min

class Day12: Day("12") {

    data class Group(val start: Int, val text: String, val length: Int = text.length)

    data class Row(val groups: List<Group>, val numbers: List<Int>, val original: String) {

        fun toPart2(): Row {
            return parse("$original?$original?$original?$original?$original ${(numbers + numbers + numbers + numbers + numbers).joinToString(",")}")
        }

        companion object {
            fun parse(line: String): Row {
                val (left, right) = line.split(" ")
                val original = left.trim()
                val numbers = right.trim().split(",").filter { it.isNotBlank() }.map { it.toInt() }
                val groups = mutableListOf<Group>()
                var i = 0
                var list = original.toCharArray().toList()
                while (list.isNotEmpty()) {
                   if (list.first() == '.') {
                       list = list.drop(1)
                       i++
                   } else {
                       val p = list.takeWhile { it != '.' }.joinToString("")
                       groups.add(Group(i, p))
                       list = list.drop(p.length).toList()
                       i += p.length
                   }
                }

                return Row(groups.toList(), numbers, original)
            }
        }


    }

    val cache = mutableMapOf<Pair<String, Int>, List<Int>>()

    private fun findPossiblePositions(string: String, blockLength: Int): List<Int> {
        cache.putIfAbsent(
            string to blockLength,
            (0 .. (string.length - blockLength))
                .filter { string.getOrElse(it-1) { '?' } == '?' && string.getOrElse(it + blockLength) { '?' } == '?' }
                .toList())
        return cache[string to blockLength]!!
    }

    private fun findPossiblePositions(groups: List<Group>, blockLength: Int): List<Int> {
        return groups.flatMap { g -> findPossiblePositions(g.text, blockLength).map { it + g.start } }
    }

    data class Node(val blockLength: Int, val position: Int, val children: List<Node> = emptyList())

    fun combinations(row: Row): Int {
        val combMap = row.numbers.toSet().associateWith { findPossiblePositions(row.groups, it) }
        val possibleCombinations = row.numbers.map { combMap[it]!!.toMutableList() }
        // Hinweg
        var firstPossiblePosition = 0
        for (i in row.numbers.indices) {
            possibleCombinations[i].removeIf { it < firstPossiblePosition }
            firstPossiblePosition = possibleCombinations[i].first() + row.numbers[i] + 1
        }
        //Rückweg
        var lastPossiblePosition = row.original.lastIndex
        for (i in row.numbers.indices.reversed()) {
            possibleCombinations[i].removeIf { it > lastPossiblePosition }
            lastPossiblePosition = possibleCombinations[i].last() - 1 - row.numbers.getOrElse(i-1) { 0 }
        }
        val seen = mutableSetOf<List<Int>>()
        val queue = LinkedList<List<Int>>()
        queue.add(emptyList())
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val indices = current.mapIndexed { index, start -> start until start + row.numbers[index] }
            if (current.isNotEmpty() && row.original.substring(0..current.last()).filterIndexed { index, _ ->  indices.none { it.contains(index) }}.any { it == '#' }) {
                // can't do
            } else if (current.size == row.numbers.size) {
                seen.add(current)
            } else {
                val nextIndex = current.size
                val p = if (nextIndex == 0) {
                    possibleCombinations[nextIndex]
                } else {
                    possibleCombinations[nextIndex].filter { it > current.last() + row.numbers[nextIndex-1] }
                }
                p.forEach { queue.add(current + it) }
            }
        }
        return seen.count()
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)
            .map { Row.parse(it) }
            .sumOf { combinations(it) }

    }


    override fun executePart2(name: String): Any {
        return getInputAsLines(name, true)
            .map { Row.parse(it).toPart2() }
            .take(1)
            .sumOf { combinations(it) }
    }

}