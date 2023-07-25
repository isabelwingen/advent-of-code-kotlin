package aoc2018

import getInputAsLines
import util.Day
import java.text.FieldPosition

class Day10: Day("10") {

    private data class Position(var x: Int, var y: Int) {
        fun move(velocity: Velocity) {
            x += velocity.x
            y += velocity.y
        }
    }

    private data class Velocity(val x: Int, val y: Int)

    private data class Light(val position: Position, val velocity: Velocity) {
        fun move() {
            position.move(velocity)
        }

        companion object {
            fun parse(line: String): Light {
                val l = line.split(",", "<", ">").filterIndexed { index, _ -> setOf(1,2,4,5).contains(index) }
                    .map { it.trim().toInt() }
                return Light(Position(l[0], l[1]), Velocity(l[2], l[3]))
            }
        }
    }

    override fun executePart1(name: String): Any {
        val lights = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { Light.parse(it) }
        var i = 0
        while (lights.maxOf { it.position.y } - lights.minOf { it.position.y } > 10) {
            lights.forEach { it.move() }
            i++
        }
        IntRange(lights.minOf { it.position.y }, lights.maxOf { it.position.y }).forEach { y ->
            IntRange(lights.minOf { it.position.x }, lights.maxOf { it.position.x }).forEach { x ->
                if (lights.any { it.position == Position(x, y) }) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
        return i
    }

    override fun executePart2(name: String): Any {
        return executePart1(name)
    }
}