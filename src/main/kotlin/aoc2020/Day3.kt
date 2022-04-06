package aoc2020

import getResourceAsList

private fun readInput(name: String): List<CharArray> {
    return getResourceAsList(name)
        .filter { x -> x.isNotBlank() }
        .map { it.asIterable().toList() }
        .map { it.toCharArray() }

}

private fun calculateSlope(path: String, x: Int, y: Int): Int {
    val map = readInput(path)
    val steps = map.count() / y
    var treeCount = 0
    for (step in 0 until steps) {
        val xx = (step*x) % map[0].count()
        val field = map[step*y][xx]
        if ('#' == field) {
            treeCount += 1
        }
    }
    return treeCount
}

fun executeDay3Part1(name: String = "2020/day3.txt"): Int {
    return calculateSlope(name, 3, 1)
}

fun executeDay3Part2(name: String = "2020/day3.txt"): Int {
    val a = calculateSlope(name, 1, 1)
    val b = calculateSlope(name, 3, 1)
    val c = calculateSlope(name, 5, 1)
    val d = calculateSlope(name, 7, 1)
    val e = calculateSlope(name, 1, 2)
    return a * b * c * d * e
}