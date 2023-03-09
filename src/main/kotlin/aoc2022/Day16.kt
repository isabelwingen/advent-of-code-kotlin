package aoc2022

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day16: Day("16") {

    private fun splitLine(line: String): Triple<String, Long, List<String>> {
        val x = line.split(" has flow rate=")
        val y = x[1].split("; ")
        val id = x[0].split(" ")[1]
        val flowRate = y[0].toLong()
        val leadTo = y[1].split(" ").drop(4).map { if (it.last() == ',') it.dropLast(1) else it }
        return Triple(id, flowRate, leadTo)
    }

    data class Edge(val a: String, val b: String, val time: Int) {
        override fun toString(): String {
            val s = "$time".padStart(2, ' ').padEnd(3, ' ')
            return "$a <--$s--> $b"
        }

        override fun equals(other: Any?): Boolean {
            return if (other is Edge) {
                setOf(a, b, time) == setOf(other.a, other.b, other.time)
            } else {
                false
            }
        }

        override fun hashCode(): Int {
            return setOf(a, b, time).hashCode()
        }

        fun hasNode(node: String) = a == node || b == node

        fun nodes() = setOf(a, b)
    }

    private fun removeNode(edges: Set<Edge>, node: String): Set<Edge> {
        val edgesWithThatNode = edges.filter { it.hasNode(node) }.toList()
        val newEdges = mutableSetOf<Edge>()
        for (i in edgesWithThatNode.indices) {
            for (j in 0 until i) {
                val a = edgesWithThatNode[i]
                val b = edgesWithThatNode[j]
                val aa = a.nodes().first { it != node }
                val bb = b.nodes().first { it != node }
                newEdges.add(Edge(aa, bb, a.time + b.time))
            }
        }
        edges.filter { !it.hasNode(node) }.forEach { newEdges.add(it) }
        return newEdges.toSet()
    }

    private fun simplifyGraph(edges: Set<Edge>, nodes: Map<String, Long>): Set<Edge> {
        val emptyNodes = nodes.filter { it.value == 0L }.filter { it.key != "AA" }.map { it.key }
        var res = edges
        for (n in emptyNodes) {
            res = removeNode(res, n)
        }
        return res
    }

    private fun findShortestPaths(edges: Set<Edge>, start: String): Any {
        val nodes = edges.flatMap { it.nodes() }.toSet()
        val abstand = nodes.associateWith { Long.MAX_VALUE }.toMutableMap()
        val vorgaenger = mutableMapOf<String, String>()
        abstand[start] = 0
        val queue = LinkedList(nodes)
        while (queue.isNotEmpty()) {
            val u = queue.minByOrNull { abstand[it]!! }!!
            queue.remove(u)
            val neighborEdges = edges.filter { it.hasNode(u) }.associateBy { it.nodes().first { x -> x != u } }
            val neighbors = neighborEdges.keys
            for (v in neighbors) {
                if (queue.contains(v)) {
                    val alternativ = abstand[u]!! + neighborEdges[v]!!.time
                    if (alternativ < abstand[v]!!) {
                        abstand[v] = alternativ
                        vorgaenger[v] = u
                    }
                }
            }

        }
        return abstand
    }

    override fun executePart1(name: String): Any {
        val lineInformation = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { splitLine(it) }
        val nodes = lineInformation.associate { it.first to it.second }
        val edges = lineInformation.flatMap { li -> li.third.map { Edge(li.first, it, 1) } }.toSet()
        val g = simplifyGraph(edges, nodes)
        val n = g.flatMap { it.nodes() }.toSet()
        for (m in n) {
            println(findShortestPaths(g, m))
        }
        return 0
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}