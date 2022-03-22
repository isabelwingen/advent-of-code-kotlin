package aoc2020

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

internal class Main2020Test {

    @Test
    fun measure() {
        val res = Main2020().measure()
        assertTrue(res < 1)
        println("Execution done in $res seconds")
    }

    @ParameterizedTest(name = "execute {0} should yield {1}")
    @MethodSource("getData")
    fun execute(key: String, expectedResult: Any) {
        assertEquals(expectedResult, Main2020().execute(key))
    }

    companion object {
        @JvmStatic
        fun getData(): List<Arguments> {
            return listOf(
                Arguments.of("1.1", 605364),
                Arguments.of("1.2", 128397680),
                Arguments.of("2.1", 614),
                Arguments.of("2.2", 354),
                Arguments.of("3.1", 171),
                Arguments.of("3.2", 1206576000),
                Arguments.of("4.1", 192),
                Arguments.of("4.2", 101),
                Arguments.of("5.1", 959),
                Arguments.of("5.2", 527),
                Arguments.of("6.1", 6273),
                Arguments.of("6.2", 3254),
                Arguments.of("7.1", 335),
                Arguments.of("7.2", 2125),
                Arguments.of("8.1", 8),
                Arguments.of("8.2", 1539),
                Arguments.of("9.1", 133015568L),
                Arguments.of("9.2", 16107959L))
        }
    }

}