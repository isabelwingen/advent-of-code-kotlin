package aoc2019


import getInputAsLines
import util.Day

class Day6: Day("6") {

    private data class Orbit(val center: String, val planet: String)

    private fun getChildren(parent: String, graph: Collection<Orbit>): Set<String> {
        return graph
            .filter { it.center == parent }
            .map { it.planet }
            .toSet()
    }

    private fun getParent(child: String, graph: Collection<Orbit>): String {
        return graph
            .filter { it.planet == child }
            .map { it.center }
            .first()
    }

    private fun getParents(node: String, allNodes: Collection<Orbit>): Set<String> {
        return if (node == "COM") {
            setOf()
        } else {
            val parent = getParent(node, allNodes)
            getParents(parent, allNodes) + parent
        }
    }

    private fun parseInput(name: String): List<Orbit> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.split(")") }
            .map { Orbit(it[0], it[1]) }
    }

    override fun executePart1(name: String): Int {
        val graph = parseInput(name)
        var nodes = setOf("COM")
        val seen = mutableSetOf<String>()
        var depth = 0
        var res = 0
        while (nodes.isNotEmpty()) {
            res += (nodes.size * depth)
            seen.addAll(nodes)
            depth++
            nodes = nodes.flatMap { getChildren(it, graph) }.filterNot { seen.contains(it) }.toSet()
        }
        return res
    }

    override fun expectedResultPart1() = 322508

    override fun executePart2(name: String): Int {
        val graph = parseInput(name)
        val yourParents = getParents("YOU", graph)
        val santasParents = getParents("SAN", graph)
        val intersection = yourParents.intersect(santasParents).last()
        val a = yourParents.reversed().indexOf(intersection)
        val b = santasParents.reversed().indexOf(intersection)
        return a + b
    }

    override fun expectedResultPart2() = 496
}