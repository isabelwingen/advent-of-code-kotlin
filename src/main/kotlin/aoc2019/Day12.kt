package aoc2019


import getInputAsLines
import util.Day
import kotlin.math.abs

class Day12: Day("12") {

    data class Vector(var x: Int, var y: Int, var z: Int) {
        fun plus(otherVector: Vector) {
            x += otherVector.x
            y += otherVector.y
            z += otherVector.z
        }

        fun abs(): Long {
            return abs(x.toLong()) + abs(y.toLong()) + abs(z.toLong())
        }

    }

    data class Moon(var pos: Vector, var vel: Vector) {
        fun move() {
            pos.plus(vel)
        }

        fun potentialEnergy(): Long {
            return pos.abs()
        }

        fun kineticEnergy(): Long {
            return vel.abs()
        }

        fun totalEnergy(): Long {
            return potentialEnergy() * kineticEnergy()
        }

        override fun toString(): String {
            val pad: (x: Int) -> String = { it.toString().padStart(4, ' ')}
            return "pos=<x=${pad(pos.x)}, y=${pad(pos.y)}, z=${pad(pos.z)}>, vel=<x=${pad(vel.x)}, y=${pad(vel.y)}, z=${pad(vel.z)}>"
        }

    }

    private fun parseInput(name: String): List<Moon> {
        return getInputAsLines(name)
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.split(",") }
            .map { it.map { t -> t.replace("<", "") } }
            .map { it.map { t -> t.replace(">", "") } }
            .map { it.map { t -> t.split("=")[1].toInt() } }
            .map { Moon(Vector(it[0], it[1], it[2]), Vector(0, 0, 0)) }
            .toList()
    }

    private fun applyGravity(moon1: Moon, moon2: Moon, getter: (t: Moon) -> Int, adder: (m: Moon, i: Int) -> Unit) {
        when (getter(moon1).compareTo(getter(moon2))) {
            -1 -> {
                adder(moon1, 1)
                adder(moon2, -1)
            }
            1 -> {
                adder(moon1, -1)
                adder(moon2, 1)
            }
        }
    }

    private fun applyGravity(moon1: Moon, moon2: Moon) {
        applyGravity(moon1, moon2, { m -> m.pos.x }) { m, i -> m.vel.x += i }
        applyGravity(moon1, moon2, { m -> m.pos.y }) { m, i -> m.vel.y += i }
        applyGravity(moon1, moon2, { m -> m.pos.z }) { m, i -> m.vel.z += i }
    }

    private fun applyGravity(io: Moon, europa: Moon, ganymede: Moon, callisto: Moon) {
        applyGravity(io, europa)
        applyGravity(io, ganymede)
        applyGravity(io, callisto)
        applyGravity(europa, ganymede)
        applyGravity(europa, callisto)
        applyGravity(ganymede, callisto)
    }

    private fun move(io: Moon, europa: Moon, ganymede: Moon, callisto: Moon) {
        io.move()
        europa.move()
        ganymede.move()
        callisto.move()
    }

    private fun step(io: Moon, europa: Moon, ganymede: Moon, callisto: Moon, id: Int = 0) {
        applyGravity(io, europa, ganymede, callisto)
        move(io, europa, ganymede, callisto)
    }


    override fun executePart1(name: String): Long {
        val (io, europa, ganymede, callisto) = parseInput(name)
        repeat(1000) { step(io, europa, ganymede, callisto, it + 1) }
        return io.totalEnergy() + europa.totalEnergy() + ganymede.totalEnergy() + callisto.totalEnergy()
    }

    override fun expectedResultPart1() = 7636L

    private fun simulateXCoordinate(xs: List<Int>): Int {
        val velocity = IntArray(4) { 0 }
        var positions = xs
        var i = 0
        do {
            val velAdd = positions.map {
                positions.filter { t -> it != t }.sumOf { t -> t.compareTo(it) }
            }
            for (j in velocity.indices) {
                velocity[j] += velAdd[j]
            }
            positions.mapIndexed { index, j -> velocity[index] + j }.also { positions = it }
            i++
        } while (positions != xs || !velocity.all { it == 0 } )
        return i
    }

    private fun lcm(a: Long, b: Long): Long {
        var gcd = 1L

        var i = 1L
        while (i <= a && i <= b) {
            if (a % i == 0L && b % i == 0L)
                gcd = i
            ++i
        }
        return (a * b) / gcd
    }

    override fun executePart2(name: String): Long {
        val moons = parseInput(name)
        val a = simulateXCoordinate(moons.map { it.pos.x }).toLong()
        val b = simulateXCoordinate(moons.map { it.pos.y }).toLong()
        val c = simulateXCoordinate(moons.map { it.pos.z }).toLong()
        return lcm(lcm(a, b), c)
    }

    override fun expectedResultPart2()= 281691380235984L
}