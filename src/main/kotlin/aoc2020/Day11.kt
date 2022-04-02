package aoc2020

import algorithm.GameOfLife
import getResourceAsList

enum class Seat(val seat: String) {
    EMPTY("L"),
    FLOOR("."),
    OCCUPIED("#");

    override fun toString(): String {
      return seat
    }
}

private fun transformLine(line: String): List<Seat> {
    return line
        .map { when (it) {
            'L' -> Seat.EMPTY
            '#' -> Seat.OCCUPIED
            else -> Seat.FLOOR
        } }
}

private fun parseInput(path: String): Set<Triple<Int, Int, Seat>> {
    return getResourceAsList(path)
        .asSequence()
        .filter { it.isNotBlank() }
        .map { transformLine(it) }
        .mapIndexed {rowIndex, row -> row.mapIndexed {col, seat -> Triple(rowIndex, col, seat)}}
        .flatten()
        .toSet()
}

private fun directNeighbours(all: Set<Pair<Int, Int>>, pos: Pair<Int, Int>): Set<Pair<Int, Int>> {
    return setOf(
        pos.first - 1 to pos.second - 1,
        pos.first - 1 to pos.second,
        pos.first - 1 to pos.second + 1,

        pos.first     to pos.second - 1,
        pos.first     to pos.second + 1,

        pos.first + 1 to pos.second - 1,
        pos.first + 1 to pos.second,
        pos.first + 1 to pos.second + 1)
        .filter { all.contains(it) }
        .toSet()
}

val cache = mutableMapOf<Pair<Int, Int>, Set<Pair<Int, Int>>>()

private fun viewNeighbours(all: Set<Pair<Int, Int>>, pos: Pair<Int, Int>): Set<Pair<Int, Int>> {
    cache.computeIfAbsent(pos) { viewNeighboursH(all, it) }
    return cache[pos]!!
}

private fun viewNeighboursH(all: Set<Pair<Int, Int>>, pos: Pair<Int, Int>): Set<Pair<Int, Int>> {
    return setOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to +1,
        1 to -1,
        1 to 0,
        1 to +1)
        .map { vec -> IntRange(1, 100).map { pos.first + it * vec.first to pos.second + it * vec.second } }
        .mapNotNull { vecs -> vecs.firstOrNull { all.contains(it) } }
        .toSet()
}

fun executeDay11Part1(path: String = "day11.txt"): Int {
    val input = parseInput(path)
    val livingCells = input.filter { it.third == Seat.OCCUPIED }.map { it.first to it.second }.toSet()
    val allCells = input.filter { it.third != Seat.FLOOR }.map { it.first to it.second }.toSet()
    val allNeighbours = allCells.associateWith { directNeighbours(allCells, it) }
    val gameOfLife = GameOfLife(
        livingCells,
        allCells,
        getNeighbours = { allNeighbours[it]!! },
        comeAlive = { neighbours: Set<Pair<Int, Int>>, alive: Set<Pair<Int, Int>> ->
            val aliveNeighbours = neighbours.count { alive.contains(it) }
            aliveNeighbours == 0
        },
        die ={ neighbours: Set<Pair<Int, Int>>, alive: Set<Pair<Int, Int>> ->
        val aliveNeighbours = neighbours.count { alive.contains(it) }
        aliveNeighbours >= 4
        })

    while (gameOfLife.hasChanged()) {
        gameOfLife.step()
    }
    return gameOfLife.getLivingCells().count()
}

fun executeDay11Part2(path: String = "day11.txt"): Int {
    val input = parseInput(path)
    val livingCells = input.filter { it.third == Seat.OCCUPIED }.map { it.first to it.second }.toSet()
    val allCells = input.filter { it.third != Seat.FLOOR }.map { it.first to it.second }.toSet()
    val allNeighbours = allCells.associateWith { viewNeighbours(allCells, it) }
    val gameOfLife = GameOfLife(
        livingCells,
        allCells,
        getNeighbours = { allNeighbours[it]!! },
        comeAlive = { neighbours, alive ->
            val aliveNeighbours = neighbours.count { alive.contains(it) }
            aliveNeighbours == 0
        },
        die = { neighbours, alive ->
            val aliveNeighbours = neighbours.count { alive.contains(it) }
            aliveNeighbours >= 5
        })

    while (gameOfLife.hasChanged()) {
        gameOfLife.step()
    }
    return gameOfLife.getLivingCells().count()
}