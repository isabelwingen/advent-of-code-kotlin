package aoc2022;

import kotlinx.serialization.decodeFromString
import java.nio.file.Paths;
import kotlin.jvm.JvmStatic;
import kotlinx.serialization.json.Json;
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.Day;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Year2022Test {
    private val expectedResults: MutableMap<String, Map<String, Long>> = HashMap()

    @BeforeAll
    fun readExpectedResults() {
        val resourceFile = Paths.get("src", "test", "resources", "2022", "results.json")
        val resultJson = resourceFile.toFile().readText()
        expectedResults.putAll(Json.decodeFromString<Map<String, Map<String, Long>>>(resultJson))
    }

    @ParameterizedTest(name = "Execution should be correct for day {0}, part1")
    @MethodSource("getDataPart1")
    fun testPart1(key: Int, day:Day) {
        assertEquals(expectedResults[day.key]!!["part1"], day.executePart1())
    }

    @ParameterizedTest(name = "Execution should be correct for day {0}, part2")
    @MethodSource("getDataPart2")
    fun testPart2(key: Int, day: Day) {
        assertEquals(expectedResults[day.key]!!["part2"], day.executePart2())
    }

    @Test
    fun testDay19() {
        val res = Day19().executePart2()
        println(res)
        assertEquals(8580, res)
    }

    @Test
    fun testDay10Part2() {
        assertEquals(
            "\n" +
                    "###....##.####.###..#..#.###..####.#..#.\n" +
                    "#..#....#.#....#..#.#..#.#..#.#....#..#.\n" +
                    "###.....#.###..#..#.####.#..#.###..#..#.\n" +
                    "#..#....#.#....###..#..#.###..#....#..#.\n" +
                    "#..#.#..#.#....#.#..#..#.#.#..#....#..#.\n" +
                    "###...##..#....#..#.#..#.#..#.#.....##..",
            Day10().executePart2())
    }

    companion object {
        @JvmStatic
        fun getDataPart1():List<Arguments> {
            return listOf(
                Arguments.of("1", Day1()),
                Arguments.of("2", Day2()),
                Arguments.of("8", Day8()),
                Arguments.of("9", Day9()),
                Arguments.of("10", Day10()),
                Arguments.of("11", Day11()),
                Arguments.of("12", Day12()),
                Arguments.of("13", Day13()),
                Arguments.of("14", Day14()),
                Arguments.of("15", Day15()),
                Arguments.of("16", Day16()),
                Arguments.of("17", Day17()),
                Arguments.of("18", Day18()),
            )
        }

        @JvmStatic
        fun getDataPart2(): List<Arguments> {
            return listOf(
                Arguments.of("1", Day1()),
                Arguments.of("2", Day2()),
                Arguments.of("8", Day8()),
                Arguments.of("9", Day9()),
                //Arguments.of("10", Day10()) --> separate test
                Arguments.of("11", Day11()),
                Arguments.of("12", Day12()),
                Arguments.of("13", Day13()),
                Arguments.of("14", Day14()),
                Arguments.of("15", Day15()),
                Arguments.of("16", Day16()),
                Arguments.of("17", Day17()),
                Arguments.of("18", Day18()),
            )
        }
    }
}
