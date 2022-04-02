package aoc2020

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import round
import kotlin.system.measureTimeMillis

internal class Main2020Test {

    @Test
    fun measure() {
        val res = Main2020().timeDistribution()
        println(res)
    }

    @Test
    fun measure2() {
        val res = Main2020()
            .executionTime()
            .asSequence()
            .onEach { println("${it.key}: ${it.value}") }
            .sumOf { it.value }
        println("total: $res")
    }

    @ParameterizedTest(name = "execute {0} should yield {1}")
    @MethodSource("getData")
    fun execute(key: String, expectedResult: Any) {
        val actualResult = Main2020().execute(key);
        if (actualResult is String) {
            assertTrue(expectedResult.equals(actualResult))
        } else {
            assertEquals(expectedResult, Main2020().execute(key))
        }
    }

    @Test
    fun testSingleExecution() {
        val key = "17.1"
        val actualResult = Main2020().execute(key)
        val expectedResult = getData().filter { it.get().first().equals(key) }.map { it.get()[1] }.first()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun measureSingleExecution() {
        val main = Main2020()
        val key = "11.2"
        val res = IntRange(0, 10).map { measureTimeMillis {  main.execute(key) } }

        println( (res.average() / 1000).round(3) )
        val actualResult = main.execute(key)
        val expectedResult = getData().filter { it.get().first().equals(key) }.map { it.get()[1] }.first()
        assertEquals(expectedResult, actualResult)

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
                Arguments.of("9.2", 16107959L),
                Arguments.of("10.1", 2040),
                Arguments.of("10.2", 28346956187648L),
                Arguments.of("11.1", 2289),
                Arguments.of("11.2", 2059),
                Arguments.of("12.1", 364),
                Arguments.of("12.2", 39518),
                Arguments.of("13.1", 4315),
                Arguments.of("13.2", 556100168221141L),
                Arguments.of("14.1", 12135523360904L),
                Arguments.of("14.2", 2741969047858L),
                Arguments.of("15.1", 1294),
                Arguments.of("15.2", 573522),
                Arguments.of("16.1", 19060),
                Arguments.of("16.2", 953713095011),
                Arguments.of("17.1", 286),
                Arguments.of("17.2", 960),
                Arguments.of("18.1", 12956356593940L),
                Arguments.of("18.2", 94240043727614L),
                Arguments.of("19.1", 162),
                Arguments.of("19.2", 267),
                Arguments.of("20.1", 15405893262491L),
                Arguments.of("20.2", 2133),
                Arguments.of("21.1", 2423),
                Arguments.of("21.2", "jzzjz,bxkrd,pllzxb,gjddl,xfqnss,dzkb,vspv,dxvsp"),
                Arguments.of("22.1", 34127),
                Arguments.of("22.2", 32054),
                Arguments.of("23.1", 785692341),
                Arguments.of("23.2", 565615814504L),
                Arguments.of("24.1", 479),
                Arguments.of("24.2", 4135),
                Arguments.of("25.1", 9177528L),
            )
        }
    }

}