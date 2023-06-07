package aoc2022

import getInputAsLines
import util.Day
import kotlin.math.abs

class Day9: Day("9") {

    private fun getInput(name: String): List<String> {
        val commands = getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.split(" ") }
            .map { it[0] to it[1].toInt() }
        return commands.flatMap { (a,b) ->  List(b) { a } }
    }

    override fun executePart1(name: String): Long {
        val commands = getInput(name)
        var head = 0 to 0
        var tail = 0 to 0
        val seen = mutableSetOf<Pair<Int, Int>>()
        seen.add(tail)
        for (dir in commands) {
            head = moveHead(head, dir)
            tail = follow(tail, head)
            seen.add(tail)
        }
        return seen.count().toLong()
    }

    private fun moveHead(pos: Pair<Int, Int>, dir: String): Pair<Int, Int> {
        return when (dir) {
            "U" -> pos.plus(0,1)
            "D" -> pos.plus(0,-1)
            "R" -> pos.plus(1,0)
            "L" -> pos.plus(-1,0)
            else -> throw java.lang.IllegalStateException()
        }
    }

    private fun Pair<Int, Int>.plus(a: Int, b: Int) = (this.first + a) to (this.second + b)

    private fun follow(tail: Pair<Int, Int>, head: Pair<Int, Int>): Pair<Int, Int> {
        return if (abs(tail.first - head.first) <= 1 && abs(tail.second - head.second) <= 1) {
            return tail
        } else if (tail.first == head.first) {
            val x = head.second-tail.second
            tail.plus(0, x / abs(x))
        } else if (tail.second == head.second) {
            val x = head.first-tail.first
            tail.plus(x / abs(x), 0)
        } else if (tail.first < head.first && tail.second < head.second) {
            tail.plus(1, 1)
        } else if (tail.first < head.first && tail.second > head.second) {
            tail.plus(1, -1)
        } else if (tail.first > head.first && tail.second < head.second) {
            tail.plus(-1, 1)
        } else {
            tail.plus(-1, -1)
        }
    }

    override fun executePart2(name: String): Long {
        val commands = getInput(name)
        val rope = MutableList(10) { 0 to 0 }
        val seen = mutableSetOf<Pair<Int, Int>>()
        seen.add(0 to 0)
        for (dir in commands) {
            rope[0] = moveHead(rope[0], dir)
            rope[1] = follow(rope[1], rope[0])
            rope[2] = follow(rope[2], rope[1])
            rope[3] = follow(rope[3], rope[2])
            rope[4] = follow(rope[4], rope[3])
            rope[5] = follow(rope[5], rope[4])
            rope[6] = follow(rope[6], rope[5])
            rope[7] = follow(rope[7], rope[6])
            rope[8] = follow(rope[8], rope[7])
            rope[9] = follow(rope[9], rope[8])
            seen.add(rope[9])
        }
        return seen.count().toLong()
    }

}

