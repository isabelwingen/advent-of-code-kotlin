package aoc2019

import getResourceAsList
import getResourceAsText

class Day8: Day {

    class Layer(val lines: List<IntArray>) {
        override fun toString(): String {
            return lines.map { it.joinToString("") }.joinToString("\n") { it }
        }
    }

    private fun parseInput(name: String): IntArray {
        return getResourceAsText(name)!!
            .trim()
            .map { it.toString().toInt() }
            .toIntArray()
    }

    override fun executePart1(name: String): Int {
        val input = parseInput(name)
        val layers = input.toList()
            .chunked(25)
            .map { it.toIntArray() }
            .chunked(6)
            .map { Layer(it) }
        val fewestZeros = layers.minByOrNull { layer -> layer.lines.flatMap { t -> t.toList() }.count { it == 0 } }!!
        return fewestZeros.lines.flatMap { t -> t.toList() }.count { it == 1} * fewestZeros.lines.flatMap { t -> t.toList() }.count { it == 2}
    }

    override fun expectedResultPart1() = 1088

    override fun executePart2(name: String): Any {
        val input = parseInput(name)
        val layers = input.toList()
            .chunked(25)
            .map { it.toIntArray() }
            .chunked(6)
            .map { Layer(it) }
        println(layers.first().lines.size)
        println(layers.first().lines.first().size)

        var result = mutableListOf(
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

        return result
            .map { it.toList().map { t -> if (t == 1) "#" else " " } }
            .joinToString("\n") { it.joinToString("") }
    }

    override fun expectedResultPart2()=
            "#     ##  #   ##  # ###  \n" +
            "#    #  # #   ##  # #  # \n" +
            "#    #     # # #### ###  \n" +
            "#    # ##   #  #  # #  # \n" +
            "#    #  #   #  #  # #  # \n" +
            "####  ###   #  #  # ###  "

    override fun key()= "8"
}