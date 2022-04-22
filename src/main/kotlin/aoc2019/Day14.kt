package aoc2019

import getResourceAsList
import java.util.LinkedList
import kotlin.math.ceil

class Day14: Day {

    data class Chemical(val quantity: Int, val name: String) {
        override fun toString(): String = "$name($quantity)"
    }

    private fun parseReaction(reaction: String): Pair<List<Chemical>, Chemical> {
        val (left, right) = reaction.split("=>").map { it.trim() }
        val leftsChemicals = left.split(", ").map { it.trim() }
            .map { it.split(" ") }
            .map { Chemical(it[0].toInt(), it[1]) }
        val (quantity, name) = right.split(" ")
        return leftsChemicals to Chemical(quantity.toInt(), name)
    }

    private fun parseInput(name: String): Map<Chemical, List<Chemical>> {
        return getResourceAsList(name)
            .filter { it.isNotBlank() }
            .map { parseReaction(it) }
            .associate { it.second to it.first }
    }

    private fun findSources(name: String, quantity: Int, reactions: Map<Chemical, List<Chemical>>): List<Chemical> {
        val (key, value) = reactions.entries.first { it.key.name == name}
        return if (quantity <= key.quantity) {
            value
        } else {
            val x = ceil(quantity.toDouble() / key.quantity).toInt()
            value.map { Chemical(it.quantity * x, it.name) }
        }
    }

    private fun findDirectParents(name: String, reactions: Map<Chemical, List<Chemical>>): List<String> {
        return reactions.entries.first { it.key.name == name }.value.map { it.name }
    }

    private fun getDepth(name: String, reactions: Map<Chemical, List<Chemical>>): Int {
        if (name == "ORE") {
            return 0
        }
        val parents = findDirectParents(name, reactions)
        return if (parents.all { it == "ORE" }) {
            1
        } else {
            parents.maxOf { getDepth(it, reactions) } + 1
        }
    }

    fun sortByDepth(queue: LinkedList<Pair<String, Int>>, depths: Map<String, Int>) {
        queue.sortByDescending { depths[it.first] }
    }

    fun summarize(queue: LinkedList<Pair<String, Int>>, depths: Map<String, Int>): LinkedList<Pair<String, Int>> {
        val newQueue = LinkedList<Pair<String, Int>>()
        queue
            .groupBy { it.first }
            .mapValues { it.value.sumOf { t -> t.second } }
            .toList()
            .forEach { newQueue.add(it) }
        sortByDepth(newQueue, depths)
        return newQueue
    }

    override fun executePart1(name: String): Int {
        val reactions = parseInput(name)
        val depths = reactions.keys.map { it.name }.associateWith { getDepth(it, reactions) }

        var list = LinkedList<Pair<String, Int>>()
        list.add("FUEL" to 1)
        var count = 0
        while (list.isNotEmpty()) {
            val (chemical, quantity) = list.pop()
            if (chemical == "ORE") {
                count += quantity
            } else {
                findSources(chemical, quantity, reactions)
                    .forEach { list.add(it.name to it.quantity) }
                list = summarize(list, depths)
            }
        }
        return count
    }

    override fun expectedResultPart1() = 1065255

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }

    override fun expectedResultPart2(): Any {
        TODO("Not yet implemented")
    }

    override fun key() = "14"
}