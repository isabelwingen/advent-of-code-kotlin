package aoc2017

import util.Day
import kotlin.math.abs

class Day3: Day("3") {

    private fun calc(x: Int): Any {
        var startOfRing = 2
        var offset = 0
        var i = 0
        while (startOfRing <= x) {
            offset += 8
            startOfRing += offset
            i++
        }
        startOfRing -= offset

        // the number is in ring i, which has a side length of  (2*i+1)
        // the ring start on the left hand border, one position up from the bottom with startOfRing
        var steps = x - startOfRing
        val sideLength = 2*i+1
        var stepsToMiddle = i-1
        if (steps <= sideLength - 2) {
            return i + abs(steps - stepsToMiddle)
        }
        steps -= (sideLength - 2)
        stepsToMiddle = i
        if (steps <= sideLength) {
            return i + abs(steps - stepsToMiddle)
        }
        steps -= (sideLength - 1)
        stepsToMiddle = i
        if (steps <= sideLength) {
            return i + abs(steps - stepsToMiddle)
        }
        steps -= (sideLength - 1)
        stepsToMiddle = i
        if (steps <= sideLength) {
            return i + abs(steps - stepsToMiddle)
        }
        return steps
    }

    override fun executePart1(name: String): Any {
       return calc(325489)

    }

    fun Pair<Int, Int>.neighbours(): Set<Pair<Int, Int>> {
        return setOf(
            this.first - 1 to this.second - 1,
            this.first - 1 to this.second,
            this.first - 1 to this.second + 1,
            this.first to this.second - 1,
            this.first to this.second + 1,
            this.first + 1 to this.second -1,
            this.first + 1 to this.second,
            this.first + 1 to this.second + 1,
        )
    }

    override fun executePart2(name: String): Any {
        val map = mutableMapOf<Pair<Int, Int>, Int>()
        map[0 to 0] = 1
        var pos = 1 to 0
        var ring = 1
        var steps = 1
        while (true) {
            val value = pos.neighbours().sumOf { map.getOrDefault(it, 0) }
            if (value > 325489) {
                return value
            } else {
                map[pos] = value
                // ring = 1 steps = 8
                // ring = 2 steps = 16
                pos = if (steps < ring * 2) { // go up
                    pos.first to pos.second-1
                } else if (steps < ring * 4) { // go left
                    pos.first-1 to pos.second
                } else if (steps < ring * 6) { // go down
                    pos.first to pos.second+1
                } else {
                    pos.first+1 to pos.second
                }
                if (steps >= ring * 8) {
                    ring += 1
                    steps = 1
                } else {
                    steps += 1
                }
            }
        }
    }
}