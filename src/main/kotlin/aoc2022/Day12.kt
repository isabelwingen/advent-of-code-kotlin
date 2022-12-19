package aoc2022

import getInputAsLines
import getNeighbouringIndices
import util.Day
import java.util.PriorityQueue

private fun Char.toElevation(): Int {
    return if (this == 'S') {
        0
    } else if (this == 'E') {
        25
    } else {
        return this.code - 97
    }
}

class Day12: Day("12") {

    private fun getGrid(name: String): List<List<Char>> {
        return getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { it.toCharArray().toList() }

    }

    private fun makeGraph(grid: List<List<Char>>): Set<DirectedEdge> {
        val edges = mutableSetOf<DirectedEdge>()
        for (row in grid.indices) {
            for (col in grid[0].indices) {
                val neighbours = grid.getNeighbouringIndices(row to col)
                    .associateWith { grid[it.first][it.second].toElevation() }
                val value = grid[row][col].toElevation()
                for ((k,v) in neighbours) {
                    if (v <= value + 1) {
                        edges.add(DirectedEdge(Node(row to col, value), Node(k, v)))
                    }
                }
            }
        }
        return edges.toSet()
    }

    private fun dijkstra(start: Node, end: Set<Node>, edges: Set<DirectedEdge>): Int {
        val nodes = edges.flatMap { setOf(it.to, it.from) }.toSet()
        val distance = nodes.associateWith { Int.MAX_VALUE }.toMutableMap()
        distance[start] = 0

        val compareByDistance: Comparator<Node> = compareBy { distance[it]!! }
        val queue = PriorityQueue(compareByDistance)
        queue.add(start)

        val visited = HashSet<Node>()

        while (queue.isNotEmpty()) {
            val current = queue.remove()!!
            visited.add(current)
            if (end.contains(current)) {
                break
            }
            val edgesToNeighbours = edges
                .filter { it.from == current }
                .filter { !visited.contains(it.to) }

            for (e in edgesToNeighbours) {
                val newDistance = distance[current]!! + 1
                if (newDistance < distance[e.to]!!) {
                    distance[e.to] = newDistance
                    queue.remove(e.to)
                    queue.add(e.to)
                }
            }

        }
        return end.minOf { distance[it]!! }
    }

    override fun executePart1(name: String): Any {
        val charGrid = getGrid(name)
        // start node
        val startRow = charGrid.withIndex().first { it.value.contains('S') }
        val start = startRow.index to startRow.value.indexOf('S')

        // end node
        val endRow = charGrid.withIndex().first { it.value.contains('E') }
        val end = endRow.index to endRow.value.indexOf('E')
        val graph = makeGraph(charGrid)
        return dijkstra(Node(start, 0), setOf(Node(end, 25)), graph)

    }

    override fun expectedResultPart1() = 380

    override fun executePart2(name: String): Int {
        val charGrid = getGrid(name)

        // end node
        val endRow = charGrid.withIndex().first { it.value.contains('E') }
        val end = endRow.index to endRow.value.indexOf('E')
        val graph = makeGraph(charGrid)
        val reverseGraph = graph.map { DirectedEdge(it.to, it.from) }.toSet()

        val starts = charGrid
            .asSequence()
            .flatten().withIndex()
            .filter { it.value == 'a' || it.value == 'S' }
            .map { it.index.div(charGrid[0].size) to it.index.mod(charGrid[0].size) }
            .map { Node(it, 0) }
            .toSet()

        return dijkstra(Node(end, 25), starts, reverseGraph)
    }

    override fun expectedResultPart2() = 375
}

data class DirectedEdge(val from: Node, val to: Node)

data class Node(val pos: Pair<Int, Int>, val value: Int)