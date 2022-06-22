package aoc2019

import getInputAsLines
import util.Day
import java.util.LinkedList
import java.util.Stack

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

    private fun nextStates(state: State): List<State> {
        val (graph, currentPosition, keys, steps) = state

        val queue = LinkedList<MutableList<Edge>>()
        val nodesSeen = mutableSetOf<Position>()
        val paths = mutableSetOf<List<Edge>>()
        queue.add(mutableListOf(Edge(Position(-1, -1), currentPosition, 0)))
        while (queue.isNotEmpty()) {
            val path = queue.pop()
            val goal = path.last().goal
            nodesSeen.add(goal)
            val goalNode = graph.nodes.first { it.coordinate == goal }
            if (goalNode.value.isUpperCase()) {
                // there is a door and we cannot go on
            } else if (goalNode.value.isLowerCase()) {
                // we have found a key
                paths.add(path.toList())
            } else {
                // go on
                val newEdges = graph.edges
                    .filter { it.start == goal }
                    .filter { !nodesSeen.contains(it.goal) }
                    .map {
                        MutableList<Edge>(path.size + 1) { i -> if (i < path.size) path[i] else it }
                    }
               newEdges.forEach { queue.add(it) }
            }
        }
        return paths.map { path ->
            val goalNode = graph.nodes.first { it.coordinate == path.last().goal }
            val key = goalNode.value
            State(graph.openDoor(key), path.last().goal, keys.add(key), steps + path.sumOf { it.weight })
        }

    }

    private fun isThereAlreadyAFasterPath(queue: Stack<State>, state: State): Boolean {
        val sameStates = queue
            .filter { it.graph == state.graph }
            .filter { it.keys == state.keys }
            .filter { it.currentPosition == state.currentPosition }
            .filter { it.steps <= state.steps }
        return sameStates.isNotEmpty()
    }


    override fun executePart1(name: String): Any {
        val (s, g) = getGraph(name)
        val numberOfKeys = g.nodes.count { it.value != EMPTY && it.value.isLowerCase()}
        val startState = State(g, s)
        val queue = Stack<State>()
        queue.add(startState)
        var smallestPath = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val currentState = queue.pop()
            val nextStates = nextStates(currentState).groupBy { it.keys.size == numberOfKeys }
            val x = nextStates[true]
                ?.filter { it.steps < smallestPath }
                ?.minByOrNull { it.steps }
            if (x != null) {
                smallestPath = x.steps
                println(smallestPath)
            }
            nextStates[false]
                ?.filter { it.steps < smallestPath }
                ?.filter { !queue.contains(it) }
                ?.filter { !isThereAlreadyAFasterPath(queue, it) }
                ?.forEach { queue.add(it) }
        }
        return smallestPath
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