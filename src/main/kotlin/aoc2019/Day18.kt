package aoc2019

import getInputAsLines
import util.Day
import java.util.LinkedList
import kotlin.math.abs
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class Day18: Day("18") {

    private fun readMaze(name: String): List<List<Char>> {
        return getInputAsLines(name)
            .map { it.map { x -> x } }
            .filter { it.isNotEmpty() }
    }

    data class Graph(val edges: Set<Set<Triple<Int, Int, Char>>>)

    private fun createGraph(maze: List<List<Char>>): Graph {
        val res = mutableSetOf<Set<Triple<Int, Int, Char>>>()
        for (row in maze.indices) {
            for (col in maze[0].indices) {
                val pos = Triple(row, col, maze[row][col])
                if (maze[row][col] != '#') {
                    res.addAll(
                        setOf(row-1 to col, row+1 to col, row to col+1, row to col-1)
                            .filter { maze.indices.contains(it.first) && maze[0].indices.contains(it.second) }
                            .filter { maze[it.first][it.second] != '#' }
                            .map { setOf(pos, Triple(it.first, it.second, maze[it.first][it.second])) })
                }
            }
        }
        return Graph(res.toSet())
    }

    private fun edgeContraction(g: Graph): Graph {
        val allEdges = g.edges.toMutableSet()
        while (true) {
            val nodes = allEdges.flatten().toSet()
            val nodesThatCanBeRemoved = nodes
                .filter { n -> allEdges.filter { edge -> edge.contains(n) }.size == 2 }
                .filter { it.third == '.' }
            if (nodesThatCanBeRemoved.isEmpty()) {
                return Graph(allEdges.toSet());
            } else {
                val toBeRemoved = nodesThatCanBeRemoved.first()
                val edges = allEdges.filter { it.contains(toBeRemoved) }
                assert(edges.size == 2)
                val otherNodes = edges.flatten().filter { it != toBeRemoved }.toSet()
                assert(otherNodes.size == 2)
                allEdges.removeIf { it.contains(toBeRemoved) }
                allEdges.add(otherNodes)
            }
        }
    }

    private fun removeEmptyCulDeSacs(graph: Graph): Graph {
        val culDeSacs = graph.edges
            .flatten()
            .distinct()
            .filter { it.third == '.' }
            .filter { n -> graph.edges.filter { edge -> edge.contains(n) }.size == 1 }
        val newEdges = graph.edges.toMutableSet()
        newEdges.removeIf { e -> culDeSacs.any { e.contains(it) } }
        return Graph(newEdges.toSet())
    }

    private fun removeEmptyNodes(graph: Graph): Graph {
        val edges = graph.edges.toMutableSet()

        while (true) {
            val emptyNodes = edges.flatten().toSet()
                .filter { it.third == '.' }
            if (emptyNodes.isEmpty()) {
                return Graph(edges.toSet())
            } else {
                val toBeRemoved = emptyNodes.first()
                val otherNodes = edges.filter { it.contains(toBeRemoved) }
                    .flatten()
                    .filter { it != toBeRemoved }
                    .toSet()
                edges.removeIf { it.contains(toBeRemoved) }
                for (a in otherNodes) {
                    for (b in otherNodes.filter { it != a }) {
                        edges.add(setOf(a, b))
                    }
                }
            }
        }
    }

    override fun executePart1(name: String): Any {
        return listOf(name)
            .map { readMaze(it) }
            .map { createGraph(it) }
            .map { edgeContraction(it) }
            .map { removeEmptyCulDeSacs(it) }
            .map { removeEmptyNodes(it) }
            .first()
            .edges
            .map { "${it.toList()[0].third} <-> ${it.toList()[1].third}"  }

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