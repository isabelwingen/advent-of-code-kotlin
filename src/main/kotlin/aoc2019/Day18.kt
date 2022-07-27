package aoc2019

import getInputAsLines
import util.Day
import java.util.LinkedList
import kotlin.math.abs
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class Day18: Day("18") {

    companion object {
        const val WALL = '#'
        const val EMPTY = '.'
        const val SECTION = '+'
    }

    data class Coordinate(val row: Int, val col: Int) {
        fun goRight(): Coordinate {
            return Coordinate(row, col + 1)
        }

        fun goLeft(): Coordinate {
            return Coordinate(row, col - 1)
        }

        fun goUp(): Coordinate {
            return Coordinate(row - 1, col)
        }

        fun goDown(): Coordinate {
            return Coordinate(row + 1, col)
        }

        fun manhattan(other: Day18.Coordinate): Int {
            return abs(this.col - other.col) + abs(this.row - other.row)
        }
    }

    data class Node(val coordinate: Coordinate, val value: Char)

    private fun readMaze(name: String): List<List<Char>> {
        return getInputAsLines(name)
            .map { it.map { x -> x } }
            .filter { it.isNotEmpty() }
    }

    private fun getNeighbourIndices(row: Int, col: Int, rows: Int, cols: Int): List<Coordinate> {
        val res = mutableListOf<Coordinate>()
        if (row < rows - 1) {
            res.add(Coordinate(row + 1, col))
        }
        if (row > 0) {
            res.add(Coordinate(row - 1, col))
        }
        if (col < cols - 1) {
            res.add(Coordinate(row, col + 1))
        }
        if (col > 0) {
            res.add(Coordinate(row, col - 1))

        }
        return res.toList()
    }

    private fun getNodes(maze: List<List<Char>>): Map<Coordinate, Char> {
        val result = HashMap<Coordinate, Char>()
        for (row in maze.indices) {
            for (col in maze[0].indices) {
                val value = maze[row][col]
                if (value != WALL && value != EMPTY) {
                    result[Coordinate(row, col)] = value
                } else if (value == EMPTY) {
                    val neighbors = getNeighbourIndices(row, col, maze.size, maze[0].size)
                        .map { maze[it.row][it.col] }
                        .count { it != WALL }
                    if (neighbors >= 2) {
                        result[Coordinate(row, col)] = SECTION
                    }
                }
            }
        }
        return result.toMap()
    }

    data class Edge(val start: Coordinate, val end: Coordinate, val weight: Int, val doors: Set<Char> = emptySet())

    private fun getEdges(nodes: Map<Coordinate, Char>, maze: List<List<Char>>): List<Edge> {
        val edges = mutableListOf<Edge>()
        for (nodeA in nodes) {
            for (nodeB in nodes) {
                if (nodeA != nodeB && nodeA.key.manhattan(nodeB.key) == 1) {
                    edges.add(Edge(nodeA.key, nodeB.key, 1))
                }
            }
        }
        return edges.toList()
    }

    private fun findAllDirectNeighbours(start: Coordinate, edges: Set<Edge>, nodes: Map<Coordinate, Char>): List<Edge> {
        val res = mutableListOf<List<Coordinate>>()
        val queue = LinkedList<MutableList<Coordinate>>()
        queue.add(mutableListOf(start))
        while (queue.isNotEmpty()) {
            val path = queue.pop()
            val endOfPath = path.last()
            val successors = edges
                .filter { it.start == endOfPath }
                .filter { !path.toSet().contains(it.end) }
                .map { MutableList(path.size + 1) { i -> if (i < path.size) path[i] else it.end } }
            for (newPath in successors) {
                val newEnd = newPath.last()
                if (nodes[newEnd]!!.isLowerCase() || nodes[newEnd]!! == '@') {
                    res.add(newPath.toList())
                } else {
                    queue.add(newPath)
                }
            }
        }
        val q = res
            .map { path -> Edge(path.first(), path.last(), path.size - 1, path.map { nodes[it]!! }.filter { it.isUpperCase() }.toSet()) }
        return q
    }

    private fun simplifyGraph(nodes: Map<Coordinate, Char>, edges: Set<Edge>): Any {
        val relevantNodes = nodes.filter { it.value.isLowerCase() || it.value == '@' }
        val result = relevantNodes.flatMap { findAllDirectNeighbours(it.key, edges, nodes) }
            .groupBy { it.copy(weight = 0) }
            .mapValues { it.value.minByOrNull { k -> k.weight }!! }
            .values
            .toSet()
        return result
    }

    override fun executePart1(name: String): Any {
        val maze = readMaze(name)
        val nodes = getNodes(maze)
        val edges = getEdges(nodes, maze)
        return measureTimeMillis {
            simplifyGraph(nodes, edges.toSet())
        }
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