package aoc2018

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day20: Day("20") {

    private fun nextRoom(current: Pair<Int, Int>, dir: Char): Pair<Int, Int> {
        return when (dir) {
            'N' -> {
                current.first - 1 to current.second
            }
            'S' -> {
                current.first + 1 to current.second
            }
            'W' -> {
                current.first to current.second - 1
            }
            'E' -> {
                current.first to current.second + 1
            }
            else -> throw IllegalStateException("Illegal State: $dir")
        }
    }

    private fun getAllRooms(input: String): MutableMap<Pair<Int, Int>, Int> {
        var current = 0 to 0
        val stack = LinkedList<Pair<Int, Int>>()
        val seen = mutableMapOf(current to 0).withDefault { Int.MAX_VALUE }
        input.forEach { command ->
            when (command) {
                in setOf('N', 'E', 'W', 'S') -> {
                    val next = nextRoom(current, command)
                    val alt = seen.getValue(current) + 1
                    if (alt < seen.getValue(next)) {
                        seen[next] = alt
                    }
                    current = next
                }
                '(' -> stack.push(current)
                ')' -> current = stack.pop()!!
                '|' -> current = stack.peek()
            }
        }
        return seen
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)[0].trim().drop(1).dropLast(1)
            .let { getAllRooms(it) }
            .maxByOrNull { it.value }!!.value
    }

    override fun executePart2(name: String): Any {
        return getInputAsLines(name, true)[0].trim().drop(1).dropLast(1)
            .let { getAllRooms(it) }
            .filterValues { it >= 1000 }.count()
    }
}