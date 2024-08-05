package aoc2017

import getInput
import getInputAsLines
import util.Day
import kotlin.math.min

class Day6: Day("6") {

    private fun redistribute(intArray: IntArray, index: Int, value: Int) {
        if (index == intArray.lastIndex) {
            for (i in 0 until min(value, intArray.size)) {
                intArray[i] += 1
            }
            if (value > intArray.size) {
                redistribute(intArray, intArray.lastIndex, value - intArray.size)
            }

        } else if (index + value <= intArray.lastIndex) {
            for (i in index+1 .. index+value) {
                intArray[i] += 1
            }
        } else {
            for (i in index+1 .. intArray.lastIndex) {
                intArray[i] += 1
            }
            redistribute(intArray, intArray.lastIndex, value - (intArray.lastIndex - index))
        }
    }
    override fun executePart1(name: String): Any {
        val banks = getInput(name).trim()
            .split("\t", " ")
            .map { Integer.valueOf(it.trim()) }
            .toIntArray()
        val seenBefore = mutableSetOf(banks.joinToString(","))
        while (true) {
            val max = banks.maxOf { it }
            val index = banks.indexOf(max)
            banks[index] = 0
            redistribute(banks, index, max)
            val banksStr = banks.joinToString(",")
            if (seenBefore.contains(banksStr)) {
                break
            } else {
                seenBefore.add(banksStr)
            }
        }
        return seenBefore.size
    }

    override fun executePart2(name: String): Any {
        val banks = getInput(name).trim()
            .split("\t", " ")
            .map { Integer.valueOf(it.trim()) }
            .toIntArray()
        var banksStr = banks.joinToString(",")
        val seenBefore = mutableListOf(banksStr)
        while (true) {
            val max = banks.maxOf { it }
            val index = banks.indexOf(max)
            banks[index] = 0
            redistribute(banks, index, max)
            banksStr = banks.joinToString(",")
            if (seenBefore.contains(banksStr)) {
                break
            } else {
                seenBefore.add(banksStr)
            }
        }
        return seenBefore.size - seenBefore.indexOf(banksStr)
    }
}