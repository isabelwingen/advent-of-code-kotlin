package aoc2023

import getInputAsLines
import util.BiMap
import util.Day
import util.Direction
import util.Position
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day18: Day("18") {

    private fun getDirection(char: Char) = when (char) {
        'R' -> Direction.RIGHT
        '0' -> Direction.RIGHT
        'L' -> Direction.LEFT
        '2' -> Direction.LEFT
        'U' -> Direction.UP
        '3' -> Direction.UP
        'D' -> Direction.DOWN
        '1' -> Direction.DOWN
        else -> throw IllegalStateException("No such direction: $char")
    }

    data class Edge(val a: Position, val b: Position) {

        private val colRange = min(a.col, b.col)..max(a.col, b.col)
        private val rowRange =  min(a.row, b.row)..max(a.row, b.row)

        fun contains(position: Position): Boolean {
            return colRange.contains(position.col) && rowRange.contains(position.row)
        }
    }

    data class PositionMapper(
        val newRowsToOldRows: BiMap<Int, Int>,
        val newColsToOldCols: BiMap<Int, Int>,
        val newNodesToOldNodes: BiMap<Position, Position>
    )

    private fun buildGraph(commandos: List<Pair<Direction, Int>>): Pair<Set<Edge>, Set<Position>> {
        var current = Position(0, 0)
        val edges = mutableSetOf<Edge>()
        val nodes = mutableSetOf<Position>()
        for ((dir, length) in commandos) {
            nodes.add(current)
            val nextPos = current.move(dir, length)
            edges.add(Edge(current, nextPos))
            current = nextPos
        }
        return edges.toSet() to nodes.toSet()
    }

    private fun normalizeIndices(l: List<Int>): BiMap<Int, Int> {
        val list = l.distinct().sorted()
        val q = BiMap<Int, Int>()
        q.put(0, list.first())
        var lastIndex = 0
        list.forEachIndexed { index, value ->
            if (index != 0) {
                lastIndex += min(2, abs(list[index - 1] - value))
                q.put(lastIndex, value)
            }
        }
        return q
    }

    private fun nodeMap(nodes: Set<Position>, rowMap: BiMap<Int, Int>, colMap: BiMap<Int, Int>): BiMap<Position, Position> {
        val nodeMap = BiMap<Position, Position>()
        nodes.forEach { node ->
            val newNode = Position(rowMap.getKeyFromValue(node.row)!!, colMap.getKeyFromValue(node.col)!!)
            nodeMap.put(newNode, node)
        }
        return nodeMap
    }

    private fun normalizeGraph(edges: Set<Edge>, nodes: Set<Position>): Pair<List<Edge>, PositionMapper> {
        val newRowsToOldRows = normalizeIndices(nodes.map { it.row })
        val newColsToOldCols = normalizeIndices(nodes.map { it.col })
        val newNodesToOldNodes = nodeMap(nodes, newRowsToOldRows, newColsToOldCols)
        return edges.map { (a, b) ->
            Edge(newNodesToOldNodes.getKeyFromValue(a)!!, newNodesToOldNodes.getKeyFromValue(b)!!)
        } to PositionMapper(newRowsToOldRows, newColsToOldCols, newNodesToOldNodes)
    }

    private fun findSizeOfQuadrant(quadrant: Position, positionMapper: PositionMapper): Long {
        val (row, col) = quadrant
        val width = if (positionMapper.newRowsToOldRows.containsKey(row)) {
            1
        } else {
            abs(positionMapper.newRowsToOldRows.getValueFromKey(row+1)!! - positionMapper.newRowsToOldRows.getValueFromKey(row-1)!!) - 1
        }
        val height = if (positionMapper.newColsToOldCols.containsKey(col)) {
            1
        } else {
            abs(positionMapper.newColsToOldCols.getValueFromKey(col+1)!! - positionMapper.newColsToOldCols.getValueFromKey(col-1)!!) - 1
        }
        return width.toLong() * height
    }

    private fun floodFill(graph: Set<Edge>, positionMapper: PositionMapper): Set<Position> {
        val interior = mutableSetOf<Position>()
        val queue = LinkedList<Position>()
        val upperLeftCorner = positionMapper.newNodesToOldNodes.keys().filter { it.row == 0 }.minByOrNull { it.col }!!
        queue.add(Position(upperLeftCorner.row + 1, upperLeftCorner.col + 1))
        while (queue.isNotEmpty()) {
            val current = queue.poll()

            if (!interior.contains(current) && !graph.any { it.contains(current) }) {
                interior.add(current)
                queue.addAll(current.getNeighbours())
            }
        }
        return interior.toSet()
    }


    override fun executePart1(name: String): Any {
        val commands = getInputAsLines(name, true)
            .map { it.split(" ").dropLast(1) }
            .map { getDirection(it[0][0]) to it[1].toInt() }
        val graph = buildGraph(commands)
        val (newEdges, positionMapper) = normalizeGraph(graph.first, graph.second)
        return floodFill(newEdges.toSet(), positionMapper).sumOf { findSizeOfQuadrant(it, positionMapper) } + commands.sumOf { it.second }
    }

    private fun readCommand(line: String): Pair<Direction, Int> {
        val hexString = line.split(" ").last().drop(2).dropLast(1)
        val dir = getDirection(hexString.last())
        val steps = hexString.dropLast(1).toInt(16)
        return dir to steps
    }

    override fun executePart2(name: String): Any {
        val commands = getInputAsLines(name, true)
            .map { readCommand(it) }
        val graph = buildGraph(commands)
        val (newEdges, positionMapper) = normalizeGraph(graph.first, graph.second)
        return floodFill(newEdges.toSet(), positionMapper).sumOf { findSizeOfQuadrant(it, positionMapper) } + commands.sumOf { it.second }
    }
}