package aoc2023

import getInputAsLines
import util.Day
import util.Direction
import util.Position
import java.util.*

class Day23: Day("23") {

    data class Path(val current: Position, val visited: Set<Position> = setOf(current), val value: Char = '.', val length: Int = 0) {

        fun getChildren(grid: List<List<Char>>, part2: Boolean = false): Set<Path> {

            val possibleNeighbours = if (part2) {
                current.getNeighbours()
            } else {
                when (value) {
                    '>' -> setOf(current.move(Direction.RIGHT))
                    '<' -> setOf(current.move(Direction.LEFT))
                    '^' -> setOf(current.move(Direction.UP))
                    'v' -> setOf(current.move(Direction.DOWN))
                    else -> current.getNeighbours()
                }
            }
            return possibleNeighbours.asSequence()
                .map { it to grid.getOrElse(it.row) { emptyList() }.getOrElse(it.col) { '#' } }
                .filterNot { it.second == '#' }
                .filterNot { visited.contains(it.first) }
                .map { Path(it.first, visited + it.first, it.second) }
                .toSet()
        }
    }

    override fun executePart1(name: String): Any {
        val grid = getInputAsLines(name, true)
            .map { it.toCharArray().toList() }
        val startingPoint = Position(0, 1)
        val endingPoing = Position(grid.lastIndex, grid[grid.lastIndex].indexOf('.'))
        val queue = LinkedList<Path>()
        queue.add(Path(startingPoint, value = grid[startingPoint.row][startingPoint.col]))
        val scenicWalks = mutableSetOf<Path>()
        while (queue.isNotEmpty()) {
            println(queue.size)
            val path = queue.poll()
            if (path.current == endingPoing) {
                scenicWalks.add(path)
            } else {
                val children = path.getChildren(grid)
                queue.addAll(children)
            }
        }
        return scenicWalks.maxByOrNull { it.visited.size }!!.visited.count() - 1
    }

    data class Edge(val nodes: Set<Position>, val weight: Int = 1) {
    }

    private fun findPathToNextRelevantNode(startingNode: Position, startingDir: Direction, grid: List<List<Char>>): Edge? {
        var currentNode = startingNode.move(startingDir)
        if (grid[currentNode.row][currentNode.col] == '#') {
            return null
        }
        val seen = mutableSetOf(startingNode)
        while (true) {
            seen.add(currentNode)
            val children = currentNode.getNeighbours()
                .filter { grid[it.row][it.col] != '#' }
                .filterNot { seen.contains(it) }
            if (children.size == 1) {
                currentNode = children.first()
            } else {
                return Edge(setOf(startingNode, currentNode), seen.count() - 1)
            }
        }
    }

    private fun buildGraph(grid: List<List<Char>>, startingNode: Position, endingNode: Position): Set<Edge> {
        var relevantNodes = grid.indices.flatMap { row -> grid[0].indices.map { col -> Position(row, col) } }
            .filter { grid[it.row][it.col] != '#' }
            .filter { it.getNeighbours(grid.indices, grid[0].indices).count { n -> grid[n.row][n.col] != '#' } > 2 }
            .toSet()
        relevantNodes = relevantNodes + setOf(startingNode, endingNode)
        val edges = mutableSetOf<Edge>()
        for (node in relevantNodes) {
            Direction.values().mapNotNull { findPathToNextRelevantNode(node, it, grid) }
                .forEach { edges.add(it) }
        }
        return edges.toSet()
    }

    private fun findLongestWalk(edges: Set<Edge>, startingNode: Position, endingNode: Position): Int {
        val queue = LinkedList<Path>()
        queue.add(Path(startingNode, setOf(startingNode), '.', 0))
        var max = 0
        while (queue.isNotEmpty()) {
            val path = queue.poll()
            if (path.current == endingNode) {
                if (path.length > max) {
                    max = path.length
                    println(max)
                }
            } else {
                edges.filter { it.nodes.contains(path.current) }
                    .map { it.nodes.first { n -> n != path.current } to it.weight }
                    .filterNot { path.visited.contains(it.first) }
                    .map { Path(it.first, path.visited + it.first, '.', path.length + it.second) }
                    .forEach { queue.addFirst(it) }
            }
        }
        return max
    }


    override fun executePart2(name: String): Any {
        var grid = getInputAsLines(name, true)
            .map { it.toCharArray().toList() }
        grid = listOf(grid[0].map { '#' }) + grid + listOf(grid[0].map { '#' })
        val startingNode = Position(1, 1)
        val endingNode = Position(grid.lastIndex-1, grid[grid.lastIndex-1].indexOf('.'))
        return buildGraph(grid, startingNode, endingNode).let { findLongestWalk(it, startingNode, endingNode) }
    }
}