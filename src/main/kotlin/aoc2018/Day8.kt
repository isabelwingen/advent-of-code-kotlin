package aoc2018

import getInput
import util.Day

class Day8: Day("8") {

    private fun getNumbers(name: String): IntArray {
        return getInput(name)
            .trim()
            .split(" ")
            .map { it.toInt() }
            .toIntArray()
    }

    private fun parsePart1(numbers: IntArray, depth: Int = 0): Pair<Int, Int> {
        val numberOfChildren = numbers[0]
        val numberOfMetadata = numbers[1]
        println("depth: $depth, $numberOfChildren/$numberOfMetadata")
        return if (numberOfChildren == 0) {
            2+numberOfMetadata to numbers.drop(2).take(numberOfMetadata).sum()
        } else {
            var length = 2
            var metadataSum = 0
            for (i in 0 until numberOfChildren) {
                val (l, m) = parsePart1(numbers.sliceArray(IntRange(length, numbers.lastIndex)), depth+1)
                length += l
                metadataSum += m
            }
            length+numberOfMetadata to metadataSum+numbers.drop(length).take(numberOfMetadata).sum()
        }
    }

    override fun executePart1(name: String): Any {
        val numbers = getNumbers(name)
        return parsePart1(numbers)
    }

    private fun parsePart2(numbers: IntArray, depth: Int = 0): Pair<Int, Int> {
        val numberOfChildren = numbers[0]
        val numberOfMetadata = numbers[1]
        println("depth: $depth, $numberOfChildren/$numberOfMetadata")
        return if (numberOfChildren == 0) {
            2+numberOfMetadata to numbers.drop(2).take(numberOfMetadata).sum()
        } else {
            var length = 2
            val childrenValues = mutableListOf<Int>()
            for (i in 0 until numberOfChildren) {
                val (l, m) = parsePart2(numbers.sliceArray(IntRange(length, numbers.lastIndex)), depth+1)
                length += l
                childrenValues.add(m)
            }
            val metadata = numbers.drop(length).take(numberOfMetadata).map { it - 1 }
                .sumOf { if (childrenValues.indices.contains(it)) childrenValues[it] else 0 }
            length+numberOfMetadata to metadata
        }
    }

    override fun executePart2(name: String): Any {
        val numbers = getNumbers(name)
        return parsePart2(numbers)
    }

}