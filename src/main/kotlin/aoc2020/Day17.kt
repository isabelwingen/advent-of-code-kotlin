package aoc2020

import getResourceAsList

private fun parseInput11(): List<List<Char>> {
    return getResourceAsList("day17.txt")
        .filter { it.isNotBlank() }
        .map { it.toCharArray().asList() }
}

data class Quadruple(val a: Int, val b: Int, val c: Int, val d: Int)

private fun <T> buildCuboid(input: List<List<Char>>, coordinateFun: (a: Int, b: Int) -> T): Map<T, Char> {
    val res = mutableMapOf<T, Char>()
    for (i in input.indices) {
        for (j in input[0].indices) {
            res[coordinateFun(i, j)] = input[i][j]
        }
    }
    return res.toMap()
}

private fun neighbourCoordinates3d(coord: Triple<Int, Int, Int>): List<Triple<Int, Int, Int>> {
    val res = mutableListOf<Triple<Int, Int, Int>>()
    for (z in coord.first - 1 until coord.first + 2) {
        for (x in coord.second - 1 until coord.second + 2) {
            for (y in coord.third - 1 until coord.third + 2) {
                res.add(Triple(z, x, y))
            }
        }
    }
    return res.filter { it != coord }
}

private fun neighbourCoordinates4d(coord: Quadruple): List<Quadruple> {
    val res = mutableListOf<Quadruple>()
    for (w in coord.a -1 until coord.a + 2) {
        for (z in coord.b - 1 until coord.b + 2) {
            for (x in coord.c - 1 until coord.c + 2) {
                for (y in coord.d - 1 until coord.d + 2) {
                    res.add(Quadruple(w, z, x, y))
                }
            }
        }
    }
    return res.filter { it != coord }
}

const val INACTIVE = '.'
const val ACTIVE = '#'

private fun <T> calculateActiveNeighbours(coord: T, cuboid: Map<T, Char>, neighbourFun: (x: T) -> List<T>): Int {
    return neighbourFun(coord)
        .map { cuboid.getOrDefault(it, INACTIVE) }
        .count { it == ACTIVE }
}

private fun <T> step(cuboid: Map<T, Char>, neighbourFun: (x: T) -> List<T>): Map<T, Char> {
    val res = mutableMapOf<T, Char>()
    cuboid.keys
        .flatMap { neighbourFun(it) }
        .distinct()
        .filter { !cuboid.keys.contains(it) }
        .forEach {
            if (calculateActiveNeighbours(it, cuboid, neighbourFun)  == 3) {
                res[it] = ACTIVE
            }
        }
    for (entry in cuboid) {
        val activeNeighbours = calculateActiveNeighbours(entry.key, cuboid, neighbourFun)
        if (entry.value == ACTIVE) {
            if (activeNeighbours == 2 || activeNeighbours == 3) {
                res[entry.key] = ACTIVE
            }
        } else {
            if (activeNeighbours == 3) {
                res[entry.key] = ACTIVE
            }
        }
    }
    return res.toMap()
}

fun executeDay17Part1(): Int {
    var cuboid = buildCuboid(parseInput11()) { a, b -> Triple(0, a, b)}
    for (i in 0 until 6) {
        cuboid = step(cuboid) {x -> neighbourCoordinates3d(x)}
    }
    return cuboid.values.count { it == ACTIVE }
}

fun executeDay17Part2(): Int {
    var cuboid = buildCuboid(parseInput11()) { a, b -> Quadruple(0, 0, a, b)}
    for (i in 0 until 6) {
        cuboid = step(cuboid) {x -> neighbourCoordinates4d(x)}
    }
    return cuboid.values.count { it == ACTIVE }
}