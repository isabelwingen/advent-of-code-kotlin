package aoc2019

import getInput
import util.Day

class Day8: Day("8") {

    class Layer(val lines: List<IntArray>) {
        override fun toString(): String {
            return lines.map { it.joinToString("") }.joinToString("\n") { it }
        }
    }

    private fun parseInput(name: String): IntArray {
        return getInput(name)
            .trim()
            .map { it.toString().toInt() }
            .toIntArray()
    }

    override fun executePart1(name: String): Long {
        val input = parseInput(name)
        val layers = input.toList()
            .chunked(25)
            .map { it.toIntArray() }
            .chunked(6)
            .map { Layer(it) }
        val fewestZeros = layers.minByOrNull { layer -> layer.lines.flatMap { t -> t.toList() }.count { it == 0 } }!!
        return fewestZeros.lines.flatMap { t -> t.toList() }.count { it == 1} *
                fewestZeros.lines.flatMap { t -> t.toList() }.count { it == 2}.toLong()
    }

    override fun executePart2(name: String): String {
        val input = parseInput(name)
        val layers = input.toList()
            .chunked(25)
            .map { it.toIntArray() }
            .chunked(6)
            .map { Layer(it) }

        val result = mutableListOf(
            IntArray(25) { -1 },
            IntArray(25) { -1 },
            IntArray(25) { -1 },
            IntArray(25) { -1 },
            IntArray(25) { -1 },
            IntArray(25) { -1 }
        )

        for (layer in layers.reversed()) {
            for (row in 0 until 6) {
                for (column in 0 until 25) {
                    when (layer.lines[row][column]) {
                        0 -> result[row][column] = 0
                        1 -> result[row][column] = 1
                    }
                }
            }
        }

        return "\n" + result
            .map { it.toList().map { t -> if (t == 1) "#" else " " } }
            .joinToString("\n") { it.joinToString("") }
    }

}