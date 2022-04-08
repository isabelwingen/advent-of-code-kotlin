package aoc2019

import algorithm.IntCode
import getResourceAsText

fun executeDay5Part1(name: String = "2019/day5.txt"): List<Int> {
    val numbers = getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
    println(numbers.toList().take(20))
    val intCode = IntCode(numbers)
    intCode.reset(input = listOf(1))
    intCode.execute()
    return intCode.getOutput()
}

fun executeDay5Part2(name: String = "2019/day5.txt"): List<Int> {
    val numbers = getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
    println(numbers.toList().take(20))
    val intCode = IntCode(numbers)
    intCode.reset(input = listOf(5))
    intCode.execute()
    return intCode.getOutput()
}
