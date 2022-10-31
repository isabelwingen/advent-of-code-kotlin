package aoc2019

import getInputAsLines
import util.Day

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

    private fun readHorizontalPortal(maze: List<List<Char>>): List<List<String>> {
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

    override fun executePart1(name: String): Any {
        return listOf(name)
            .asSequence()
            .map { readMaze(it) }
            .map { readHorizontalPortal(it) }
            .map { createGraph(it) }
            .map { it.simplify() }
            .first()
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