package aoc2019

import getResourceAsList
import java.util.LinkedList
import kotlin.math.abs

class Day3: Day {

    private enum class Direction {
        R, L, U, D
    }

    private data class Operation(val dir: Direction, val steps: Int)

    private fun toOperation(string: String): Operation {
        val dir = Direction.valueOf(string[0].toString())
        val steps = string.substring(IntRange(1, string.length - 1)).toInt()
        return Operation(dir, steps)
    }

    private fun executePath(commands: List<Operation>): List<Pair<Int, Int>> {
        var x = 0
        var y = 0
        val points = mutableListOf(0 to 0)
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

    private fun parseInput(name: String = "2019/day3.txt"): Pair<List<Operation>, List<Operation>> {
        val coll = getResourceAsList(name).toList()
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


    override fun executePart1(name: String): Int {
        val (first, second) = parseInput(name)
        val firstPath = executePath(first)
        val secondPath = executePath(second)
        return firstPath.toSet().intersect(secondPath.toSet())
            .filter { it.first != 0 ||  it.second != 0 }
            .minOf { abs(it.first) + abs(it.second) }
    }

    override fun expectedResultPart1() = 2180

    override fun executePart2(name: String): Int {
        val (first, second) = parseInput(name)
        val firstPath = executePath(first)
        val secondPath = executePath(second)
        val intersections = firstPath.toSet().intersect(secondPath.toSet())
            .filter { it.first != 0 || it.second != 0 }
        var res = Int.MAX_VALUE
        for (intersection in intersections) {
            val stepsA = firstPath.indexOf(intersection)
            val stepsB = secondPath.indexOf(intersection)
            val sum = stepsA + stepsB
            if (sum < res) {
                res = sum
            }
        }
        return res
    }

    override fun expectedResultPart2() = 112316

    override fun key() = "3"
}
