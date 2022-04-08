package aoc2019

import algorithm.IntCode
import getResourceAsText

fun executeDay2Part1(name: String = "2019/day2.txt"): Int {
    val numbers = getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
    val intCode = IntCode(numbers)
    intCode.reset(listOf(12, 2))
    return intCode.execute()
}

fun executeDay2Part2(name: String = "2019/day2.txt"): Int {
    val numbers = getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
    val res2 = 19690720
    val i = (res2 - 1870666) / 810000
    val j = (res2 - 1870666) % 810000

    return 100 * i + j

}