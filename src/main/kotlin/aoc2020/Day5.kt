package aoc2020

import getResourceAsList

fun parseSeat(seat: String): Int {
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

fun executeDay5Part1(): Int? {
    return getResourceAsList("day5.txt")
        .filter { it.isNotBlank() }
        .map { parseSeat(it) }
        .maxOrNull()
}

fun executeDay5Part2(): Int {
    val coll = getResourceAsList("day5.txt")
        .filter { it.isNotBlank() }
        .map { parseSeat(it) }
        .sorted()
    val minSeat = coll[0]
    for (i in coll.indices) {
        if (coll[i] != minSeat + i) {
            return coll[i]-1
        }
    }
    return -1
}