package aoc2020

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day24KtTest {

    @Test
    fun westEastToEmpty() {
        //assertEquals(mapOf<Map<String, Int>, Int>(), executeDay24Part1(listOf("we")))
        //assertEquals(mapOf<Map<String, Int>, Int>(), executeDay24Part1(listOf("ew")))
        //assertEquals(mapOf<Map<String, Int>, Int>(), executeDay24Part1(listOf("eeeewwww")))
        //assertEquals(mapOf<Map<String, Int>, Int>(), executeDay24Part1(listOf("ewewwewe")))
        assertEquals(mapOf<Map<String, Int>, Int>(), executeDay24Part1(listOf("neewne")))
    }
}