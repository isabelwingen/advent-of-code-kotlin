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

    private fun findPath(start: String, end: String, edges: Set<Set<String>>): List<Set<String>>? {
        val queue = LinkedList<List<Set<String>>>()
        edges.filter { it.contains(start) }.forEach { queue.add(listOf(it)) }
        val resultPath = mutableSetOf<List<Set<String>>>()
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
                val possibleNextEdges = edges
                    .filter { it.contains(lastNode) }
                    .filterNot { currentPath.contains(it) }
                    .filterNot { currentPath.flatten().contains(it.first { n -> n != lastNode }) }
                    .filterNot { resultPath.any { rp -> rp.contains(it) } }
                possibleNextEdges.forEach {
                    val next = currentPath + listOf(it)
                    queue.add(next)
                }
            }
        }
        return null
    }

    private fun sameDomain(start: String, end: String, edges: Set<Set<String>>): Boolean {
        println("xxx $start to $end")
        val queue = LinkedList<List<Set<String>>>()
        edges.filter { it.contains(start) }.forEach { queue.add(listOf(it)) }
        val resultPath = mutableSetOf<List<Set<String>>>()
        while (queue.isNotEmpty()) {

            val currentPath = queue.poll()

            if (currentPath.last().contains(end)) {
                queue.removeIf { it.any { edge -> currentPath.contains(edge) } }
                resultPath.add(currentPath)
                if (resultPath.size > 3) {
                    return true
                }
            } else {
                val lastNode = if (currentPath.size == 1) {
                    currentPath.first().first { it != start }
                } else {
                    val beforeLastEdge = currentPath.dropLast(1).last()
                    currentPath.last().first { !beforeLastEdge.contains(it) }
                }
                val possibleNextEdges = edges
                    .filter { it.contains(lastNode) }
                    .filterNot { currentPath.contains(it) }
                    .filterNot { currentPath.flatten().contains(it.first { n -> n != lastNode }) }
                    .filterNot { resultPath.any { rp -> rp.contains(it) } }
                possibleNextEdges.forEach {
                    val next = currentPath + listOf(it)
                    queue.add(next)
                }
            }
        }
        return resultPath.size > 3
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

        val origin = "slm"
        println(edges.flatten().distinct().size)
        val (firstDomain, secondDomain) = edges.flatten().distinct()
            .groupBy { sameDomain(origin, it, edges.toSet()) }
            .mapValues { it.value.size }
            .toSortedMap()
            .values.toList()
        return firstDomain.toLong() * secondDomain.toLong()

        return 0
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}