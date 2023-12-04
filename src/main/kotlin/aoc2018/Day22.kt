package aoc2018

import util.Day
import java.util.PriorityQueue
import kotlin.math.abs

private const val M = 20183
private const val DEPTH = 6969
private const val TARGET_X = 9
private const val TARGET_Y = 796
private const val OVER_X = 25
private const val OVER_Y = 1

class Day22: Day("22") {

    private fun geologicIndex(x: Int, y: Int, cache: MutableMap<Pair<Int, Int>, Long>): Long {
        if (!cache.containsKey(x to y)) {
            val alt = if (x == TARGET_X && y == TARGET_Y) {
                0L
            } else if (x == 0 && y == 0) {
                0L
            } else if (y == 0) {
                x * 16807L
            } else if (x == 0) {
                y * 48271L
            } else {
                erosionLevel(x-1, y, cache).toLong() * erosionLevel(x, y-1, cache)
            }
            cache[x to y] = alt
        }
        return cache.getValue(x to y)
    }

    private fun erosionLevel(x: Int, y: Int, cache: MutableMap<Pair<Int, Int>, Long>): Int {
        return (geologicIndex(x, y, cache) + DEPTH).mod(M)
    }

    private fun type(x: Int, y: Int, cache: MutableMap<Pair<Int, Int>, Long>, typeCache: MutableMap<Pair<Int, Int>, Int>): Int {
        typeCache.putIfAbsent(x to y, erosionLevel(x, y, cache).mod(3))
        return typeCache.getValue(x to y)
    }

    override fun executePart1(name: String): Any {
        val cache = mutableMapOf<Pair<Int, Int>, Long>()
        return (0 .. TARGET_X).sumOf { x ->
            (0 .. TARGET_Y).sumOf { y ->
                type(x, y, cache, mutableMapOf())
            }
        }
    }

    data class Node(val position: Pair<Int, Int>, val equipment: Int)

    override fun executePart2(name: String): Any {
        val cache = mutableMapOf<Pair<Int, Int>, Long>()
        val types = mutableMapOf<Pair<Int, Int>, Int>()
        val positions = (0 .. TARGET_X + OVER_X).flatMap { x ->
            (0 .. TARGET_Y + OVER_Y).flatMap { y ->
                val t = type(x, y, cache, types)
                allowedTools.getValue(t).map { tool -> Node(x to y, tool) }
            }
        }.toMutableSet()

        val distance = mutableMapOf<Node, Long>().withDefault { Int.MAX_VALUE.toLong() }
        val start = Node(0 to 0, TORCH)
        val target = Node(TARGET_X to TARGET_Y, TORCH)

        distance[start] = 0L

        val compareByDistance: Comparator<Node> = compareBy<Node> { distance.getValue(it) }
            .thenBy { abs(TARGET_X-it.position.first) + abs(TARGET_Y-it.position.second) }

        val queue = PriorityQueue(compareByDistance)
        queue.add(start)
        positions.remove(start)

        while (queue.isNotEmpty()) {
            val u = queue.poll()
            positions.remove(u)
            if (u == target) {
               break
            }
            val (x, y) = u.position
            allowedTools.getValue(types.getValue(u.position)).first { it != u.equipment }
                .let { Node(u.position, it) }
                .let { v ->
                    if (positions.contains(v)) {
                        val alt = distance.getValue(u) + 7
                        if (alt < distance.getValue(v)) {
                            distance[v] = alt
                            queue.add(v)
                        }
                    }
                }
            setOf(x-1 to y, x+1 to y, x to y-1, x to y+1)
                .filter { it.first in 0..TARGET_X+OVER_X }
                .filter { it.second in 0..TARGET_Y+OVER_Y }
                .map { Node(it, u.equipment) }
                .filter { positions.contains(it) }
                .forEach { v ->
                    val alt = distance.getValue(u) + 1
                    if (alt < distance.getValue(v)) {
                        distance[v] = alt
                        queue.add(v)
                    }
                }

        }
        return distance.getValue(target)
    }

    companion object {
        const val TORCH = 0
        const val CLIMBING_GEAR = 1
        const val NEITHER = 2

        const val ROCKY = 0
        const val WET = 1
        const val NARROW = 2

        val allowedTools = mapOf(
            ROCKY to sequenceOf(CLIMBING_GEAR, TORCH),
            WET to sequenceOf(CLIMBING_GEAR, NEITHER),
            NARROW to sequenceOf(NEITHER, TORCH))

    }
}