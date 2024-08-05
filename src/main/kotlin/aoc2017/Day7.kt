package aoc2017

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day7: Day("7") {

    private data class Edge(val left: String, val weight: Int, val right: List<String>) {
        companion object {
            fun parse(line: String): Edge {
                return if (line.contains("->")) {
                    val (left, weight, rights) = line
                        .split("(", ")", "->")
                        .filter { it.isNotBlank() }
                        .map { it.trim() }
                    Edge(left, Integer.valueOf(weight), rights.split(", "))
                } else {
                    val (left, weight) = line
                        .split("(", ")")
                        .filter { it.isNotBlank() }
                        .map { it.trim() }
                    Edge(left, Integer.valueOf(weight), emptyList())
                }
            }
        }
    }

    private data class Node(val value: String, val weight: Int, val children: List<Node>)

    private fun findRoot(edges: List<Edge>): String {
        val onLeftSide = edges.map { it.left }.toSet()
        val onRightSide = edges.flatMap { it.right }.toSet()
        return onLeftSide.filterNot { onRightSide.contains(it) }.first()
    }


    override fun executePart1(name: String): Any {
        val edges = getInputAsLines(name, true)
            .map { Edge.parse(it) }
        return findRoot(edges)
    }

    private fun makeToNode(name: String, edges: List<Edge>): Node {
        val edge = edges.first { it.left == name }
        return if (edge.right.isEmpty()) {
            Node(name, edge.weight, emptyList())
        } else {
            val children = edge.right.map { makeToNode(it, edges) }
            Node(name, edge.weight, children)
        }
    }

    private fun subtreeWeight(node: Node): Int {
        return if (node.children.isEmpty()) {
            node.weight
        } else {
            node.weight + node.children.sumOf { subtreeWeight(it) }
        }
    }

    override fun executePart2(name: String): Any {
        val edges = getInputAsLines(name, true)
            .map { Edge.parse(it) }
        val root = findRoot(edges)
        val rootNode = makeToNode(root, edges)
        val queue = LinkedList<Node>()
        queue.add(rootNode)
        var res = mutableListOf(rootNode)
        while (queue.isNotEmpty()) {
            val node = queue.pop()
            val subtreeWeights = node.children.associateWith { subtreeWeight(it) }
            if (subtreeWeights.values.toSet().size == 1) {
                break
            } else {
                val outlierValue = subtreeWeights.values.groupBy { it }
                    .mapValues { it.value.size }
                    .filter { it.value == 1 }
                    .map { it.key }
                    .first()
                val outlierNode = subtreeWeights.filter { it.value == outlierValue }.map { it.key }.first()
                queue.add(outlierNode)
                res.add(outlierNode)
            }
        }
        res.forEach { node ->
            println("${node.weight} + ${node.children.map { subtreeWeight(it) }}")
        }
        val normalValue = res.dropLast(1).last().children.map { subtreeWeight(it) }
            .groupBy { it }
            .mapValues { it.value.size }
            .filter { it.value != 1 }
            .map { it.key }
            .first()
        val unnormalValue = res.last().weight + res.last().children.sumOf { subtreeWeight(it) }
        val diff = normalValue - unnormalValue
        return res.last().weight + diff
    }
}