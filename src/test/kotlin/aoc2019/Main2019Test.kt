package aoc2019

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class Main2019Test {

    @ParameterizedTest(name = "result should be as expected for Day {0}")
    @MethodSource("getData")
    fun testExecutions(key: String) {
        val day = days[key]!!
        assertEquals(day.expectedResultPart1(), day.executePart1())
        assertEquals(day.expectedResultPart2(), day.executePart2())
    }

    companion object {
        val days = mapOf(
            "1" to Day1(),
            "5" to Day5()
        )

        @JvmStatic
        fun getData(): List<Arguments> {
            return days.keys.map { Arguments.of(it) }
        }
    }

    @Test
    fun testSingleExecution() {
        val key = "5"
        val day = days[key]!!
        assertEquals(day.expectedResultPart1(), day.executePart1())
        assertEquals(day.expectedResultPart2(), day.executePart2())
    }
}