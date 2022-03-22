package aoc2020

import getResourceAsList

private fun parseSeat(seat: String): Int {
    val row = seat
        .substring(0, 7)
        .map { if (it == 'F') 0 else 1}
        .map { it.toString() }
        .joinToString(separator = "")
        .toInt(radix = 2)
    val column = seat
        .substring(7, 10)
        .map { if (it == 'L') 0 else 1 }
        .map { it.toString() }
        .joinToString(separator = "")
        .toInt(radix = 2)
    return row * 8 + column
}

fun executeDay5Part1(name: String = "day5.txt"): Int {
    return getResourceAsList(name)
        .filter { it.isNotBlank() }
        .maxOf { parseSeat(it) }
}

fun executeDay5Part2(name: String = "day5.txt"): Int {
    val coll = getResourceAsList(name)
        .filter { it.isNotBlank() }
        .map { parseSeat(it) }
        .sorted()
    val minSeat = coll[0]
    for (i in coll.indices) {
        if (coll[i] != minSeat + i) {
            return coll[i] - 1
        }
    }
    return -1
}