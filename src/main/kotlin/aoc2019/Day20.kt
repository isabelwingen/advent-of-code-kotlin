package aoc2019

import aoc2019.util.Dijkstra
import aoc2019.util.DijkstraEdge
import getInputAsLines
import util.*

class Day20: Day("20") {

    private fun readMaze(name: String): List<List<Char>> {
        val maze = getInputAsLines(name)
            .map { it.map { x -> x} }
            .filter { it.isNotEmpty() }
            .map { it.toMutableList() }
            .toMutableList()
        val maxLineLength = maze.maxOf { it.size }
        return maze
            .map { List(maxLineLength) {p -> if (it.indices.contains(p)) it[p] else ' '} }
            .map { it.toList() }
            .toList()
    }

    private fun readPortals(maze: List<List<Char>>): List<List<String>> {
        val newMaze = MutableList(maze.size) { r -> MutableList(maze[0].size) {c -> maze[r][c].toString() } }
        for (row in maze.indices) {
            for (col in maze[0].indices) {
                val value = maze[row][col]
                if (value.isUpperCase()) {
                    if (maze[0].indices.contains(col - 1) && maze[row][col-1] == '.') {
                        newMaze[row][col-1] = "${value}${maze[row][col+1]}"
                        newMaze[row][col] = " "
                        newMaze[row][col+1] = " "
                    } else if (maze[0].indices.contains(col + 1) && maze[row][col+1] == '.') {
                        newMaze[row][col+1] = "${maze[row][col-1]}${value}"
                        newMaze[row][col] = " "
                        newMaze[row][col-1] = " "
                    } else if (maze.indices.contains(row - 1) && maze[row-1][col] == '.') {
                        newMaze[row-1][col] = "${value}${maze[row+1][col]}"
                        newMaze[row][col] = " "
                        newMaze[row+1][col] = " "
                    } else if (maze.indices.contains(row + 1) && maze[row+1][col] == '.') {
                        newMaze[row+1][col] = "${maze[row-1][col]}${value}"
                        newMaze[row][col] = " "
                        newMaze[row-1][col] = " "
                    }
                }
            }
        }
        return newMaze
            .map { it.map { s -> if (s == " ") "#" else s  }}
            .toList()
    }

    data class Node(val pos: Pair<Int, Int>, val value: String) {
        override fun toString()= value
    }

    data class Graph(private val map: Map<Set<Node>, Int>) {
        private val mmap = map.toMutableMap()

        private fun removeNode(node: Node): Graph {
            mmap.entries.removeIf { it.key.contains(node) }
            return this
        }

        private fun addEdge(nodeA: Node, nodeB: Node, weight: Int): Graph {
            val edge = setOf(nodeA, nodeB)
            if (mmap.getOrDefault(edge, Int.MAX_VALUE) > weight) {
                mmap[edge] = weight
            }
            return this
        }

        fun contractNode(node: Node): Graph {
            val edges = mmap.entries
                .filter { it.key.contains(node) }
                .map { it.key.first { k -> k != node } to it.value }
            for (i in 1 until edges.size) {
                for (j in 0 until i) {
                    addEdge(edges[i].first, edges[j].first, edges[i].second + edges[j].second)
                }
            }
            removeNode(node)
            return this
        }

        fun simplify(): Graph {
            mmap.keys.flatten().distinct().filter { it.value == "." }.forEach { contractNode(it) }
            return this
        }

        override fun toString(): String {
            return mmap.entries
                .map { it.key.toList() to it.value }
                .map { Triple(it.first[0].value, it.first[1].value, it.second) }
                .map { "${it.first} <- ${it.third} -> ${it.second}" }
                .joinToString("\n") { it }
        }

        fun nodes(): List<Node> {
            return mmap.entries.flatMap { it.key }.distinct()
        }

        fun edges(): Map<Set<Node>, Int> {
            return mmap.toMap()
        }

    }

    private fun createGraph(maze: List<List<String>>): Graph {
        val map = mutableMapOf<Set<Node>, Int>()
        // portals
        val portals = maze.flatten().filter { it != "#" }.filter { it != "." }.distinct()
        for (p in portals) {
            val firstR = maze.indexOfFirst { it.contains(p) }
            val firstC = maze[firstR].indexOfFirst { it == p }
            val secondR = maze.indexOfLast { it.contains(p) }
            val secondC = maze[secondR].indexOfLast { it == p }
            val x = firstR to firstC
            val y = secondR to secondC
            if (x != y) {
                map[setOf(Node(x, p), Node(y, p))] = 1
            }
        }
        // direct connections
        for (row in maze.indices) {
            for (col in maze[0].indices) {
                val value = maze[row][col]
                if (value != "#") {
                    val node = Node(row to col, value)
                    if (row > 0 && maze[row-1][col] != "#") {
                        map[setOf(node, Node(row - 1 to col, maze[row - 1][col]))] = 1
                    }
                    if (row < maze.size - 1 && maze[row+1][col] != "#") {
                        map[setOf(node, Node(row + 1 to col, maze[row + 1][col]))] = 1

                    }
                    if (col > 0 && maze[row][col-1] != "#") {
                        map[setOf(node, Node(row to col - 1, maze[row][col - 1]))] = 1
                    }
                    if (col < maze[0].size - 1 && maze[row][col+1] != "#") {
                        map[setOf(node, Node(row to col + 1, maze[row][col + 1]))] = 1

                    }
                }
            }
        }
        return Graph(map.toMap())
    }

    private fun dijkstra(graph: Graph): Int {
        val dijkstraEdges = graph.edges().map { DijkstraEdge(it.key.map { n -> n.pos }.toSet(), it.value) }.toSet()
        val dijkstra = Dijkstra(dijkstraEdges, 2 to 63)
        return dijkstra.findShortestPathTo(116 to 61)
    }

    override fun executePart1(name: String): Long {
        val graph = listOf(name)
            .asSequence()
            .map { readMaze(it) }
            .map { readPortals(it) }
            .map { createGraph(it) }
            .map { it.simplify() }
            .first()
        return dijkstra(graph).toLong()

    }

    private fun directEdges(graph: Graph, level: Int): Set<DijkstraEdge<Triple<Int, Int, Int>>> {
        return graph.edges()
            .filter { it.key.map { n -> n.value }.toSet().size != 1}
            .map { (t, u) -> DijkstraEdge(t.map { x -> Triple(x.pos.first, x.pos.second, level) }.toSet(), u) }
            .toSet()
    }

    private fun edgesToInnerDonuts(graph: Graph, level: Int, isOuter: (Pair<Int, Int>) -> Boolean ): Set<DijkstraEdge<Triple<Int, Int, Int>>> {
        return graph.edges()
            .filter { it.key.map { n -> n.value }.toSet().size == 1 }
            .map { edge ->
                val outerPortal = edge.key.first { isOuter.invoke(it.pos) }
                val innerPortal = edge.key.first { !isOuter.invoke(it.pos) }
                val dijkstraNodes = setOf(
                    Triple(innerPortal.pos.first, innerPortal.pos.second, level),
                    Triple(outerPortal.pos.first, outerPortal.pos.second, level + 1)
                )
                DijkstraEdge(dijkstraNodes, edge.value)
            }
            .toSet()
    }

    private fun edgesToOuterDonuts(graph: Graph, level: Int, isOuter: (Pair<Int, Int>) -> Boolean ): Set<DijkstraEdge<Triple<Int, Int, Int>>> {
        return graph.edges()
            .filter { it.key.map { n -> n.value }.toSet().size == 1 }
            .map { edge ->
                val outerPortal = edge.key.first { isOuter.invoke(it.pos) }
                val innerPortal = edge.key.first { !isOuter.invoke(it.pos) }
                val dijkstraNodes = setOf(
                    Triple(innerPortal.pos.first, innerPortal.pos.second, level - 1),
                    Triple(outerPortal.pos.first, outerPortal.pos.second, level)
                )
                DijkstraEdge(dijkstraNodes, edge.value)
            }
            .toSet()
    }

    private fun edgesFromLevel0(graph: Graph, isOuter: (Pair<Int, Int>) -> Boolean): MutableSet<DijkstraEdge<Triple<Int, Int, Int>>> {
        val dijkstraEdges = mutableSetOf<DijkstraEdge<Triple<Int, Int, Int>>>()
        dijkstraEdges.addAll(directEdges(graph, 0))
        dijkstraEdges.addAll(edgesToInnerDonuts(graph, 0, isOuter))
        return dijkstraEdges
    }

    private fun edgesFromOtherLevels(graph: Graph, isOuter: (Pair<Int, Int>) -> Boolean, level: Int): MutableSet<DijkstraEdge<Triple<Int, Int, Int>>> {
        val dijkstraEdges = mutableSetOf<DijkstraEdge<Triple<Int, Int, Int>>>()
        dijkstraEdges.addAll(directEdges(graph, level))
        dijkstraEdges.addAll(edgesToInnerDonuts(graph, level, isOuter))
        dijkstraEdges.addAll(edgesToOuterDonuts(graph, level, isOuter))
        return dijkstraEdges
    }

    private fun dijkstra2(graph: Graph, isOuter: (Pair<Int, Int>) -> Boolean): Int {
        val dijkstraEdges = mutableSetOf<DijkstraEdge<Triple<Int, Int, Int>>>()
        dijkstraEdges.addAll(edgesFromLevel0(graph, isOuter))
        for (i in 1 until 26) {
            dijkstraEdges.addAll(edgesFromOtherLevels(graph, isOuter, i))
        }
        val startPos = graph.nodes().first { it.value == "AA" }.pos
        val endPos = graph.nodes().first { it.value == "ZZ" }.pos
        return Dijkstra(dijkstraEdges.toSet(), Triple(startPos.first, startPos.second, 0))
            .findShortestPathTo(Triple(endPos.first, endPos.second, 0))
    }

    override fun executePart2(name: String): Long {
        val maze = listOf(name)
            .asSequence()
            .map { readMaze(it) }
            .map { readPortals(it) }
            .map { it.drop(2) }
            .map { it.dropLast(2) }
            .first()
            .map { it.drop(2) }
            .map { it.dropLast(2) }
        val isOuter = { pos: Pair<Int, Int> -> pos.first == 0 || pos.first == maze.size-1 || pos.second == 0 || pos.second == maze[0].size-1 }
        val graph = listOf(maze)
            .asSequence()
            .map { createGraph(it) }
            .map { it.simplify() }
            .first()
        return dijkstra2(graph, isOuter).toLong()
    }

}



