package aoc2023

import getInputAsLines
import util.Day
import java.math.BigInteger
import java.util.*
import kotlin.Comparator

class Day25: Day("25") {

    data class Edge(val nodes: Set<String>)

    private fun shortestPathsFrom(nodeA: String, graph: List<Edge>, nodes: Set<String>): Map<String, String> {
        val distance = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE - 1000 }
        distance[nodeA] = 0
        val predecessor = mutableMapOf<String, String>()
        val distanceComparator = Comparator<String> { s1, s2 -> (distance.getValue(s1)).compareTo(distance.getValue(s2)) }

        val queue = PriorityQueue(distanceComparator)
        nodes.forEach { queue.add(it) }
        while (queue.isNotEmpty()) {
            val u = queue.poll()
            val neighbours = graph.filter { it.nodes.contains(u) }.map { it.nodes.first { n -> n != u } }.toSet()
            neighbours.filter { queue.contains(it) }.forEach { v ->
                val alternative = distance.getValue(u) + 1
                if (alternative < distance.getValue(v)) {
                    distance[v] = alternative
                    predecessor[v] = u
                    queue.remove(v)
                    queue.add(v)
                }
            }
        }
        return predecessor.toMap()
    }

    private fun findPath(predecessor: Map<String, String>, nodeB: String): List<Edge> {
        val result = mutableListOf<Edge>()
        var before: String
        var u = nodeB
        while (predecessor[u] != null) {
            before = u
            u = predecessor[u]!!
            result.add(0, Edge(setOf(before, u)))
        }
        return result
    }

    private fun findPaths(startNode: String, endNodes: List<String>, graph: List<Edge>, nodes: Set<String>): List<List<Edge>> {
        val predecessor = shortestPathsFrom(startNode, graph, nodes)
        return endNodes.map { findPath(predecessor, it) }
    }

    private fun getGraphFromInput(name: String): MutableList<Edge> {
        val graph = mutableListOf<Edge>()
        getInputAsLines(name, true)
            .map { it.split(":") }
            .map { it[0].trim() to it[1].split(" ").filter { s -> s.isNotBlank() }.map { s -> s.trim() } }
            .forEach { (a, vs) ->
                vs.forEach { v ->
                    graph.add(Edge(setOf(a, v)))
                }
            }
        return graph
    }

    private fun findEdgesToCut(graph: List<Edge>): List<Edge> {
        val nodes = graph.flatMap { it.nodes }.toSet()
        println("Found ${nodes.size} nodes")
        val pairs = (0..10).flatMap {
            (0..10).map { _ ->
                val a = nodes.random()
                val b = nodes.filter { it != a }.random()
                a to b
            }
        }
            .map { setOf(it.first, it.second).sorted() }
            .map { it[0] to it[1] }
            .toSet()
            .groupBy { it.first }
            .mapValues { it.value.map { x -> x.second }.distinct() }
        println("Check ${pairs.map { it.value.size }.sum()} pairs")
        return pairs.flatMap { findPaths(it.key, it.value, graph, nodes) }
            .flatten()
            .groupBy { it }.mapValues { it.value.size }
            .map { it.key to it.value }
            .sortedBy { it.second }
            .reversed()
            .map { it.first }
            .take(3)
    }

    private fun findGroups(splittedGraph: List<Edge>): BigInteger {
        val numberOfNodes = splittedGraph.flatMap { it.nodes }.toSet().size.toBigInteger()
        val group1 = mutableSetOf(splittedGraph.first().nodes.first())
        while (true) {
            val children = group1
                .flatMap { node ->
                    splittedGraph.filter { it.nodes.contains(node) }
                        .flatMap { it.nodes }
                        .filter { it != node }
                        .toSet()
                }
                .filterNot { group1.contains(it) }
                .toSet()
            if (children.isEmpty()) {
                break;
            } else {
                group1.addAll(children)
            }
        }
        val sizeOfGroup1 = group1.size.toBigInteger()
        return sizeOfGroup1 * (numberOfNodes - sizeOfGroup1)
    }

    override fun executePart1(name: String): Any {
        val graph = getGraphFromInput(name)
        println("Find edges to cut")
        findEdgesToCut(graph).forEach { graph.remove(it) }
        println("Flood groups")
        return findGroups(graph)
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}