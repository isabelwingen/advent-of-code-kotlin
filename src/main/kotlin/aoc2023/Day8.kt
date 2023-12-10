package aoc2023

import getInputAsLines
import splitBy
import util.Day

class Day8: Day("8") {

    private fun parseLine(line: String): Pair<String, Map<Char, String>> {
        val (name, children) = line.split(" = ")
        val (left, right) = children.drop(1).dropLast(1).split(", ")
        return name to mapOf('L' to left, 'R' to right)
    }

    private fun buildMap(lines: List<String>): Map<String, Map<Char, String>> {
        return lines.associate { parseLine(it) }
    }

    private fun runFromStartToEnd(nodeMap: Map<String, Map<Char, String>>, instructions: String): (String, String) -> Long {
        return fun(start: String, end: String): Long {
            val seen = mutableSetOf<Pair<String, Int>>()
            var currentNodeKey = start
            var index = 0
            var c = 0L
            do {
                val instruction = instructions[index]
                (currentNodeKey to index).let {
                    if (seen.contains(it)) {
                        return -1
                    } else {
                        seen.add(it)
                    }
                }
                currentNodeKey = nodeMap.getValue(currentNodeKey).getValue(instruction)
                if (index < instructions.lastIndex) {
                    index += 1
                } else {
                    index = 0
                }
                c++
            } while (currentNodeKey != end)
            return c
        }
    }

    override fun executePart1(name: String): Any {
        val (i, _, nodes, _) = getInputAsLines(name).splitBy { it.isBlank() }
        val instructions = i[0]
        val nodeMap = buildMap(nodes)

        return runFromStartToEnd(nodeMap, instructions)("AAA", "ZZZ")
    }

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    override fun executePart2(name: String): Any {
        val (i, _, nodes, _) = getInputAsLines(name).splitBy { it.isBlank() }
        val instructions = i[0]
        val nodeMap = buildMap(nodes)
        val runFunc = runFromStartToEnd(nodeMap, instructions)

        val startNodes = nodeMap.keys.filter { it.endsWith("A") }
        val endNodes = nodeMap.keys.filter { it.endsWith("Z") }

        return startNodes.associate { s ->
            endNodes
                .map { e ->
                    e to runFunc(s, e)
                }
                .filterNot { it.second == -1L }
                .also { if (it.size != 1) throw IllegalStateException("More than one goal found") }
                .first()
        }.onEach { (key, value) ->
            if (value != runFunc(key, key)) {
                throw java.lang.IllegalStateException("Cycle Lengths not equal")
            }
        }.values.reduce { a, b -> findLCM(a, b) }
    }
}