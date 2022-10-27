package aoc2019

import getInputAsLines
import util.Day
import java.util.LinkedList
import java.util.Queue

class Day18: Day("18") {

    private fun readMaze(name: String): List<List<Char>> {
        return getInputAsLines(name)
            .map { it.map { x -> x } }
            .filter { it.isNotEmpty() }
    }

    data class Node(val pos: Pair<Int, Int>, val value: Char)

    data class Graph(private val map: Map<Set<Node>, Int>) {

        private val mmap = map.toMutableMap()

        fun removeNode(node: Node): Graph {
            mmap.entries.removeIf { it.key.contains(node) }
            return this
        }

        fun addEdge(nodeA: Node, nodeB: Node, weight: Int): Graph {
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
            mmap.keys.flatten().distinct().filter { it.value == '.' }.forEach { contractNode(it) }
            return this
        }

        override fun toString(): String {
            return mmap.toString()
        }

    }

    private fun createGraph(maze: List<List<Char>>): Graph {
        val map = mutableMapOf<Set<Node>, Int>()
        for (row in maze.indices) {
            for (col in maze[0].indices) {
                val value = maze[row][col]
                if (value != '#') {
                val node = Node(row to col, value)
                    if (row > 0 && maze[row-1][col] != '#') {
                        map[setOf(node, Node(row - 1 to col, maze[row-1][col]))] = 1
                    }
                    if (row < maze.size - 1 && maze[row+1][col] != '#') {
                        map[setOf(node, Node(row + 1 to col, maze[row+1][col]))] = 1

                    }
                    if (col > 0 && maze[row][col-1] != '#') {
                        map[setOf(node, Node(row to col - 1, maze[row][col-1]))] = 1
                    }
                    if (col < maze[0].size - 1 && maze[row][col+1] != '#') {
                        map[setOf(node, Node(row to col + 1, maze[row][col+1]))] = 1

                    }
                }
            }
        }
        return Graph(map.toMap())
    }


    override fun executePart1(name: String): Any {
        return listOf(name)
            .map { readMaze(it) }
            .map { createGraph(it) }
            .map { it.simplify() }
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