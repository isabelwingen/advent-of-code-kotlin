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

    private fun moveElement(index: Int, list: LinkedList<Pair<Int, Long>>) {
        val currentPosition = findPosition(index, list)
        val value = list[currentPosition].second
        val newPosition = remainder(currentPosition + value, list.lastIndex)
        list.removeAt(currentPosition)
        list.add(newPosition, index to value)
    }

    private fun findPosition(index: Int, list: LinkedList<Pair<Int, Long>>): Int {
        return list.mapIndexed { i, pair -> i to pair }
            .first { it.second.first == index }
            .first
    }


    override fun executePart1(name: String): Any {
        val input = readInput(name).mapIndexed { index, i -> index to i }
        val list = LinkedList<Pair<Int, Long>>()
        list.addAll(input)
        for (i in input.indices) {
            moveElement(i, list)
        }
        val posOfZero = list.mapIndexed { i, pair -> i to pair }
            .first { it.second.second == 0L }
            .first
        val a = list[(posOfZero + 1000) % list.size].second
        val b = list[(posOfZero + 2000) % list.size].second
        val c = list[(posOfZero + 3000) % list.size].second
        return a+b+c
    }

    override fun executePart2(name: String): Any {
        val input = readInput(name)
            .mapIndexed { index, i -> index to i * 811589153L }
        val list = LinkedList<Pair<Int, Long>>()
        list.addAll(input)
        for (j in 0 until 10) {

            for (i in input.indices) {
                moveElement(i, list)
            }
        }
        val posOfZero = list.mapIndexed { i, pair -> i to pair }
            .first { it.second.second == 0L }
            .first
        val a = list[(posOfZero + 1000) % list.size].second
        val b = list[(posOfZero + 2000) % list.size].second
        val c = list[(posOfZero + 3000) % list.size].second
        return a+b+c
    }
}