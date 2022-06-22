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

    data class Position(val row: Int, val column: Int) {

        override fun toString(): String {
            val x = row.toString().padStart(2, ' ')
            val y = column.toString().padStart(2, ' ')
            return "($x, $y)"
        }
    }

    data class Node(val value: Char, val coordinate: Position) {
        override fun toString() = "$value/$coordinate"
    }

    data class Edge(val start: Position, val goal: Position, val weight: Int = 1) {
        override fun toString(): String {
            val n = weight.toString().length
            val weightString = weight.toString().padStart(4, ' ').padEnd(5, ' ')
            return "$start --$weightString--> $goal"
        }
    }

    data class Graph(val nodes: Set<Node>, val edges: Set<Edge>) {
        override fun toString() = "  nodes:\n\t ${nodes.joinToString("\n\t ") { it.toString() }}" +
                "\n  edges:\n\t ${edges.joinToString("\n\t ") { it.toString() }}"

        fun openDoor(key: Char): Graph {
            val n = nodes
                .map { if (it.value == key) it.copy(value = '.') else it }
                .map { if (it.value.lowercaseChar() == key) it.copy(value = '.') else it }
                .toSet()
            return this.copy(nodes = n)
        }
    }


    private fun getRawGraph(name: String): Graph {
        val maze = readMaze(name)
        val rows = maze.size
        val cols = maze[0].size
        val edges = mutableSetOf<Edge>()
        val nodes = mutableSetOf<Node>()
        for (row in maze.indices) {
            for (col in maze[0].indices) {
                val coord = Position(row, col)
                val value = maze[row][col]
                if (value == WALL) {
                    continue
                } else {
                    nodes.add(Node(value, coord))
                    for ((a,b) in getNeighbourIndices(row, col, rows, cols)) {
                        val neighbor = maze[a][b]
                        if (neighbor != WALL) {
                            edges.add(Edge(coord,Position(a, b)))
                            nodes.add(Node(neighbor, Position(a, b)))
                        }
                    }
                }
            }
        }
        return Graph(nodes.toSet(), edges.toSet())
    }

    private fun simplifyGraph(graph: Graph): Graph {
        val nodes = HashSet<Node>(graph.nodes)
        val edges = HashSet<Edge>(graph.edges)
        for (node in nodes.filter { it.value == '.' }) {
            val neighbourEdges = edges.filter { it.goal == node.coordinate || it.start == node.coordinate }.toSet()
            if (neighbourEdges.size == 2) {
                val (a, b) = neighbourEdges.toList()
                val (c, d) = setOf(a.goal, a.start, b.goal, b.start).filter { it != node.coordinate }.toList()
                edges.add(Edge(c, d, a.weight + b.weight))
                edges.removeIf { neighbourEdges.contains(it) }
                nodes.remove(node)
            }
        }
        edges.toList().forEach { edges.add(Edge(it.goal, it.start, it.weight)) }
        return Graph(nodes.toSet(), edges.toSet())
    }

    private fun getGraph(name: String): Pair<Position, Graph> {
        var g = getRawGraph(name)
        var sizeBefore = 0
        while (g.edges.size != sizeBefore) {
            sizeBefore = g.edges.size
            g = simplifyGraph(g)
        }
        val startNode = g.nodes.first { it.value == '@' }
        val nodes = HashSet<Node>(g.nodes)
        nodes.remove(startNode)
        nodes.add(startNode.copy(value = '.'))
        return startNode.coordinate to g.copy(nodes = nodes)
    }

    data class State(val graph: Graph, val currentPosition: Position, val keys: Set<Char> = setOf(), val steps: Int = 0) {
        override fun toString(): String {
            return "\ngraph:\n$graph\ncurrent Position: $currentPosition\nkeys: $keys\nsteps: $steps\n"
        }
    }

    fun <T> Set<T>.add(elem: T): Set<T> {
        val s = this.toMutableSet()
        s.add(elem)
        return s.toSet()
    }

    private fun nextStates(state: State): Collection<State> {
        val (graph, currentPosition, keys, steps) = state
        val edges = graph.edges.filter { it.start ==  currentPosition }
        return edges
            .mapNotNull{ edge ->
                val goalNode = graph.nodes.first { it.coordinate == edge.goal }
                if (goalNode.value == '.') {
                    State(graph, edge.goal, keys, steps)
                } else if (goalNode.value.isLowerCase()) {
                    val key = goalNode.value
                    val r = State(graph.openDoor(key), edge.goal, keys.add(key), steps + edge.weight)
                    r
                } else {
                    null
                }
            }
     }


    override fun executePart1(name: String): Any {
        val (s, g) = getGraph(name)
        val startState = State(g, s)
        val a = nextStates(startState).first()
        return nextStates(a)
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