package aoc2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Day19Test {

    @ParameterizedTest(name = "building robot {1} from {0} yields {2}")
    @MethodSource("getDataForBuild")
    fun build(state: List<Int>, robotToBuild: Int, expectedState: List<Int>?) {
        val day = Day19()
        val blueprint = listOf(listOf(4,0,0), listOf(2,0,0), listOf(3,14,0), listOf(2,0,7))

        val result = day.build(robotToBuild, state, blueprint, listOf(4,14,7))
        assertEquals(expectedState, result)
    }


    companion object {
        @JvmStatic
        fun getDataForBuild():List<Arguments> {
            return listOf(
                Arguments.of(listOf(1,0,0,0,0,0,0,0), 0, listOf(2,0,0,1,0,0,0,5)),
                Arguments.of(listOf(1,0,0,0,0,0,0,0), 1, listOf(1,1,0,1,0,0,0,3)),
                Arguments.of(listOf(1,0,0,0,0,0,0,0), 2, null),
                Arguments.of(listOf(1,0,0,0,0,0,0,0), 3, null),
                Arguments.of(listOf(1,1,0,3,2,0,0,4), 2, listOf(1,1,1,13,1,0,0,17)),
                Arguments.of(listOf(1,1,2,0,13,2,0,4), 3, listOf(1,1,2,2,17,3,16,8)),
                Arguments.of(listOf(1,1,2,0,13,2,10,4), 3, listOf(1,1,2,2,17,3,26,8)),
                Arguments.of(listOf(1,1,2,0,13,2,10,22), 3, null)
            )
        }
    }
}