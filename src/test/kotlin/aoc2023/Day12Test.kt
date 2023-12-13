package aoc2023

import getInput
import getInputAsLines
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.nio.file.Paths

internal class Day12Test {

    @ParameterizedTest(name = "{0} should have {1} combinations")
    @MethodSource("getData")
    fun `Finding Combinations works as expected`(line: Day12.Row, result: Int) {
        assertEquals(result, day.combinations(line))
    }

    companion object {

        val day = Day12()

        @JvmStatic
        fun getLines(): List<Arguments> {
            val resourceFile = Paths.get("src", "test", "resources", "2023", "day12.txt")
            return resourceFile.toFile().readText().split("\n")
                .filter { it.isNotBlank() }
                .map { Day12.Row.parse(it) }
                .map { Arguments.of(it) }
        }

        @JvmStatic
        fun getData(): List<Arguments> {
            return listOf(
                Arguments.of(Day12.Row.parse("??????????..?.?? 3,4,1"), 19),
                Arguments.of(Day12.Row.parse("??##??.?????#?#. 6,1,1,2,1"), 1),
                Arguments.of(Day12.Row.parse("?#???##?#???. 3,5"), 3),
                Arguments.of(Day12.Row.parse("??.#.??#??#??????? 1,1,3,4,1,1"), 10),
                Arguments.of(Day12.Row.parse("?#?????.??????. 4,1"), 15),
                Arguments.of(Day12.Row.parse("#..?.?#?#????#? 1,1,1,1,2"), 5),
                Arguments.of(Day12.Row.parse("???.### 1,1,3"), 1),
                Arguments.of(Day12.Row.parse(".??..??...?##. 1,1,3"), 4),
                Arguments.of(Day12.Row.parse("?#?#?#?#?#?#?#? 1,3,1,6"), 1),
                Arguments.of(Day12.Row.parse("????.#...#... 4,1,1"), 1),
                Arguments.of(Day12.Row.parse("????.######..#####. 1,6,5"), 4),
                Arguments.of(Day12.Row.parse("?###???????? 3,2,1"), 10),
                Arguments.of(Day12.Row.parse(".##.?#??.#.?# 2,1,1,1"), 1),
                Arguments.of(Day12.Row.parse("###.### 3"), 0),
                Arguments.of(Day12.Row.parse(".??.#??.??? 1,1,2"), 6),
                Arguments.of(Day12.Row.parse("???#????#?????#?## 1,2,4,1,5"), 5),
            )
        }
    }
}