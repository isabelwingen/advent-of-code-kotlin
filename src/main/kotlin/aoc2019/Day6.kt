package aoc2019

import getResourceAsList

data class Orbit(val center: String, val planet: String)

private fun parseInput(name: String = "2019/day6.txt"): List<Orbit> {
    return getResourceAsList(name)
        .filter { it.isNotBlank() }
        .map { it.split(")") }
        .map { Orbit(it[0], it[1]) }
}

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

fun executeDay6Part1(name: String = "2019/day6.txt"): Int {
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

fun getParents(node: String, allNodes: Collection<Orbit>): Set<String> {
    if (node == "COM") {
        return setOf()
    } else {
        val parent = getParent(node, allNodes)
        return getParents(parent, allNodes) + parent
    }
}

fun executeDay6Part2(name: String = "2019/day6.txt"): Int {
    val graph = parseInput(name)
    val yourParents = getParents("YOU", graph)
    val santasParents = getParents("SAN", graph)
    val intersection = yourParents.intersect(santasParents).last()
    val a = yourParents.reversed().indexOf(intersection)
    val b = santasParents.reversed().indexOf(intersection)
    return a + b
}