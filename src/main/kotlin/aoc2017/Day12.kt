package aoc2017

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day12: Day("12") {
    private fun parseLine(line: String): Set<Set<Int>> {
        val (l, right) = line.split(" <-> ")
        val left = Integer.valueOf(l.trim())
        return right.split(",").map { Integer.valueOf(it.trim()) }.map {
            setOf(left, it)
        }.toSet()
    }

    private fun findGroup(edges: Set<Set<Int>>, startNode: Int): MutableSet<Int> {
        val flood = mutableSetOf<Int>()
        val queue = LinkedList<Int>()
        queue.add(startNode)
        while (queue.isNotEmpty()) {
            val node = queue.pop()
            flood.add(node)
            edges.asSequence()
                .filter { it.contains(node) }.flatten()
                .filter { it != node }
                .filterNot { flood.contains(it) }
                .filterNot { queue.contains(it) }.toList()
                .forEach { queue.add(it) }
        }
        return flood
    }

    override fun executePart1(name: String): Any {
        val edges = getInputAsLines(name, true)
            .flatMap { parseLine(it) }
            .toSet()
        return findGroup(edges, 0).size
    }

    override fun executePart2(name: String): Any {
        val edges = getInputAsLines(name, true)
            .flatMap { parseLine(it) }
            .toSet()
        val remainingNodes = edges.flatten().toMutableSet()
        val groups = mutableSetOf<Set<Int>>()
        val group0 = findGroup(edges, 0)
        groups.add(group0)
        remainingNodes.removeIf { group0.contains(it) }
        while (remainingNodes.isNotEmpty()) {
            println(remainingNodes.size)
            val nextGroup = findGroup(edges, remainingNodes.first())
            groups.add(nextGroup)
            remainingNodes.removeIf { nextGroup.contains(it) }
        }
        return groups.size
    }
}