package aoc2020


import getInputAsLines
import util.Day

class Day1: Day("1") {

    private fun parseInput(name: String): IntArray {
        return getInputAsLines(name)
            .filter { x -> x.isNotBlank() }
            .map { x -> Integer.parseInt(x) }
            .sortedWith(compareBy {it})
            .toIntArray()
    }

    override fun executePart1(name: String): Long {
        val coll = parseInput(name)

        for (i in coll.indices) {
            for (j in coll.size-1 downTo i+1) {
                val x = coll[i]
                val y = coll[j]
                if (x + y == 2020) {
                    return x * y.toLong()
                } else if (x + y < 2020) {
                    break
                }
            }
        }
        return -1
    }

    override fun executePart2(name: String): Long {
        val coll = parseInput(name)

        for (i in coll.indices) {
            for (j in coll.size-1 downTo i+1) {
                for (k in i+1 until j-1) {
                    val x = coll[i]
                    val y = coll[j]
                    val z = coll[k]
                    if (x + y + z == 2020) {
                        return x * y * z.toLong()
                    }
                }
            }
        }
        return -1
    }

}