package aoc2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class Day19Test {

    @ParameterizedTest(name = "building robot {1} from {0} yields {2}")
    @MethodSource("getDataForBuild")
    fun build(state: IntArray, robotToBuild: Int, expectedState: IntArray?) {
        val day = Day19()
        val blueprint = arrayOf(intArrayOf(4,0,0), intArrayOf(2,0,0), intArrayOf(3,14,0), intArrayOf(2,0,7))

        val result = day.build(robotToBuild, state, blueprint, intArrayOf(4,14,7), 24)
        assertEquals(expectedState, result)
    }

    @Test
    fun execution() {
       assertEquals(988, Day19().executePart1())
       assertEquals(8580, Day19().executePart2())
    }


    companion object {
        @JvmStatic
        fun getDataForBuild():List<Arguments> {
            return listOf(
                Arguments.of(intArrayOf(1,0,0,0,0,0,0,0), 0, intArrayOf(2,0,0,1,0,0,0,5)),
                Arguments.of(intArrayOf(1,0,0,0,0,0,0,0), 1, intArrayOf(1,1,0,1,0,0,0,3)),
                Arguments.of(intArrayOf(1,0,0,0,0,0,0,0), 2, null),
                Arguments.of(intArrayOf(1,0,0,0,0,0,0,0), 3, null),
                Arguments.of(intArrayOf(1,1,0,3,2,0,0,4), 2, intArrayOf(1,1,1,13,1,0,0,17)),
                Arguments.of(intArrayOf(1,1,2,0,13,2,0,4), 3, intArrayOf(1,1,2,2,17,3,16,8)),
                Arguments.of(intArrayOf(1,1,2,0,13,2,10,4), 3, intArrayOf(1,1,2,2,17,3,26,8)),
                Arguments.of(intArrayOf(1,1,2,0,13,2,10,22), 3, null)
            )
        }
    }
}