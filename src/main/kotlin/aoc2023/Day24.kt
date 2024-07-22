package aoc2023

import getInputAsLines
import util.Day
import java.math.BigInteger

class Day24: Day("24") {

    data class Point(val x: BigInteger, val y: BigInteger, val z: BigInteger) {
        fun add(other: Point) = Point(x + other.x, y + other.y, z + other.z)
        fun sub(other: Point) = Point(x - other.x, y - other.y, z - other.z)
        fun multiplyBy(scalar: BigInteger) = Point(x * scalar, y * scalar, z * scalar)
        fun div(scalar: BigInteger) = Point(x / scalar, y / scalar, z /scalar)
    }

    data class Hailstone(val p: Point, val v: Point) {
        companion object {
            fun parse(line: String): Hailstone {
                val (p,v) = line.split('@')
                val (px, py, pz) = p.split(',').map { it.trim().toBigInteger() }
                val (vx, vy, vz) = v.split(',').map { it.trim().toBigInteger() }
                return Hailstone(Point(px, py, pz), Point(vx, vy, vz))
            }
        }
    }

    override fun executePart1(name: String): Any {
        TODO("Not yet implemented")
    }

    private fun crossProduct(v: Point, u: Point): Point {
        return Point(
            u.y * v.z - u.z * v.y,
            u.z * v.x - u.x * v.z,
            u.x * v.y - u.y * v.x)
    }

    private fun dotProduct(v: Point, u: Point): BigInteger {
        return v.x.multiply(u.x) + v.y.multiply(u.y) + v.z.multiply(u.z)
    }

    // https://www.reddit.com/r/adventofcode/comments/18pnycy/comment/kxqjg33/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
    override fun executePart2(name: String): Any {
        val hailstones = getInputAsLines(name, true).map { Hailstone.parse(it) }.toList()
        val h0 = hailstones[0]
        val h1 = hailstones[1]
        val h2 = hailstones[2]
        val p1 = h1.p.sub(h0.p)
        val p2 = h2.p.sub(h0.p)
        val v1 = h1.v.sub(h0.v)
        val v2 = h2.v.sub(h0.v)
        val t1 = - (dotProduct(crossProduct(p1, p2), v2)) / (dotProduct(crossProduct(v1, p2), v2))
        val t2 = - (dotProduct(crossProduct(p1, p2), v1)) / (dotProduct(crossProduct(p1, v2), v1))
        val c1 = h1.p.add(h1.v.multiplyBy(t1))
        val c2 = h2.p.add(h2.v.multiplyBy(t2))
        val v = (c2.sub(c1)).div(t2 -t1)
        val p = c1.sub(v.multiplyBy(t1))
        return p.x + p.y + p.z
    }

}