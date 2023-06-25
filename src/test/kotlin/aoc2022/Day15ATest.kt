package aoc2022

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day15ATest {

    @Test
    public fun testAddNoZoneToField() {
        val d = Day15A()
        var field = mutableMapOf<Long, MutableList<Pair<Long, Long>>>()
        // add to empty
        d.addNoZoneToField(field, 0, 10L to 20L)
        assertEquals(mutableListOf(10L to 20L), field[0], "Expect one element (10,20)")
        d.addNoZoneToField(field, 0, 12L to 18L)
        assertEquals(mutableListOf(10L to 20L), field[0], "No new element is added")
        d.addNoZoneToField(field, 0, 30L to 40L)
        assertEquals(mutableListOf(10L to 20L, 30L to 40L), field[0], "element is added at the end")
        d.addNoZoneToField(field, 0, 1L to 5L)
        assertEquals(mutableListOf(1L to 5L, 10L to 20L, 30L to 40L), field[0], "element is added at the beginning")
        d.addNoZoneToField(field, 0, 4L to 35L)
        assertEquals(mutableListOf(1L to 40L), field[0], "starting in one, ending in another")
        d.addNoZoneToField(field, 0, 35L to 50L)
        assertEquals(mutableListOf(1L to 50L), field[0], "going over the right end")
        d.addNoZoneToField(field, 0, 0L to 10L)
        assertEquals(mutableListOf(0L to 50L), field[0], "going below the left end")

        field = mutableMapOf(0L to mutableListOf(10L to 20L, 30L to 40L, 50L to 60L, 70L to 80L, 90L to 100L))
        d.addNoZoneToField(field, 0, 25L to 55L)
        assertEquals(mutableListOf(10L to 20L, 25L to 60L, 70L to 80L, 90L to 100L), field[0], "left outside, right inside")
        d.addNoZoneToField(field, 0, 75L to 110L)
        assertEquals(mutableListOf(10L to 20L, 25L to 60L, 70L to 110L), field[0], "left inside, right outside")

        d.addNoZoneToField(field, 0, 5L to 120L)
        assertEquals(mutableListOf(5L to 120L), field[0], "left outside, right outside")
        d.addNoZoneToField(field, 0, 200L to 300L)
        d.addNoZoneToField(field, 0, 400L to 500L)
        d.addNoZoneToField(field, 0, 350L to 370L)


        assertEquals(mutableListOf(5L to 120L, 200L to 300L, 350L to 370L, 400L to 500L), field[0], "between two ranges")
        d.addNoZoneToField(field, 0, 370L to 380L)
        assertEquals(mutableListOf(5L to 120L, 200L to 300L, 350L to 380L, 400L to 500L), field[0], "equal ends")
        d.addNoZoneToField(field, 0, 4L to 121L)
        d.addNoZoneToField(field, 0, 301L to 349L)
        d.addNoZoneToField(field, 0, 0L to 3L)
        d.addNoZoneToField(field, 0, 122L to 199L)
        d.addNoZoneToField(field, 0, 381L to 399L)
        d.addNoZoneToField(field, 0, 501L to 510L)
        d.addNoZoneToField(field, 0, 510L to 530L)
        assertEquals(mutableListOf(0L to 530L), field[0], "connect diff by one")
    }

    @Test
    fun test() {
        println(Day15A().executePart2())
    }
}
