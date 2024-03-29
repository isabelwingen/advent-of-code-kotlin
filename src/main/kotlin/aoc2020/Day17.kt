package aoc2020

import aoc2020.util.GameOfLife
import getInputAsLines
import util.Day

data class Quadruple(val a: Int, val b: Int, val c: Int, val d: Int)

private fun parseInput(name: String): List<List<Char>> {
    return getInputAsLines(name)
        .filter { it.isNotBlank() }
        .map { it.toCharArray().asList() }
}

class Day17 : Day("17") {
    private fun <T> buildCuboid(input: List<List<Char>>, coordinateFun: (a: Int, b: Int) -> T): Set<T> {
        val res = mutableSetOf<T>()
        for (i in input.indices) {
            for (j in input[0].indices) {
                val value = input[i][j]
                if (value == Companion.ACTIVE) {
                    res.add(coordinateFun(i, j))
                }
            }
        }
        return res.toSet()
    }

    val neighbours3d = mutableMapOf<Triple<Int, Int, Int>, Set<Triple<Int, Int, Int>>>()

    private fun neighbourCoordinates3d(coord: Triple<Int, Int, Int>): Set<Triple<Int, Int, Int>> {
        neighbours3d.computeIfAbsent(coord) { neighbourCoordinates3dH(it) }
        return neighbours3d[coord]!!
    }

    private fun neighbourCoordinates3dH(coord: Triple<Int, Int, Int>): Set<Triple<Int, Int, Int>> {
        val res = mutableListOf<Triple<Int, Int, Int>>()
        for (z in coord.first - 1 until coord.first + 2) {
            for (x in coord.second - 1 until coord.second + 2) {
                for (y in coord.third - 1 until coord.third + 2) {
                    res.add(Triple(z, x, y))
                }
            }
        }
        return res.filter { it != coord }.toSet()
    }

    val neighbours4d = mutableMapOf<Quadruple, Set<Quadruple>>()

    private fun neighbourCoordinates4d(coord: Quadruple): Set<Quadruple> {
        neighbours4d.computeIfAbsent(coord) { neighbourCoordinates4dH(it) }
        return neighbours4d[coord]!!
    }

    private fun neighbourCoordinates4dH(coord: Quadruple): Set<Quadruple> {
        val res = mutableListOf<Quadruple>()
        for (w in coord.a - 1 until coord.a + 2) {
            for (z in coord.b - 1 until coord.b + 2) {
                for (x in coord.c - 1 until coord.c + 2) {
                    for (y in coord.d - 1 until coord.d + 2) {
                        res.add(Quadruple(w, z, x, y))
                    }
                }
            }
        }
        return res.filter { it != coord }.toSet()
    }

    override fun executePart1(name: String): Long {
        val cuboid = buildCuboid(parseInput(name)) { a, b -> Triple(0, a, b) }
        val gameOfLife = GameOfLife(
            livingCells = cuboid,
            allCells = null,
            getNeighbours = { neighbourCoordinates3d(it) },
            comeAlive = { neighbours, alive ->
                val aliveNeighbours = neighbours.count { alive.contains(it) }
                aliveNeighbours == 3
            },
            die = { neighbours, alive ->
                val aliveNeighbours = neighbours.count { alive.contains(it) }
                aliveNeighbours != 2 && aliveNeighbours != 3
            }
        )
        repeat(6) { gameOfLife.step() }
        return gameOfLife.getLivingCells().count().toLong()
    }

    override fun executePart2(name: String): Long {
        val cuboid = buildCuboid(parseInput(name)) { a, b -> Quadruple(0, 0, a, b) }
        val gameOfLife = GameOfLife(
            livingCells = cuboid,
            allCells = null,
            getNeighbours = { neighbourCoordinates4d(it) },
            comeAlive = { neighbours, alive ->
                val aliveNeighbours = neighbours.count { alive.contains(it) }
                aliveNeighbours == 3
            },
            die = { neighbours, alive ->
                val aliveNeighbours = neighbours.count { alive.contains(it) }
                aliveNeighbours != 2 && aliveNeighbours != 3
            }
        )
        repeat(6) { gameOfLife.step() }
        return gameOfLife.getLivingCells().count().toLong()
    }

    companion object {
        const val ACTIVE = '#'
    }
}