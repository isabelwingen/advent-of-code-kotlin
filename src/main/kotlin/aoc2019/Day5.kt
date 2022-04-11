package aoc2019

import algorithm.IntCode
import getResourceAsText

fun executeDay5Part1(name: String = "2019/day5.txt"): Long {
    val numbers = getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
    println(numbers.toList().take(20))
    val intCode = IntCode("Day5", numbers)
    intCode.init(listOf(1))
    var res = 0L
    while (res == 0L) {
        res =  intCode.execute()
    }
    return res
}

fun executeDay5Part2(name: String = "2019/day5.txt"): Long {
    val numbers = getResourceAsText(name)!!
        .trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
    println(numbers.toList().take(20))
    val intCode = IntCode("Day5", numbers)
    intCode.init(listOf(5))
    return intCode.execute()
}
