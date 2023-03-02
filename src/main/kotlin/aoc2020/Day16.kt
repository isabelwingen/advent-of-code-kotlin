package aoc2020

import getInputAsLines
import util.Day

private class Field(val title: String, val ranges: List<IntRange>) {
    override fun toString(): String {
        return "$title: $ranges"
    }
}

class Day16 : Day("16") {
    private class Input(var fields: List<Field>, var myTicket: IntArray, var nearbyTickets: List<IntArray>) {
        override fun toString(): String {
            return "fields: $fields, myTicket: $myTicket, nearbyTickets: $nearbyTickets"
        }
    }

    private fun toField(line: String): Field {
        val title = line.split(": ")[0]
        val ranges = line.split(": ")[1]
            .split(" or ")
            .map { it.split("-") }
            .map { x -> x.map { it.toInt() } }
            .map { IntRange(it[0], it[1]) }
        return Field(title, ranges)
    }

    private fun parseInput(name: String): Input {
        val coll = getInputAsLines(name);
        val x = coll.takeWhile { it.isNotBlank() }
            .map { toField(it) }
        val y = coll.drop(x.size + 2).get(0)
            .split(",")
            .map { it.toInt() }
            .toIntArray()
        val z = coll.drop(x.size + 5).dropLast(1)
            .map { it.split(",") }
            .map { d -> d.map { it.toInt() } }
            .map { it.toIntArray() }
        return Input(x, y, z)
    }


    override fun executePart1(name: String): Long {
        val input = parseInput(name)
        val ranges = input.fields.flatMap { it.ranges }.toSet()
        return input.nearbyTickets
            .map { it.toList() }
            .flatten()
            .filter { !valueIsValid(ranges, it) }
            .sum()
            .toLong()
    }

    private fun valueIsValid(ranges: Collection<IntRange>, field: Int): Boolean {
        return ranges.any { it.contains(field) }
    }

    override fun executePart2(name: String): Long {
        val input = cleanInput(parseInput(name))
        val results = mutableMapOf<Int, List<String>>()
        for (i in input.nearbyTickets[0].indices) {
            val coll = input.nearbyTickets.map { it[i] }
            val fields = input.fields
                .filter { valueIsValidForField(it, coll) }
            results.put(i, fields.map { it.title })
        }
        val sortedRes = results.toList().sortedBy { it.second.size }
        val seen = mutableSetOf<String>()
        val res = mutableListOf<Pair<Int, String>>()
        for (p in sortedRes) {
            val x = p.second.filter { !seen.contains(it) }
            assert(x.size == 1)
            res.add(p.first to x[0])
            seen.add(x[0])
        }
        return res
            .asSequence()
            .filter { it.second.startsWith("departure") }
            .map { it.first }
            .sorted()
            .map { input.myTicket[it] }
            .map { it.toLong() }
            .reduce { a, b -> a * b }
            .toLong()
    }

    private fun valueIsValidForField(field: Field, values: List<Int>): Boolean {
        return values.all { v -> field.ranges.any { it.contains(v) } }
    }

    private fun cleanInput(input: Input): Input {
        val ranges = input.fields.flatMap { it.ranges }
        val validTickets = input.nearbyTickets
            .filter { x -> x.all { valueIsValid(ranges, it) } }
        return Input(input.fields, input.myTicket, validTickets)
    }
}



