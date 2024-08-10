package aoc2017

import util.Day
import java.util.LinkedList

class Day14: Day("14") {
    private fun calculateHashes(): List<String> {
        val key = "wenycdww"
        val knotHashCalculator = Day10()
        return (0..127).map { i ->
            knotHashCalculator.calculateKnotHash("$key-$i").map { Integer.parseInt(it.toString(), 16).toString(2).padStart(4, '0') }.joinToString("")
        }
    }

    override fun executePart1(name: String): Any {
        return calculateHashes().sumOf { it.count { x -> x == '1' } }
    }

    override fun executePart2(name: String): Any {
        val field = calculateHashes().joinToString("").toMutableList().map { it.digitToInt() }.toMutableList()
        var nextNumber = 2
        while (field.contains(1)) {
            val queue = LinkedList<Int>()
            queue.add(field.indexOf(1))
            while (queue.isNotEmpty()) {
                val indexOfOne = queue.pop()
                field[indexOfOne] = nextNumber
                val neighbors = if (indexOfOne.mod(128) == 0) {
                    setOf(indexOfOne+1, indexOfOne-128, indexOfOne+128)
                } else if (indexOfOne.mod(128) == 127) {
                    setOf(indexOfOne-1, indexOfOne-128, indexOfOne+128)
                } else {
                    setOf(indexOfOne-1, indexOfOne+1, indexOfOne-128, indexOfOne+128)
                }
                neighbors
                    .filter { field.indices.contains(it) }
                    .filter { field[it] == 1 }
                    .forEach { queue.add(it) }
            }
            nextNumber++
        }
        return field.toSet().filterNot { it == 0 }.size


    }
}