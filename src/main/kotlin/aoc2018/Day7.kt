package aoc2018

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day7: Day("7") {

    private data class Edge(val from: Char, val to: Char) {
        companion object {
            fun parse(line: String): Edge {
                val split = line.split(" ")
                return Edge(split[1].first(), split[7].first())
            }
        }
    }

    private fun kahn(incomingEdges: Set<Edge>): List<Char> {
        val nodes = incomingEdges.flatMap { listOf(it.from, it.to) }.toSet()
        val l = mutableListOf<Char>()
        val s = LinkedList(nodes.filter { incomingEdges.none { e -> e.to == it } }.sortedBy { it })

        val edges = incomingEdges.toMutableSet()
        while (s.isNotEmpty()) {
            val node = s.pop()
            l.add(node)
            // for each node m with an edge e from n to m do
            val bla = edges.filter { it.from == node }
            for (e in bla) {
                edges.remove(e)
                if (edges.none { it.to == e.to }) {
                    s.add(e.to)
                }

            }
            s.sort()
        }
        return l.toList()
    }

    override fun executePart1(name: String): Any {
        val edges = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { Edge.parse(it) }
            .toSet()
        return kahn(edges).joinToString("")
    }

    private data class Worker(val step: Char, val timeRemaining: Int = step.code-4) {
        fun isIdle() = timeRemaining <= 0
    }

    override fun executePart2(name: String): Any {
        val edges = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { Edge.parse(it) }
            .toMutableSet()
        val openSteps = edges.flatMap { listOf(it.from, it.to) }.toMutableSet()
        var workers = List(5) { Worker('.', 0) }.toMutableList()
        var res = 0
        while(openSteps.isNotEmpty()) {
            val nextPossibleSteps = openSteps.filter { edges.none { edge -> edge.to == it } }.sorted().toMutableList()
            for (worker in workers) {
                if (worker.isIdle()) {
                    if (nextPossibleSteps.isNotEmpty()) {
                        val x = nextPossibleSteps.first()
                        workers[workers.indexOf(worker)] = Worker(x)
                        nextPossibleSteps.removeAt(0)
                        openSteps.remove(x)
                    }
                }
            }

            // go smallest time unit
            val minutes = workers.filterNot { it.isIdle() }.minOf { it.timeRemaining }
            res += minutes
            workers.filter { it.timeRemaining == minutes }.forEach { worker ->
                edges.removeIf { it.from == worker.step }
            }
            workers = workers.map { Worker(it.step, maxOf(0, it.timeRemaining-minutes)) }.toMutableList()
        }
        return res
    }
}