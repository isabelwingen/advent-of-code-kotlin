package aoc2020

import getInputAsLines
import util.Day

class Day7 : Day("7") {

    private class Edge(val from: String, val to: String, val value: Int) {
        @Override
        override fun toString(): String {
            return "$from -> $to ($value)"
        }
    }

    private fun parseLine(line: String): List<Edge> {
        val x = line.split("bags contain")
        val source = x[0].trim()
        return if (line.contains("no other bags")) {
            listOf()
        } else {
            x[1]
                .trim()
                .split(",")
                .map { it.trim() }
                .map { it.removeSuffix(".") }
                .map { it.split(" ") }
                .map { Edge(source, it.subList(1, it.count() - 1).joinToString(separator = " "), it[0].toInt()) }
        }
    }

    private fun parseInput(name: String): List<Edge> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { parseLine(it) }
            .reduceRight { a, b -> a + b }
    }

    override fun executePart1(name: String): Any {
        val edges = parseInput(name)
        val res = mutableSetOf<String>()
        var queue = mutableSetOf("shiny gold")
        while (queue.isNotEmpty()) {
            res.addAll(queue)
            queue = edges
                .filter { queue.contains(it.to) }
                .map { it.from }
                .toMutableSet()
        }
        return res.toSet().count() - 1
    }

    override fun expectedResultPart1() = 335

    private fun getPath(edge: Edge, edges: List<Edge>): Int {
        val nextEdges = edges
            .filter { it.from == edge.to }
        return if (nextEdges.isEmpty()) {
            edge.value
        } else {
            edge.value
            +nextEdges
                .map { getPath(it, edges) * edge.value }
                .reduceRight { a, b -> a + b }
        }
    }

    override fun executePart2(name: String): Any {
        val edges = parseInput(name)
        return getPath(Edge("nil", "shiny gold", 1), edges) - 1
    }

    override fun expectedResultPart2() = 2125
}