package aoc2023

import getInputAsLines
import util.Day
import java.math.BigDecimal
import java.math.BigInteger

// y = mx + b
// p.y = m* p.x + b
// b = p.y - m*p.x

class Day24: Day("24") {

    data class Point(val x: Double, val y: Double)

    data class Hailstone(val p: Point, val v: Point) {

        val m = v.y/v.x

        val b = p.y - m * p.x

        companion object {
            fun parse(line: String): Hailstone {
                val (px, py, _, vx, vy) = line.split("@", ",")
                    .map { it.trim().toDouble() }
                return Hailstone(Point(px, py), Point(vx, vy))
            }
        }

        fun inFuture(cx: Double): Boolean {
            return if (v.x < 0) {
                cx < p.x
            } else {
                cx > p.x
            }
        }
    }


    fun intersectInFuture(h1: Hailstone, h2: Hailstone): Boolean {
        if (h1.m == h2.m) {
            return false
        }
        val cx = (h2.b - h1.b) / (h1.m - h2.m)
        val cy = (h1.m * (cx - h1.p.x)) + h1.p.y
        assert(cy == h2.m*cx + h2.b)

        return cy in 200000000000000.0..400000000000000.0 && cx in 200000000000000.0..400000000000000.0 && h1.inFuture(cx) && h2.inFuture(cx)
    }

    override fun executePart1(name: String): Any {
        val hailstones = getInputAsLines(name, true)
            .map { Hailstone.parse(it) }
        var count = 0
        for (i in hailstones.indices) {
            for (j in i+1..hailstones.lastIndex) {
                if (intersectInFuture(hailstones[i], hailstones[j])) {
                    count++
                }
            }
        }
        return count
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }


}