import aoc2020.*
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Hello World!")
    val res = listOf(
        { executeDay1Part1() },
        { executeDay1Part2() },
        { executeDay2Part1() },
        { executeDay2Part2() },
        { executeDay3Part1() },
        { executeDay3Part2() },
        { executeDay4Part1() },
        { executeDay4Part2() },
        { executeDay5Part1() },
        { executeDay5Part2() },
        { executeDay6Part1() },
        { executeDay6Part2() },
        { executeDay7Part1() },
        { executeDay7Part2() },
        { executeDay8Part1() },
        { executeDay8Part2() },
        { executeDay9Part1() },
        { executeDay9Part2() },
    )
        .sumOf { measureTimeMillis { it.invoke() } }
    println("execution time: ${res.toDouble() / 1000} seconds")


}

