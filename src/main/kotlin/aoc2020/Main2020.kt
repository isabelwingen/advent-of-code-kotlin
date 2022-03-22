package aoc2020

import kotlin.system.measureTimeMillis

class Main2020 {
    private val functions = mapOf(
        "1.1" to { executeDay1Part1() },
        "1.2" to { executeDay1Part2() },
        "2.1" to { executeDay2Part1() },
        "2.2" to { executeDay2Part2() },
        "3.1" to { executeDay3Part1() },
        "3.2" to { executeDay3Part2() },
        "4.1" to { executeDay4Part1() },
        "4.2" to { executeDay4Part2() },
        "5.1" to { executeDay5Part1() },
        "5.2" to { executeDay5Part2() },
        "6.1" to { executeDay6Part1() },
        "6.2" to { executeDay6Part2() },
        "7.1" to { executeDay7Part1() },
        "7.2" to { executeDay7Part2() },
        "8.1" to { executeDay8Part1() },
        "8.2" to { executeDay8Part2() },
        "9.1" to { executeDay9Part1() },
        "9.2" to { executeDay9Part2() },
        "10.1" to { executeDay9Part2() },
        "10.2" to { executeDay9Part2() })

    fun measure(): Double {
        val res = functions.values.sumOf { measureTimeMillis { it.invoke() } }
        return res.toDouble() / 1000
    }

    fun execute(key: String): Any {
        return functions[key]!!.invoke()
    }
}



