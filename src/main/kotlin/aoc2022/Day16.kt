package aoc2022

import getInputAsLines
import util.Day
import org.paukov.combinatorics3.Generator;

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

    private fun simplifyGraph(edges: Set<Edge>, emptyNodes: Set<String>): Set<Edge> {
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

    private fun findBestPath(
        dist: Map<String, Map<String, Int>>,
        values: Map<String, Int>,
        nodes: Set<String> = dist.keys,
        time: Int = 30
    ): Long {
        val stack = MutableList(1) { Point("AA", time, setOf("AA"), 0, listOf("Start at AA")) }
        var max = stack.first().units
        while (stack.isNotEmpty()) {
            val p = stack.removeAt(0)
            val (current, remaining, open, units, history) = p
            val candidates = nodes.filter { !open.contains(it) }.filter { dist[current]!![it]!! + 1 < remaining }
            if (candidates.isEmpty()) {
                if (units > max) {
                    max = p.units
                }
            } else {
                candidates.forEach {
                    val timeToOpen = dist[current]!![it]!! + 1
                    val unitsProduced = (remaining - timeToOpen) * values[it]!!
                    stack.add(
                        0,
                        Point(
                            it, remaining - timeToOpen, open + it, units + unitsProduced,
                            history + "Open Valve $it with ${remaining - timeToOpen} minutes left. ($unitsProduced)"))
                }
            }
        }
        return max
    }

    override fun executePart1(name: String): Long {
        val lineInformation = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { splitLine(it) }
        val allNodeValues = lineInformation
            .associate { it.first to it.second }
        val emptyNodes = allNodeValues.filter { it.value == 0 }.filter { it.key != "AA" }.map { it.key }.toSet()
        val edges = lineInformation.flatMap { li -> li.third.map { Edge(li.first, it, 1) } }.toSet()
        val g = simplifyGraph(edges, emptyNodes)
        val dist = floydWarshall(g)
        return findBestPath(dist, allNodeValues)
    }

    private fun findBestPath2(dist: Map<String, Map<String, Int>>, values: Map<String, Int>): Long {
        val nodes = dist.keys.filter { it != "AA" }.toSet()
        var max = 0L
        for (n in 1 until nodes.size/2 + 1) {
            //println("Calculate max units for n=$n")
            for (a in Generator.combination(nodes).simple(n).map { it.toSet() }) {
                val b = nodes.filter { !a.contains(it) }.toSet()
                val unitsOfA = findBestPath(dist, values, a, 26)
                val unitsOfB = findBestPath(dist, values, b, 26)
                val units = unitsOfA + unitsOfB
                if (units > max) {
                    max = units
                }
            }
            //println("Max units after n=$n: $max")
        }
        return max
    }

    override fun executePart2(name: String): Any {
        val lineInformation = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { splitLine(it) }
        val allNodeValues = lineInformation
            .associate { it.first to it.second }
        val emptyNodes = allNodeValues.filter { it.value == 0 }.filter { it.key != "AA" }.map { it.key }.toSet()
        val edges = lineInformation.flatMap { li -> li.third.map { Edge(li.first, it, 1) } }.toSet()
        val g = simplifyGraph(edges, emptyNodes)
        val dist = floydWarshall(g)
        return findBestPath2(dist, allNodeValues)
    }
}