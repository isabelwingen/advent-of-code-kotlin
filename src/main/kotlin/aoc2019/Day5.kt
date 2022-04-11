package aoc2019

import algorithm.IntCode
import getResourceAsText

fun executeDay5Part1(name: String = "2019/day5.txt"): List<Long> {
    val numbers = getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
    println(numbers.toList().take(20))
    val intCode = IntCode(numbers)
    intCode.reset(listOf(1))
    return intCode.execute()
}

fun executeDay5Part2(name: String = "2019/day5.txt"): List<Long> {
    val numbers = getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
    println(numbers.toList().take(20))
    val intCode = IntCode(numbers)
    intCode.reset(listOf(5))
    return intCode.execute()
}
