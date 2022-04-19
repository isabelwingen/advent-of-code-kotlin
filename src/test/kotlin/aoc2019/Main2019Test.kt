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
        val day = days.first { it.key() == key }
        assertEquals(day.expectedResultPart1(), day.executePart1())
        assertEquals(day.expectedResultPart2(), day.executePart2())
    }

    companion object {
        val days = setOf(Day1(), Day2(), Day3(), Day4(), Day5(), Day6(), Day7(), Day8(), Day9(), Day10(), Day11(), Day12())

        @JvmStatic
        fun getData(): List<Arguments> {
            return days.map { Arguments.of(it.key()) }
        }
    }

    @Test
    fun testSingleExecution() {
        val key = "5"
        val day = days.first { it.key() == key }
        assertEquals(day.expectedResultPart1(), day.executePart1())
        assertEquals(day.expectedResultPart2(), day.executePart2())
    }
}