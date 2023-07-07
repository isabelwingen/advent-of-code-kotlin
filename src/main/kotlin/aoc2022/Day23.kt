package aoc2022

import getInputAsLines
import util.Day
import java.util.LinkedList
import java.util.SortedSet
import kotlin.math.abs

class Day23: Day("23") {

    private fun getInput(name: String): List<CharArray> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.toCharArray() }
    }

    private fun getElvesPosition(name: String): Set<Pair<Int, Int>> {
        val lines = getInput(name)
        val res = mutableSetOf<Pair<Int, Int>>()
        for (row in lines.indices) {
            for (col in lines[0].indices) {
                if (lines[row][col] == '#') {
                    res.add(row to col)
                }
            }
        }
        return res.toSet()
    }

    private fun north_neighbours(pos: Pair<Int, Int>) =
        setOf(pos.first-1 to pos.second-1, pos.first-1 to pos.second, pos.first-1 to pos.second+1)

    private fun south_neighbours(pos: Pair<Int, Int>) =
        setOf(pos.first+1 to pos.second-1, pos.first+1 to pos.second, pos.first+1 to pos.second+1)

    private fun west_neighbours(pos: Pair<Int, Int>) =
        setOf(pos.first-1 to pos.second-1, pos.first to pos.second-1, pos.first+1 to pos.second-1)

    private fun east_neighbours(pos: Pair<Int, Int>) =
        setOf(pos.first-1 to pos.second+1, pos.first to pos.second+1, pos.first+1 to pos.second+1)

    private fun neighbours(pos: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val (row, col) = pos
        return setOf(
            row-1 to col-1,
            row-1 to col,
            row-1 to col+1,
            row to col-1,
            row to col+1,
            row+1 to col-1,
            row+1 to col,
            row+1 to col+1,
        )
    }

    val directionMap = mapOf(
        'n' to { p: Pair<Int, Int> -> north_neighbours(p)},
        's' to { p: Pair<Int, Int> -> south_neighbours(p)},
        'w' to { p: Pair<Int, Int> -> west_neighbours(p)},
        'e' to { p: Pair<Int, Int> -> east_neighbours(p)}
    )

    val go =mapOf(
        'n' to { p: Pair<Int, Int> -> p.first-1 to p.second},
        's' to { p: Pair<Int, Int> -> p.first+1 to p.second},
        'w' to { p: Pair<Int, Int> -> p.first to p.second-1},
        'e' to { p: Pair<Int, Int> -> p.first to p.second+1},
    )

    private fun printElves(elves: Set<Pair<Int, Int>>): Int {
        val minRow = elves.minOf { it.first }
        val maxRow = elves.maxOf { it.first }
        val minCol = elves.minOf { it.second }
        val maxCol = elves.maxOf { it.second }
        var emptyTiles = 0
        for (row in minRow until maxRow+1) {
            for (col in minCol until maxCol+1) {
                if (elves.contains(row to col)) {
                    print('#')
                } else {
                    print('.')
                    emptyTiles++
                }
            }
            println()
        }
        println()
        return emptyTiles
    }

    override fun executePart1(name: String): Any {
        var elves = getElvesPosition(name)
        val directions = LinkedList(listOf('n','s','w','e'))
        for (i in 0 until 10) {
            val whereToGo = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
            for (elf in elves) {
                if (neighbours(elf).any { elves.contains(it) }) {
                    for (dir in directions) {
                        if (directionMap[dir]!!(elf).none { elves.contains(it) }) {
                            whereToGo[elf] = go[dir]!!(elf)
                            break
                        }
                    }
                }
            }
            val allowedToMove = whereToGo.entries
                .groupBy { it.value }
                .filter { it.value.size < 2 }
                .map { it.value.first() }
                .associate { it.key to it.value }
            elves = elves.map { allowedToMove.getOrDefault(it, it) }.toSet()
            directions.addLast(directions.pop())
        }
        return printElves(elves)
    }

    override fun executePart2(name: String): Any {
        var elves = getElvesPosition(name)
        val directions = LinkedList(listOf('n','s','w','e'))
        var c = 1
        while (true) {
            val whereToGo = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
            for (elf in elves) {
                if (neighbours(elf).any { elves.contains(it) }) {
                    for (dir in directions) {
                        if (directionMap[dir]!!(elf).none { elves.contains(it) }) {
                            whereToGo[elf] = go[dir]!!(elf)
                            break
                        }
                    }
                }
            }
            val allowedToMove = whereToGo.entries
                .groupBy { it.value }
                .filter { it.value.size < 2 }
                .map { it.value.first() }
                .associate { it.key to it.value }
            if (allowedToMove.isEmpty()) {
                break
            }
            elves = elves.map { allowedToMove.getOrDefault(it, it) }.toSet()
            directions.addLast(directions.pop())
            c++
        }
        return c
    }

}