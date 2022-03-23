package aoc2020

import round
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
        "10.1" to { executeDay10Part1() },
        "10.2" to { executeDay10Part2() },
        "11.1" to { executeDay11Part1() },
        "11.2" to { executeDay11Part2() },
        "12.1" to { executeDay12Part1() },
        "12.2" to { executeDay12Part2() },
        "13.1" to { executeDay13Part1() },
        "13.2" to { executeDay13Part2() },
        "14.1" to { executeDay14Part1() },
        "14.2" to { executeDay14Part2() },
        "15.1" to { executeDay15Part1() },
        "15.2" to { executeDay15Part2() },
        "16.1" to { executeDay16Part1() },
        "16.2" to { executeDay16Part2() },
        "17.1" to { executeDay17Part1() },
        "17.2" to { executeDay17Part2() },
        "18.1" to { executeDay18Part1() },
        "18.2" to { executeDay18Part2() },
        "19.1" to { executeDay19Part1() },
        "19.2" to { executeDay19Part2() },
        "20.1" to { executeDay20Part1() },
        "20.2" to { executeDay20Part2() },
        "21.1" to { executeDay21Part1() },
        "21.2" to { executeDay21Part2() },
        "22.1" to { executeDay22Part1() },
        "22.2" to { executeDay22Part2() },
        "23.1" to { executeDay23Part1() },
        "23.2" to { executeDay23Part2() },
        "24.1" to { executeDay24Part1() },
        "24.2" to { executeDay24Part2() },
        "25.1" to { executeDay25Part1() },
    )

    fun timeDistribution(): Map<String, Double> {
        val res = functions.mapValues { measureTimeMillis { it.value.invoke() } }
        val sum = res.values.sum()
        return res.mapValues { ((it.value.toDouble() / sum) * 100).round(2) }
    }

    fun executionTime(): Map<String, Double> {
        return functions.mapValues { measureTimeMillis { it.value.invoke() } }
            .mapValues { (it.value.toDouble() / 1000).round(3) }
    }

    fun execute(key: String): Any {
        return functions[key]!!.invoke()
    }
}



