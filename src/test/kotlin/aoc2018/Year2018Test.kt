package aoc2018

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import util.Day
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class Year2018Test {

    private val expectedResults: MutableMap<String, Map<String, Long>> = HashMap()

    @BeforeAll
    fun readExpectedResults() {
        val resourceFile = Paths.get("src", "test", "resources", "2018", "results.json")
        val resultJson = resourceFile.toFile().readText()
        expectedResults.putAll(Json.decodeFromString<Map<String, Map<String, Long>>>(resultJson))
    }

    @ParameterizedTest(name = "Execution should be correct for day {0}, part1")
    @MethodSource("getDataPart1")
    fun testPart1(key: Int, day: Day) {
        assertEquals(expectedResults[day.key]!!["part1"], day.executePart1())
    }

    @ParameterizedTest(name = "Execution should be correct for day {0}, part2")
    @MethodSource("getDataPart2")
    fun testPart2(key: Int, day: Day) {
        assertEquals(expectedResults[day.key]!!["part2"], day.executePart2())
    }

    @Test
    fun executeLatest() {
        println(Day1().executePart1())
    }

    companion object {
        @JvmStatic
        fun getDataPart1():List<Arguments> {
            return listOf(
                Arguments.of("1", Day1()),
            )
        }

        @JvmStatic
        fun getDataPart2(): List<Arguments> {
            return listOf(
            )
        }
    }
}