package aoc2019

import getInputAsLines
import util.Day
import java.util.LinkedList
import kotlin.math.abs

class Day3: Day("3") {

    private enum class Direction {
        R, L, U, D
    }

    private data class Operation(val dir: Direction, val steps: Int)

    private fun toOperation(string: String): Operation {
        val dir = Direction.valueOf(string[0].toString())
        val steps = string.substring(IntRange(1, string.length - 1)).toInt()
        return Operation(dir, steps)
    }

    private fun executePath(commands: List<Operation>): List<Pair<Long, Long>> {
        var x = 0L
        var y = 0L
        val points = mutableListOf(0L to 0L)
        val queue = LinkedList(commands)
        while (queue.isNotEmpty()) {
            val command = queue.pop()!!
            when (command.dir) {
                Direction.R -> {
                    for (i in 0 until command.steps) {
                        x++
                        points.add(y to x)
                    }
                }
                Direction.L -> {
                    for (i in 0 until command.steps) {
                        x--
                        points.add(y to x)
                    }
                }
                Direction.U -> {
                    for (i in 0 until command.steps) {
                        y++
                        points.add(y to x)
                    }
                }
                Direction.D ->  {
                    for (i in 0 until command.steps) {
                        y--
                        points.add(y to x)
                    }
                }
            }
        }
        return points.toList()
    }

    private fun parseInput(name: String): Pair<List<Operation>, List<Operation>> {
        val coll = getInputAsLines(name).toList()
        val first = coll[0].split(",")
            .filter { it.isNotBlank() }
            .map { it.trim() }
            .map { toOperation(it) }
        val second = coll[1].split(",")
            .filter { it.isNotBlank() }
            .map { it.trim() }
            .map { toOperation(it) }
        return first to second
    }

    override fun executePart1(name: String): Long {
        val (first, second) = parseInput(name)
        val firstPath = executePath(first)
        val secondPath = executePath(second)
        return firstPath.toSet().intersect(secondPath.toSet())
            .filter { it.first != 0L ||  it.second != 0L }
            .minOf { abs(it.first) + abs(it.second) }
    }

    override fun executePart2(name: String): Long {
        val (first, second) = parseInput(name)
        val firstPath = executePath(first)
        val secondPath = executePath(second)
        val intersections = firstPath.toSet().intersect(secondPath.toSet())
            .filter { it.first != 0L || it.second != 0L }
        var res = Long.MAX_VALUE
        for (intersection in intersections) {
            val stepsA = firstPath.indexOf(intersection)
            val stepsB = secondPath.indexOf(intersection)
            val sum = stepsA + stepsB
            if (sum < res) {
                res = sum.toLong()
            }
        }
        return res
    }

}
