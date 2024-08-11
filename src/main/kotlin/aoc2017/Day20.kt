package aoc2017

import getInputAsLines
import util.Day
import kotlin.math.absoluteValue

class Day20: Day("20") {
    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)
            .map { it.split("a=").last().drop(1).dropLast(1).split(",").sumOf { x -> x.toInt().absoluteValue } }
            .indexOf(0)
    }

    private data class Vector(val x: Long, val y: Long, val z: Long) {
        fun add(other: Vector): Vector {
            return Vector(x + other.x, y + other.y, z + other.z)
        }
    }

    private data class Particle(val p: Vector, val v: Vector, val a: Vector) {
        fun move(): Particle {
            return this.copy(p = p.add(v.add(a)), v = v.add(a))
        }
    }

    //p=<-13053,-6894,1942>, v=<14,39,-11>, a=<16,7,-2>
    private fun parseLine(line: String): Particle {
        val l = line.split("=")
        val p = l[1].drop(1).dropLast(4).split(",").map { it.trim().toLong() }.run { Vector(this[0], this[1], this[2]) }
        val v = l[2].drop(1).dropLast(4).split(",").map { it.trim().toLong() }.run { Vector(this[0], this[1], this[2]) }
        val a = l[3].drop(1).dropLast(1).split(",").map { it.trim().toLong() }.run { Vector(this[0], this[1], this[2]) }
        return Particle(p, v, a)
    }

    override fun executePart2(name: String): Any {
        var particles = getInputAsLines(name, true)
            .map { parseLine(it) }
        var i = 0
        println(particles.size)
        while (i < 1000) {
            particles = particles.map { it.move() }
            val collided = particles.groupBy { it.p }.mapValues { it.value.size }.filter { it.value > 1 }.map { it.key }
            particles = particles.filterNot { collided.contains(it.p) }
            println(particles.size)
            i++
        }
        return particles.size
    }
}