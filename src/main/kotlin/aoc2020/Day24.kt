package aoc2020

import aoc2020.util.GameOfLife
import getInputAsLines
import util.Day

class Day24 : Day("24") {
    private fun parseLine(line: String): List<String> {
        var i = 0
        val res = mutableListOf<String>()
        while (i < line.length) {
            val currentChar = line[i]
            val nextChar = if (i < line.length - 1) line[i + 1] else null
            if (currentChar == 'n' || currentChar == 's') {
                res.add(currentChar.toString() + nextChar.toString())
                i++
            } else {
                res.add(currentChar.toString())
            }
            i++
        }
        assert(line == res.joinToString(""))
        return res.toList()
    }

    override fun executePart1(name: String): Long {
        return getInputAsLines(name)
            .asSequence()
            .filter { it.isNotBlank() }
            .map { parseLine(it) }
            .map { x -> x.groupBy { it }.mapValues { it.value.count() } }
            .map { transformHexTileToCoordinate(it) }
            .groupBy { it }
            .mapValues { it.value.count() }
            .filter { it.value % 2 == 1 }
            .count()
            .toLong()
    }

    private fun getNeighbours(coord: HexCoordinate): Set<HexCoordinate> {
        return listOf(
            HexCoordinate(-1, 0, +1),
            HexCoordinate(0, -1, +1),
            HexCoordinate(+1, -1, 0),
            HexCoordinate(+1, 0, -1),
            HexCoordinate(0, +1, -1),
            HexCoordinate(-1, +1, 0))
            .map { coord.add(it) }
            .toSet()
    }

    private data class HexCoordinate(val q: Int = 0, val r: Int = 0, val s: Int = 0) {
        fun add(a: HexCoordinate): HexCoordinate {
            return HexCoordinate(q + a.q, r + a.r, s + a.s)
        }
    }

    private fun transformHexTileToCoordinate(path: Map<String, Int>): HexCoordinate {
        var q = 0
        var r = 0
        var s = 0
        for ((dir, times) in path) {
            when (dir) {
                "w" -> {
                    q -= times
                    s += times
                }
                "nw" -> {
                    s += times
                    r -= times
                }
                "ne" -> {
                    q += times
                    r -= times
                }
                "e" -> {
                    q += times
                    s -= times
                }
                "se" -> {
                    r += times
                    s -= times
                }
                "sw" -> {
                    q -= times
                    r += times
                }
            }
        }
        return HexCoordinate(s = s, q = q, r = r)
    }

    override fun executePart2(name: String): Long {
        val initialState = getInputAsLines(name)
            .asSequence()
            .filter { it.isNotBlank() }
            .map<String, List<String>> { parseLine(it) }
            .map { x -> x.groupBy { it }.mapValues { it.value.count() } }
            .map { transformHexTileToCoordinate(it) }
            .groupBy { it }
            .mapValues { it.value.count() }
            .filter { it.value % 2 == 1 }
            .map { it.key }
            .toSet()
        val gameOfLife = GameOfLife(
            initialState,
            null,
            getNeighbours = { getNeighbours(it) },
            comeAlive = { neighbours: Set<HexCoordinate>, alive: Set<HexCoordinate> ->
                val aliveNeighbours = neighbours.count { alive.contains(it) }
                aliveNeighbours == 2
            },
            die = { neighbours: Set<HexCoordinate>, alive: Set<HexCoordinate> ->
                val aliveNeighbours = neighbours.count { alive.contains(it) }
                aliveNeighbours == 0 || aliveNeighbours > 2
            })
        repeat(100) { gameOfLife.step() }
        return gameOfLife.getLivingCells().count().toLong()
    }

}