package aoc2019.util

class Dijkstra<E>(private val edges: Set<DijkstraEdge<E>>, private val startNodeId: E) {

    private val nodes = edges.flatMap { it.nodes }.distinct().map { DijkstraNode(it, Int.MAX_VALUE, null) }.toSet()
    private val startNode = nodes.first { it.id == startNodeId }

    private fun getNodeFromId(id: E): DijkstraNode<E> {
        return nodes.first { it.id == id }
    }

    private fun getNeighbours(id: E): Map<DijkstraNode<E>, Int> {
        return edges
            .filter { it.nodes.contains(id) }
            .associate { it.nodes.filter { n -> n != id }.map { n -> getNodeFromId(n) }.first() to it.weight }
    }

    fun findShortestPathTo(endNodeId: E): Int {
        val endNode = nodes.first { it.id == endNodeId }
        startNode.distance = 0

        val notVisited = mutableSetOf<DijkstraNode<E>>()
        notVisited.addAll(nodes)

        while (notVisited.isNotEmpty()) {
            val u = notVisited.minByOrNull { it.distance }!!
            notVisited.remove(u)
            if (u == endNode) {
                break
            }
            for ((v,d) in getNeighbours(u.id)) {
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

class DijkstraEdge<E>(val nodes: Set<E>, val weight: Int) {
    override fun toString() = "${nodes.toList()[0]} <- $weight -> ${nodes.toList()[1]}"

    override fun hashCode() = (nodes to weight).hashCode()

    override fun equals(other: Any?): Boolean {
        return if (other is DijkstraEdge<*>) {
            nodes == other.nodes && weight == other.weight
        } else {
            false
        }
    }
}

class DijkstraNode<E>(val id: E, var distance: Int, var successor: DijkstraNode<E>?) {

    override fun equals(other: Any?): Boolean {
        return if (other is DijkstraNode<*>) {
            this.id == other.id
        } else {
            false
        }
    }

    override fun hashCode(): Int = id.hashCode()
}