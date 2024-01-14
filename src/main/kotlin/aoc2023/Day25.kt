package aoc2023

import getInputAsLines
import util.Day
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashSet

class Day25: Day("25") {

    fun neighbours(edges: Set<Set<String>>, vertex: String): Set<String> {
        return edges
            .filter { it.contains(vertex) }
            .flatten()
            .filterNot { it == vertex }
            .toSet()
    }

    private fun isConnected(edges: Set<Set<String>>): Boolean {
        val allNodes = edges.flatten().distinct().toSet()
        var connectedArea = setOf(allNodes.first())
        while (true) {
            val newConnectedArea = connectedArea + connectedArea.flatMap { neighbours(edges, it) }
            if (newConnectedArea == connectedArea) {
                break
            } else {
                connectedArea = newConnectedArea
            }
        }
        return connectedArea == allNodes
    }

    private fun findPath(edges: Set<Set<String>>, start: String, end: String): List<Set<String>>? {
        val queue = LinkedList<List<Set<String>>>()
        edges.filter { it.contains(start) }.forEach { queue.add(listOf(it)) }
        while (queue.isNotEmpty()) {
            val currentPath = queue.poll()
            if (currentPath.last().contains(end)) {
                return currentPath
            } else {
                val lastNode = if (currentPath.size == 1) {
                    currentPath.first().first { it != start }
                } else {
                    val beforeLastEdge = currentPath.dropLast(1).last()
                    currentPath.last().first { !beforeLastEdge.contains(it) }
                }
                val possibleNextEdges = edges.filter { it.contains(lastNode) }.filterNot { currentPath.contains(it) }
                possibleNextEdges.forEach {
                    queue.add(currentPath + listOf(it))
                }
            }
        }
        return null
    }

    private fun sameDomain(from: String, to: String, edges: Set<Set<String>>): Boolean {
        val allEdges = edges.toMutableSet()
        for (i in 0..2) {
            val path = findPath(allEdges, from, to)!!
            allEdges.removeAll(path.toSet())
        }
        return findPath(allEdges, from, to) != null
    }

    override fun executePart1(name: String): Any {
        val edges = mutableSetOf<Set<String>>()
        getInputAsLines(name, true)
            .forEach { line ->
                val (key, values) = line.split(":")
                values.split(" ")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .forEach { value ->
                       edges.add(setOf(key, value))
                    }
            }

        val origin = edges.first().first()
        val (firstDomain, secondDomain) = edges.flatten().distinct()
            .groupBy { sameDomain(origin, it, edges.toSet()) }
            .mapValues { it.value.size }
            .toSortedMap()
            .values.toList()
        return firstDomain.toLong() * secondDomain.toLong()
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}