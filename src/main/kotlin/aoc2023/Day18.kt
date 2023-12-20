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

    class PositionMapper {

        private val newRowsToOldRows = BiMap<Int, Int>()
        private val newColsToOldCols = BiMap<Int, Int>()

        private fun transformToOldRow(newRow: Int) = newRowsToOldRows.getValueFromKey(newRow)!!

        fun transformToNewRow(oldRow: Int) = newRowsToOldRows.getKeyFromValue(oldRow)!!

        private fun transformToOldCol(newCol: Int) = newColsToOldCols.getValueFromKey(newCol)!!

        fun transformToNewCol(oldCol: Int) = newColsToOldCols.getKeyFromValue(oldCol)!!

        private fun isRowIdOfNewNode(newRow: Int) = newRowsToOldRows.containsKey(newRow)

        private fun isColIdOfNewNode(newCol: Int) = newColsToOldCols.containsKey(newCol)

        fun storeRowMapping(newRow: Int, oldRow: Int) {
            newRowsToOldRows.put(newRow, oldRow)
        }

        fun storeColMapping(newCol: Int, oldCol: Int) {
            newColsToOldCols.put(newCol, oldCol)
        }

        fun originalHeight(row: Int): Long {
            return if (isRowIdOfNewNode(row)) 1L else abs(transformToOldRow(row+1) - transformToOldRow(row-1)) - 1L
        }

        fun originalWidth(col: Int): Long {
            return if (isColIdOfNewNode(col)) 1L else  abs(transformToOldCol(col+1) - transformToOldCol(col-1)) - 1L

        }
    }

    private fun findSizeOfQuadrant(quadrant: Position, positionMapper: PositionMapper): Long {
        val (row, col) = quadrant
        return positionMapper.originalHeight(row) * positionMapper.originalWidth(col)
    }

    private fun floodFill(graph: Set<Edge>): Set<Position> {
        val interior = mutableSetOf<Position>()
        val queue = LinkedList<Position>()
        val upperLeftCorner = graph.flatMap { setOf(it.a, it.b) }.distinct().filter { it.row == 0 }.minByOrNull { it.col }!!
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

    private fun normalizeIndices(l: List<Int>, storeFunc: (Int, Int) -> Unit) {
        val list = l.distinct().sorted()
        storeFunc(0, list.first())
        var lastIndex = 0
        list.forEachIndexed { index, value ->
            if (index != 0) {
                lastIndex += min(2, abs(list[index - 1] - value))
                storeFunc(lastIndex, value)
            }
        }
    }

    private fun doGraphTransformation(nodes: Set<Position>): PositionMapper {
        val positionMapper = PositionMapper()
        normalizeIndices(nodes.map { it.row }) { k, v -> positionMapper.storeRowMapping(k, v) }
        normalizeIndices(nodes.map { it.col }) { k, v -> positionMapper.storeColMapping(k, v) }
        return positionMapper
    }

    private fun normalizeGraph(edges: Set<Edge>, nodes: Set<Position>): Pair<List<Edge>, PositionMapper> {
        val positionMapper = doGraphTransformation(nodes)
        return edges.map { (a, b) ->
            val newA = Position(positionMapper.transformToNewRow(a.row), positionMapper.transformToNewCol(a.col))
            val newB = Position(positionMapper.transformToNewRow(b.row), positionMapper.transformToNewCol(b.col))
            Edge(newA, newB)
        } to positionMapper
    }

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

    private fun execute(commands: List<Pair<Direction, Int>>): Long {
        val graph = buildGraph(commands)
        val (newEdges, positionMapper) = normalizeGraph(graph.first, graph.second)
        return floodFill(newEdges.toSet()).sumOf { findSizeOfQuadrant(it, positionMapper) } + commands.sumOf { it.second }
    }

    override fun executePart1(name: String): Any {
        val commands = getInputAsLines(name, true)
            .map { it.split(" ").dropLast(1) }
            .map { getDirection(it[0][0]) to it[1].toInt() }
        return execute(commands)
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
        return execute(commands)
    }
}