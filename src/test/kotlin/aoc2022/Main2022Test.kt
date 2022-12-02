package aoc2022

import chosenYear
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

internal class Main2022Test {

    @ParameterizedTest(name = "result should be as expected for Day {0} Part 1")
    @MethodSource("getData")
    fun testExecutionsPart1(key: String) {
        chosenYear = "2022"
        val day = days.first { it.key == key }
        println(day.executePart1())
        assertEquals(day.expectedResultPart1(), day.executePart1())
    }

    @ParameterizedTest(name = "result should be as expected for Day {0} Part 2")
    @MethodSource("getData")
    fun testExecutionsPart2(key: String) {
        chosenYear = "2022"
        val day = days.first { it.key == key }
        println(day.executePart2())
        assertEquals(day.expectedResultPart2(), day.executePart2())
    }

    companion object {
        val days = setOf(Day1(), Day2())

        @JvmStatic
        fun getData(): List<Arguments> {
            return days.map { Arguments.of(it.key) }
        }
    }
}