package aoc2019


import getInputAsLines
import util.Day
import kotlin.math.ceil

class Day14: Day("14") {

    data class Chemical(val quantity: Long, val name: String) {
        override fun toString(): String = "$name($quantity)"
    }

    data class Reaction(val chemical: Chemical, val sources: List<Chemical>)

    private fun parseReaction(reaction: String): Reaction {
        val (left, right) = reaction.split("=>").map { it.trim() }
        val leftsChemicals = left.split(", ").map { it.trim() }
            .map { it.split(" ") }
            .map { Chemical(it[0].toLong(), it[1]) }
        val (quantity, name) = right.split(" ")
        return Reaction(Chemical(quantity.toLong(), name), leftsChemicals)
    }

    private fun parseInput(name: String): List<Reaction> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { parseReaction(it) }
    }

    override fun executePart1(name: String): Long {
        return round(parseInput(name), 1)
    }

    override fun expectedResultPart1() = 1065255L

    private fun round(reactions: List<Reaction>, fuel: Long = 1L): Long {
        val needed = mutableMapOf<String, Long>()
        val having = mutableMapOf<String, Long>()
        var ore = 0L
        needed["FUEL"] = fuel
        while (needed.isNotEmpty()) {
            var (chemical, quantity) = needed.entries.first()
            needed.remove(chemical)
            if (chemical == "ORE") {
                ore += quantity
            } else {
                val alreadyThere = having.getOrDefault(chemical, 0)
                val reaction = reactions.first { it.chemical.name == chemical }
                val throughReaction = reaction.chemical.quantity
                if (alreadyThere >= quantity) {
                    //we do not need a reaction, cause we already have enough
                    having[chemical] = alreadyThere - quantity
                } else {
                    quantity -= alreadyThere
                    val factor = if (throughReaction >= quantity) 1 else ceil(quantity.toDouble() / throughReaction).toLong()
                    having[chemical] = (throughReaction * factor - quantity)
                    reaction.sources.forEach {
                        needed[it.name] = needed.getOrPut(it.name) {0} + it.quantity.toInt() * factor
                    }
                }
            }

        }
        return ore
    }

    override fun executePart2(name: String): Any {
        val reactions = parseInput(name)
        var low = TRILLION / round(reactions, 1)
        var high = low * 10
        while (round(reactions, high) < TRILLION) {
            low = high
            high = 10 * low
        }
        var mid = 0L
        while (low < high - 1) {
            mid = (low + high) / 2
            val ore = round(reactions, mid)
            if (ore < TRILLION) {
                low = mid
            } else if (ore > TRILLION) {
                high = mid
            } else {
                break
            }
        }
        return low
    }

    override fun expectedResultPart2() = 1766154L

    companion object {
        const val TRILLION = 1_000_000_000_000L
    }
}