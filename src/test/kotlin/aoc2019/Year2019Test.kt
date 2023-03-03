package aoc2019

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
internal class Year2019Test {

    private val expectedResults: MutableMap<String, Map<String, Long>> = HashMap()

    @BeforeAll
    fun readExpectedResults() {
        val resourceFile = Paths.get("src", "test", "resources", "2019", "results.json")
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
    fun testDay8Part2() {
        val expectedResult = "\n" +
                "#     ##  #   ##  # ###  \n" +
                "#    #  # #   ##  # #  # \n" +
                "#    #     # # #### ###  \n" +
                "#    # ##   #  #  # #  # \n" +
                "#    #  #   #  #  # #  # \n" +
                "####  ###   #  #  # ###  "
        assertEquals(expectedResult, Day8().executePart2())
    }

    @Test
    fun testDay11Part2() {
        val expectedResult = "\n" +
                " ###  #### ###  #### ###  ###  #  #  ##    \n" +
                " #  #    # #  # #    #  # #  # # #  #  #   \n" +
                " #  #   #  #  # ###  #  # #  # ##   #      \n" +
                " ###   #   ###  #    ###  ###  # #  #      \n" +
                " #    #    # #  #    #    # #  # #  #  # # \n" +
                " #    #### #  # #    #    #  # #  #  ##    "
        assertEquals(expectedResult, Day11().executePart2())
    }

    companion object {
        @JvmStatic
        fun getDataPart1(): List<Arguments> {
            return listOf(
                Day1(), Day2(), Day3(), Day4(), Day5(),
                Day6(), Day7(), Day8(), Day9(), Day10(),
                Day11(), Day12(), Day13(), Day14(), Day15(),
                Day16(), Day17(), Day18(), Day19(), Day20(),
                Day21(), Day22(), Day23(), Day24(),
            )
                .mapIndexed { index, day -> Arguments.of(index+1, day) }
        }

        @JvmStatic
        fun getDataPart2(): List<Arguments> {
            return listOf(
                Day1(), Day2(), Day3(), Day4(), Day5(),
                Day6(), Day7(), Day8(), Day9(), Day10(),
                Day11(), Day12(), Day13(), Day14(), Day15(),
                Day16(), Day17(), Day18(), Day19(), Day20(),
                Day21(), Day22(), Day23(), Day24()
            )
                .asSequence()
                .mapIndexed { index, day -> index+1 to day }
                .filter { it.first != 8 }
                .filter { it.first != 11 }
                .map { Arguments.of(it.first, it.second) }
                .toList()
        }
    }

}