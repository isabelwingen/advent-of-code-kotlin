package algorithm

import aoc2019.util.IntCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class IntCodeTest {


    @Test
    fun testCloning() {
        val longArray = LongArray(10)
        longArray[0] = 3
        longArray[1] = 8
        longArray[2] = 99
        val prog1 = IntCode("Var1", longArray)
        val prog3 = IntCode("Var1", longArray)
        val prog2 = prog1.copy()
        prog1.execute(4)
        assertEquals(prog2, prog3)

        println()
    }
}