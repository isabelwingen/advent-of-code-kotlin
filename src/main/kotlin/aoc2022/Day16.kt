package aoc2022

import getInputAsLines
import util.Day

class Day16: Day("16") {

    private fun splitLine(line: String): Triple<String, Int, List<String>> {
        val x = line.split(" has flow rate=")
        val y = x[1].split("; ")
        val id = x[0].split(" ")[1]
        val flowRate = y[0].toInt()
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

    private fun simplifyGraph(edges: Set<Edge>, nodes: Map<String, Int>): Set<Edge> {
        val emptyNodes = nodes.filter { it.value == 0 }.filter { it.key != "AA" }.map { it.key }
        var res = edges
        for (n in emptyNodes) {
            res = removeNode(res, n)
        }
        return res
    }

    private fun HashMap<String, HashMap<String, Int>>.safe_get(a: String, b: String, default: Int = Int.MAX_VALUE): Int {
        return this[a]!!.getOrDefault(b, default)
    }

    private fun floydWarshall(edges: Set<Edge>): HashMap<String, HashMap<String, Int>> {
        val nodes = edges.flatMap { it.nodes() }.toSet()
        val dist = HashMap<String, HashMap<String, Int>>(nodes.size)
        nodes.forEach { dist[it] = HashMap() }
        for (edge in edges) {
            dist[edge.a]!![edge.b] = edge.time
            dist[edge.b]!![edge.a] = edge.time
        }
        nodes.forEach { dist[it]!![it] = 0 }
        for (k in nodes) {
            for (i in nodes) {
                for (j in nodes) {
                    if (dist.safe_get(i, j).toLong() > dist.safe_get(i, k).toLong() + dist.safe_get(k, j).toLong()) {
                        dist[i]!![j] = dist.safe_get(i, k) + dist.safe_get(k, j)
                    }
                }
            }
        }
        return dist
    }

    data class Point(val current: String, val remainingMinutes: Int, val openValves: Set<String>, val units: Long, val history: List<String>)

    private fun findBestPath(dist: Map<String, Map<String, Int>>, values: Map<String, Int>): Point {
        val stack = MutableList(1) { Point("AA", 30, setOf("AA"), 0, listOf("Start at AA")) }
        var max = stack.first()
        while (stack.isNotEmpty()) {
            val p = stack.removeAt(0)
            val (current, remaining, open, units, history) = p
            val candidates = values.keys.filter { !open.contains(it) }.filter { dist[current]!![it]!! + 1 < remaining }
            if (candidates.isEmpty()) {
                if (units > max.units) {
                    max = p
                }
            } else {
                candidates.forEach {
                    val timeToOpen = dist[current]!![it]!! + 1
                    if (timeToOpen <= remaining) {
                        val unitsProduced = (remaining - timeToOpen) * values[it]!!
                        val openValves = open.toMutableSet()
                        openValves.add(it)
                        stack.add(
                            0,
                            Point(
                                it, remaining - timeToOpen, open + it, units + unitsProduced,
                                history + "Open Valve $it with ${remaining - timeToOpen} minutes left. ($unitsProduced)"))
                    }
                }
            }
        }
        return max
    }

    override fun executePart1(name: String): Point {
        val lineInformation = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { splitLine(it) }
        val nodeValues = lineInformation.associate { it.first to it.second }.toMutableMap()
        val edges = lineInformation.flatMap { li -> li.third.map { Edge(li.first, it, 1) } }.toSet()
        val g = simplifyGraph(edges, nodeValues)
        val nodes = g.flatMap { it.nodes() }.toSet()
        nodeValues.keys.filter { !nodes.contains(it) }.forEach { nodeValues.remove(it) }
        val dist = floydWarshall(g)
        val res = findBestPath(dist, nodeValues)
        return res
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}