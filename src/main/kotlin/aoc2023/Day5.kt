package aoc2023

import getInputAsLines
import splitBy
import util.Day

class Day5: Day("5") {

    data class Line(val destinationStart: Long, val sourceStart: Long, val length: Int) {
        fun transform(a: Long): Pair<Boolean, Long> {
            return if (a in sourceStart until (sourceStart + length)) {
                true to (destinationStart + a - sourceStart)
            } else {
                false to a
            }
        }

        fun back(a: Long): Pair<Boolean, Long> {
            return if (a in destinationStart until (destinationStart + length)) {
                true to (sourceStart + a - destinationStart)
            } else {
                false to a
            }
        }
    }

    data class PlantMap(val title: String, val lines: List<Line>) {
        fun findResult(a: Long): Long {
            return lines.fold(a) { acc, line ->
                val (b, r) = line.transform(acc)
                if (b) {
                    return r
                } else {
                    r
                }
            }
        }

        fun back(a: Long): Long {
            return lines.fold(a) { acc, line ->
                val (b, r) = line.back(a)
                if (b) {
                    return r
                } else {
                    r
                }
            }
        }
    }

    private fun parseMapLine(line: String): Line {
        return line.split(" ")
            .map { it.trim().toLong() }
            .let { (a, b, c) ->
                Line(a, b, c.toInt())
            }
    }

    private fun parse(lines: List<String>): Pair<List<Long>, List<PlantMap>> {
        val seeds = lines[0].split(":")[1].split(" ").filterNot { it.isBlank() }.map { it.trim().toLong() }
        val maps = lines.drop(1).splitBy { it.isBlank() }
            .filter { it.isNotEmpty() }
            .filter { it[0].isNotEmpty() }
            .map { it[0] to it.drop(1).map(::parseMapLine) }
            .map { PlantMap(it.first, it.second) }
        return seeds to maps
    }

    override fun executePart1(name: String): Any {
        val lines = getInputAsLines(name, false)
        val (seeds, maps) = parse(lines)
        return seeds.minOf { seed ->
            maps.fold(seed) { acc, m ->
                m.findResult(acc)
            }
        }
    }

    override fun executePart2(name: String): Any {
        val lines = getInputAsLines(name, false)
        val (s, maps) = parse(lines)

        val seedRanges = s.chunked(2)
            .map { it[0] until it[0] + it[1] }
        var i = 0L
        while (i < 10_000_000) {
            val result = maps.reversed().fold(i) { acc, m ->
                m.back(acc)
            }
            if (seedRanges.any { it.contains(result) }) {
                return i
            }
            i++
        }
        return -1
    }
}