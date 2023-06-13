package aoc2022

import getInputAsLines
import util.Day
import java.util.LinkedList
import kotlin.math.abs

class Day20: Day("20") {

    private fun readInput(name: String): List<Long> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.toLong() }
    }

    private fun remainder(a: Long, b: Int): Int {
        return if (a >= 0) {
            (a % b).toInt()
        } else {
            val x = ((abs(a) / b) + 1)
            val y = (-1) * x * b
            (a-y).toInt()
        }
    }

    private fun moveElement(index: Int, list: IntArray, values: LongArray): IntArray {
        val currentPosition = findPosition(index, list)
        val value = values[index]
        val newPosition = remainder(currentPosition + value, list.lastIndex)
        val newList = IntArray(list.size) { list[it] }
        return if (newPosition == currentPosition) {
            newList
        } else if ((newPosition == 0) && (newPosition == newList.lastIndex)) {
            System.arraycopy(list, currentPosition + 1, newList, currentPosition, list.size - currentPosition - 1)
            newList[newList.lastIndex] = index
            newList
        } else if (newPosition > currentPosition) {
            System.arraycopy(list, currentPosition + 1, newList, currentPosition, newPosition - currentPosition)
            newList[newPosition] = index
            newList
        } else {
            newList[newPosition] = index
            System.arraycopy(list, newPosition, newList, newPosition+1, currentPosition - newPosition)
            newList
        }
    }

    private fun findPosition(index: Int, list: IntArray): Int {
        for (i in list) {
            if (list[i] == index) {
                return i
            }
        }
        throw IllegalStateException("Element not found")
    }


    override fun executePart1(name: String): Any {
        val values = readInput(name)
        var list = values.indices.toList().toIntArray()
        for (i in values.indices) {
            list = moveElement(i, list, values.toLongArray())
        }
        val posOfZero = list.mapIndexed { i, pair -> i to pair }
            .first { values[it.second] == 0L }
            .first
        val a = values[list[(posOfZero + 1000) % list.size]]
        val b = values[list[(posOfZero + 2000) % list.size]]
        val c = values[list[(posOfZero + 3000) % list.size]]
        return a+b+c
    }

    override fun executePart2(name: String): Any {
        val values = readInput(name).map { it * 811589153 }.toLongArray()
        var list = values.indices.toList().toIntArray()
        for (j in 0 until 10) {
            for (i in values.indices) {
                list =moveElement(i, list, values)
            }
        }
        val posOfZero = list.mapIndexed { i, pair -> i to pair }
            .first { values[it.second] == 0L }
            .first
        val a = values[list[(posOfZero + 1000) % list.size]]
        val b = values[list[(posOfZero + 2000) % list.size]]
        val c = values[list[(posOfZero + 3000) % list.size]]
        return a+b+c
    }
}