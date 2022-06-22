package aoc2019

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day18: Day("18") {

    companion object {
        const val WALL = '#'
        const val EMPTY = '.'
    }

    private fun readMaze(name: String): List<List<Char>> {
        return getInputAsLines(name)
            .map { it.map { x -> x } }
            .filter { it.isNotEmpty() }
    }

    private fun getNeighbourIndices(row: Int, col: Int, rows: Int, cols: Int): List<Pair<Int, Int>> {
        val res = mutableListOf<Pair<Int, Int>>()
        if (row < rows - 1) {
            res.add(row + 1 to col)
        }
        if (col < cols - 1) {
            res.add(row to col + 1)
        }
        return res.toList()
    }

    data class Node(val value: Char, val coordinate: Pair<Int, Int>) {
        override fun toString() = "$value/$coordinate"
    }

    data class Edge(val start: Node, val goal: Node, val weight: Int = 1) {
        override fun toString() = "$start ---$weight--> $goal"

        fun transformStartSymbol(): Edge {
            val startSymbol = if (start.value == '@') '.' else start.value
            val endSymbol = if (goal.value == '@') '.' else goal.value
            return Edge(Node(startSymbol, start.coordinate), Node(endSymbol, goal.coordinate), weight)
        }
    }


    private fun getRawGraph(name: String): MutableSet<Edge> {
        val maze = readMaze(name)
        val rows = maze.size
        val cols = maze[0].size
        val graph = mutableSetOf<Edge>()
        for (row in maze.indices) {
            for (col in maze[0].indices) {
                val coord = row to col
                val value = maze[row][col]
                if (value == WALL) {
                    continue
                } else {
                    for ((a,b) in getNeighbourIndices(row, col, rows, cols)) {
                        val neighbor = maze[a][b]
                        if (neighbor != WALL) {
                            graph.add(Edge(Node(value, coord), Node(neighbor, a to b)))
                        }
                    }
                }
            }
        }
        return graph
    }

    private fun neighbourEdges(graph: Set<Edge>, node: Node): List<Edge> {
        return graph
            .filter { it.start == node || it.goal == node}
            .toList()
    }

    private fun simplifyGraph(graph: MutableSet<Edge>): MutableSet<Edge> {
        val allNodes = graph.flatMap { setOf(it.start, it.goal) }.toSet()
        val toBeRemoved = mutableSetOf<Node>()
        for (node in allNodes) {
            val n = neighbourEdges(graph, node)
            if (node.value == EMPTY && n.size == 2) {
                val (q, r) = n
                val weight = q.weight + r.weight
                val (c, d) = setOf(q.start, q.goal, r.start, r.goal).filter { it != node }.toList()
                graph.add(Edge(c, d, weight))
                toBeRemoved.add(node)
            }
        }
        graph.removeIf { toBeRemoved.contains(it.start) || toBeRemoved.contains(it.goal) }
        return graph
    }

    private fun getGraph(name: String): Pair<Node, Set<Edge>> {
        var graph = getRawGraph(name)
        var sizeBefore: Int
        do {
            sizeBefore = graph.size
            graph = simplifyGraph(graph)
        } while (graph.size != sizeBefore)
        val start = graph.flatMap { setOf(it.goal, it.start) }.first { it.value == '@' }
        val edgesWithStart = graph.filter { it.goal.value == '@' || it.start.value == '@' }
        graph.removeIf { it.goal.value == '@' || it.start.value == '@' }
        edgesWithStart.map { it.transformStartSymbol() }.forEach { graph.add(it) }
        return start.copy(value = EMPTY) to graph.toSet()
    }


    override fun executePart1(name: String): Any {
        val graph = getGraph(name)

    }


    override fun expectedResultPart1(): Any {
        TODO("Not yet implemented")
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }

    override fun expectedResultPart2(): Any {
        TODO("Not yet implemented")
    }
}