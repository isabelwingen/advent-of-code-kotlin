package aoc2020

import getInputAsLines
import splitBy
import util.Day

class Day6: Day("6") {

    private fun parseInput(path: String, connect: (a: List<String>) -> Set<Char>): List<Set<Char>> {
        return getInputAsLines(path)
            .splitBy { it.isBlank() }
            .filter { line -> line.all { it.isNotBlank() } }
            .map { group -> connect(group) }
    }

    private fun intersectGroup(lines: List<String>): Set<Char> {
        return lines
            .map { it.toSet() }
            .reduceRight { a, b -> a.intersect(b) }
    }

    private fun joinGroup(lines: List<String>): Set<Char> {
        return lines
            .map { it.toSet() }
            .reduceRight { a, b -> a.union(b) }
    }

    override fun executePart1(name: String): Long {
        return parseInput(name) { joinGroup(it) }
            .map { it.count() }
            .reduceRight { a, b -> a + b }
            .toLong()
    }

    override fun executePart2(name: String): Long {
        return parseInput(name) { intersectGroup(it) }
            .map { it.count() }
            .reduceRight { a, b -> a + b }
            .toLong()
    }

}