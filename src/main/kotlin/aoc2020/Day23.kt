package aoc2020

private const val INPUT: String = "394618527"

private fun parseInput(): List<Int> {
    return INPUT
        .map { it.toString().toInt() }
}

private fun destination(current: Int, cups: ArrayList<Int>, firstPickUp: Int, secondPickUp: Int, thirdPickup: Int): Int {
    var dest = if (current != 1) current - 1 else cups.maxOrNull()!!
    while (firstPickUp == dest || secondPickUp == dest || thirdPickup == dest) {
        dest = if (dest != 1) dest - 1 else cups.maxOrNull()!!
    }
    return dest
}

private fun pickUp(cups: ArrayList<Int>, current: Int): ArrayList<Int> {
    val first = cups[current]
    val second = cups[first]
    val third = cups[second]
    val fourth = cups[third]
    val dest = destination(current, cups, first, second, third)
    val afterDest = cups[dest]

    cups[current] = fourth
    cups[dest] = first
    cups[third] = afterDest
    return cups
}

private fun buildArrayList(input: List<Int>): ArrayList<Int> {
    val cups = ArrayList(IntRange(0, input.size).map { -1 })
    for (i in input.indices) {
        if (i != input.size - 1) {
            cups[input[i]] = input[i + 1]
        }
    }
    cups[input[input.size - 1]] = input[0]
    return cups
}

private fun execute(times: Int, input: List<Int> = parseInput()): List<Int> {
    var cups = buildArrayList(input)
    var current = input[0]
    for (i in 1 until times+1) {
        cups = pickUp(cups, current)
        current = cups[current]
    }
    return cups.toList()
}

fun executeDay23Part1(): Int {
    val cups = execute(100)
    val res = mutableListOf<Int>()
    var current = 1
    while (res.size < 9) {
        current = cups[current]
        res.add(current)
    }
    return res.joinToString("").toInt()
}

fun executeDay23Part2(): Long {
    val input = parseInput().toMutableList()
    val maxPlusOne = input.maxOrNull()!! + 1
    for (i in maxPlusOne until 1_000_001) {
        input.add(i)
    }
    val cups = execute(10_000_000, input)
    val a = cups[1]
    val b = cups[a]
    return a.toLong() * b.toLong()
}
