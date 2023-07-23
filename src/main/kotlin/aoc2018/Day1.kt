package aoc2018

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day1: Day("1") {

    private fun getInput(name: String): List<Long> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it[0] to it.drop(1).toLong() }
            .map { if (it.first == '+') it.second else -it.second }
    }

    override fun executePart1(name: String): Any {
        return getInput(name).sum()
    }

    override fun executePart2(name: String): Any {
        val queue = LinkedList<Long>()
        val seenFrequencies = mutableSetOf<Long>()
        var currentFrequency = 0L
        seenFrequencies.add(currentFrequency)
        queue.addAll(getInput(name))
        while (queue.isNotEmpty()) {
            val step = queue.pop()
            currentFrequency += step
            if (seenFrequencies.contains(currentFrequency)) {
                return currentFrequency
            } else {
                seenFrequencies.add(currentFrequency)
            }
            if (queue.isEmpty()) {
                queue.addAll(getInput(name))
            }
        }
        return -1
    }
}