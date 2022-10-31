package util

import aoc2019.Day18
import java.util.PriorityQueue

class Dijkstra(private val edges: Set<DijkstraEdge>, private val startNodeValue: String) {

    private val nodes = edges.flatMap { it.nodes }.toSet()
    private val startNode = nodes.first { it.value == startNodeValue }

    fun findShortestPathTo(endNodeValue: String): Int {
        val endNode = nodes.first { it.value == endNodeValue }
        nodes.forEach { it.distance = Int.MAX_VALUE }
        startNode.distance = 0

        val notVisited = mutableSetOf<DijkstraNode>()
        notVisited.addAll(nodes)

        while (notVisited.isNotEmpty()) {
            val u = notVisited.minByOrNull { it.distance }!!
            notVisited.remove(u)
            for ((v,d) in u.getNeighbours(edges)) {
                if (notVisited.contains(v)) {
                    val alternative = u.distance + d
                    if (alternative < v.distance) {
                        v.distance = alternative
                        v.successor = u
                    }
                }
            }
        }
        return endNode.distance
    }
}

class DijkstraEdge(val nodes: Set<DijkstraNode>, val weight: Int) {
    override fun toString() = "${nodes.toList()[0]} <- $weight -> ${nodes.toList()[1]}"
}

class DijkstraNode(val id: Any, val value: String, var distance: Int, var successor: DijkstraNode?) {

    override fun toString() = value

    fun getNeighbours(edges: Set<DijkstraEdge>): Map<DijkstraNode, Int> {
        return edges
            .filter { it.nodes.contains(this) }
            .associate { it.nodes.first { n -> n != this } to it.weight }
    }

    override fun equals(other: Any?): Boolean {
        if (other is DijkstraNode) {
            return this.id == other.id
        } else {
            return false
        }
    }

    override fun hashCode(): Int = id.hashCode()
}