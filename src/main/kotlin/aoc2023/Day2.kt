package aoc2023

import getInputAsLines
import util.Day

class Day2: Day("2") {

    private fun transformLine(line: String): Map<String, Int> {
        val maps =  line.split(":")[1].split("; ")
            .map { it.trim().split(", ") }
            .map { it.map { x -> x.split(" ") } }
            .map { drawing -> drawing.associate { entry -> entry[1].trim() to entry[0].trim().toInt() } }
        return listOf("blue", "red", "green").associateWith { color ->  maps.maxOf { it.getOrDefault(color, 0) } }
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name)
            .asSequence()
            .filter { it.isNotBlank() }
            .map { transformLine(it) }
            .mapIndexed { index, m ->  if (m["red"]!! <= 12 && m["green"]!! <= 13 && m["blue"]!! <= 14) index+1 else 0 }
            .sum()
    }

    override fun executePart2(name: String): Any {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .sumOf { transformLine(it).values.reduce { acc, i -> acc * i }.toLong() }
    }
}