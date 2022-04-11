package aoc2019

import algorithm.IntCode
import getResourceAsText

fun executeDay2Part1(name: String = "2019/day2.txt"): Long {
    val numbers = getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
    numbers[1] = 12
    numbers[2] = 2
    val intCode = IntCode(numbers)
    return intCode.execute().first()
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