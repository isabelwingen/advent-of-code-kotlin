package aoc2022

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day22Test {
    //    _ _
    //   |1|4|
    //    2
    //  3|6
    //  5

    @Test
    fun doNotLeaveArea_fewSteps() {
        val day = Day22()
        val obstacles = mapOf<Int, Map<String, List<List<Int>>>>(
            1 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            2 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            3 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            4 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            5 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            6 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
        )
        for (i in 1 until 7) {
            assertEquals(State(i, 10, 13, RIGHT), day.nextSteps(3, State(i, 10, 10, RIGHT), obstacles))
            assertEquals(State(i, 10, 7, LEFT), day.nextSteps(3, State(i, 10, 10, LEFT), obstacles))
            assertEquals(State(i, 7, 10, UP), day.nextSteps(3, State(i, 10, 10, UP), obstacles))
            assertEquals(State(i, 13, 10, DOWN), day.nextSteps(3, State(i, 10, 10, DOWN), obstacles))
        }
    }

    @Test
    fun doNotLeaveArea_obstacle() {
        val day = Day22()
        val x = listOf(0, 49)
        val obstacles = IntRange(1, 6).toList()
            .map { i -> i to mapOf<String, List<MutableList<Int>>>(
                "lr" to List(50) { mutableListOf() },
                "ud" to List(50) { mutableListOf() }
            ) }
            .toMap()
        for (i in 1 until 7) {
            obstacles[i]!!["lr"]!![10+i].add(0)
            obstacles[i]!!["lr"]!![10+i].add(49)
            obstacles[i]!!["lr"]!![0].add(10+i)
            obstacles[i]!!["lr"]!![49].add(10+i)

            obstacles[i]!!["ud"]!![10+i].add(0)
            obstacles[i]!!["ud"]!![10+i].add(49)
            obstacles[i]!!["ud"]!![49].add(10+i)
            obstacles[i]!!["ud"]!![0].add(10+i)
        }

        assertEquals(State(1, 14, 49, RIGHT), day.nextSteps(7, State(1, 14, 45, RIGHT), obstacles))
        assertEquals(State(1, 49, 12, DOWN), day.nextSteps(7, State(1, 45, 12, DOWN), obstacles))
        assertEquals(State(1, 0, 15, UP), day.nextSteps(7, State(1, 4, 15, UP), obstacles))
        assertEquals(State(1, 36, 0, LEFT), day.nextSteps(7, State(1, 36, 4, LEFT), obstacles))

        assertEquals(State(2, 14, 49, RIGHT), day.nextSteps(7, State(2, 14, 45, RIGHT), obstacles))
        assertEquals(State(2, 49, 16, DOWN), day.nextSteps(7, State(2, 45, 16, DOWN), obstacles))
        assertEquals(State(2, 0, 11, UP), day.nextSteps(7, State(2, 4, 11, UP), obstacles))
        assertEquals(State(2, 13, 0, LEFT), day.nextSteps(7, State(2, 13, 4, LEFT), obstacles))

        assertEquals(State(3, 16, 49, RIGHT), day.nextSteps(7, State(3, 16, 45, RIGHT), obstacles))
        assertEquals(State(3, 49, 15, DOWN), day.nextSteps(7, State(3, 45, 15, DOWN), obstacles))
        assertEquals(State(3, 0, 12, UP), day.nextSteps(7, State(3, 4, 12, UP), obstacles))
        assertEquals(State(3, 38, 0, LEFT), day.nextSteps(7, State(3, 38, 4, LEFT), obstacles))

        assertEquals(State(4, 33, 49, RIGHT), day.nextSteps(7, State(4, 33, 45, RIGHT), obstacles))
        assertEquals(State(4, 49, 12, DOWN), day.nextSteps(7, State(4, 45, 12, DOWN), obstacles))
        assertEquals(State(4, 0, 15, UP), day.nextSteps(7, State(4, 4, 15, UP), obstacles))
        assertEquals(State(4, 11, 0, LEFT), day.nextSteps(7, State(4, 11, 4, LEFT), obstacles))

        assertEquals(State(5, 16, 49, RIGHT), day.nextSteps(7, State(5, 16, 45, RIGHT), obstacles))
        assertEquals(State(5, 49, 14, DOWN), day.nextSteps(7, State(5, 45, 14, DOWN), obstacles))
        assertEquals(State(5, 0, 13, UP), day.nextSteps(7, State(5, 4, 13, UP), obstacles))
        assertEquals(State(5, 11, 0, LEFT), day.nextSteps(7, State(5, 11, 4, LEFT), obstacles))

        assertEquals(State(6, 35, 49, RIGHT), day.nextSteps(7, State(6, 35, 45, RIGHT), obstacles))
        assertEquals(State(6, 49, 15, DOWN), day.nextSteps(7, State(6, 45, 15, DOWN), obstacles))
        assertEquals(State(6, 0, 12, UP), day.nextSteps(7, State(6, 4, 12, UP), obstacles))
        assertEquals(State(6, 13, 0, LEFT), day.nextSteps(7, State(6, 13, 4, LEFT), obstacles))

    }

    @Test
    fun part2() {
        val day = Day22()
        val obstacles = mapOf<Int, Map<String, List<List<Int>>>>(
            1 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            2 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            3 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            4 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            5 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
            6 to mapOf("lr" to List(50) { emptyList() }, "ud" to List(50) { emptyList() }),
        )

        assertEquals(State(4, 14, 2, RIGHT), day.nextSteps(7, State(1, 14, 45, RIGHT), obstacles))
        assertEquals(State(2, 2, 12, DOWN), day.nextSteps(7, State(1, 45, 12, DOWN), obstacles))
        assertEquals(State(5, 15, 2, RIGHT), day.nextSteps(7, State(1, 4, 15, UP), obstacles))
        assertEquals(State(3, 13, 2, RIGHT), day.nextSteps(7, State(1, 36, 4, LEFT), obstacles))

        assertEquals(State(4, 47, 14, UP), day.nextSteps(7, State(2, 14, 45, RIGHT), obstacles))
        assertEquals(State(6, 2, 16, DOWN), day.nextSteps(7, State(2, 45, 16, DOWN), obstacles))
        assertEquals(State(1, 47, 11, UP), day.nextSteps(7, State(2, 4, 11, UP), obstacles))
        assertEquals(State(3, 2, 13, DOWN), day.nextSteps(7, State(2, 13, 4, LEFT), obstacles))

        assertEquals(State(6, 14, 2, RIGHT), day.nextSteps(7, State(3, 14, 45, RIGHT), obstacles))
        assertEquals(State(5, 2, 16, DOWN), day.nextSteps(7, State(3, 45, 16, DOWN), obstacles))
        assertEquals(State(2, 11, 2, RIGHT), day.nextSteps(7, State(3, 4, 11, UP), obstacles))
        assertEquals(State(1, 36, 2, RIGHT), day.nextSteps(7, State(3, 13, 4, LEFT), obstacles))

        assertEquals(State(6, 35, 47, LEFT), day.nextSteps(7, State(4, 14, 45, RIGHT), obstacles))
        assertEquals(State(2, 16, 47, LEFT), day.nextSteps(7, State(4, 45, 16, DOWN), obstacles))
        assertEquals(State(5, 47, 11, UP), day.nextSteps(7, State(4, 4, 11, UP), obstacles))
        assertEquals(State(1, 13, 47, LEFT), day.nextSteps(7, State(4, 13, 4, LEFT), obstacles))

        assertEquals(State(6, 47, 14, UP), day.nextSteps(7, State(5, 14, 45, RIGHT), obstacles))
        assertEquals(State(4, 2, 16, DOWN), day.nextSteps(7, State(5, 45, 16, DOWN), obstacles))
        assertEquals(State(3, 47, 11, UP), day.nextSteps(7, State(5, 4, 11, UP), obstacles))
        assertEquals(State(1, 2, 13, DOWN), day.nextSteps(7, State(5, 13, 4, LEFT), obstacles))

        assertEquals(State(4, 35, 47, LEFT), day.nextSteps(7, State(6, 14, 45, RIGHT), obstacles))
        assertEquals(State(5, 16, 47, LEFT), day.nextSteps(7, State(6, 45, 16, DOWN), obstacles))
        assertEquals(State(2, 47, 11, UP), day.nextSteps(7, State(6, 4, 11, UP), obstacles))
        assertEquals(State(3, 13, 47, LEFT), day.nextSteps(7, State(6, 13, 4, LEFT), obstacles))

    }

    @Test
    public fun testFailingMove() {
        val day = Day22()
        val areas = day.getAreas("2022/day22.txt")
        val obstacles = areas.map { it.key to day.getObstaclesForArea(it.value) }.toMap()
        val startState = State(2, 11, 48, 3)
        val end = day.nextSteps(27, startState, obstacles)
        println(end)
    }
}