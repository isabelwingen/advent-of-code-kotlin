package aoc2020

import getInputAsLines
import util.Day

class Day21: Day("21") {

    private fun parseInput(name: String): List<Pair<List<String>, List<String>>> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.split("(contains ") }
            .map { it[0] to it[1] }
            .map { it -> it.first.split(" ").filter { it.isNotBlank() } to it.second.replace(")", "").split(", ") }
    }

    private fun getMapping(lines: List<Pair<List<String>, List<String>>>): List<Pair<String, Set<String>>> {
        val mapping = mutableSetOf<Pair<String, List<String>>>()
        for (line in lines) {
            for (allergen in line.second) {
                mapping.add(allergen to line.first)
            }
        }
        return mapping.toSet()
            .sortedBy { it.first }
            .fold(mutableMapOf<String, Set<String>>()) { acc, pair ->
                if (acc.containsKey(pair.first)) {
                    acc[pair.first] = acc[pair.first]!!.intersect(pair.second.toSet())
                } else {
                    acc[pair.first] = pair.second.toSet()
                }
                acc
            }
            .toList()
            .sortedBy { it.second.size }
    }

    private fun ingredientsWithAllergens(mapping: List<Pair<String, Set<String>>>): Map<String, String> {
        val m = mapping.map { it.first to it.second.toMutableSet() }.toMutableList()
        val result = mutableMapOf<String, String>()
        while (m.isNotEmpty()) {
            val next = m.first { it.second.size == 1 }
            val ingredient = next.second.toList().first()
            result[next.first] = ingredient
            m.remove(next)
            m.forEach { it.second.remove(ingredient) }
        }

        return result.toMap()
    }

    override fun executePart1(name: String): Long {
        val data = parseInput(name)
        val mapping = getMapping(data)

        val withAllergens = ingredientsWithAllergens(mapping)

        return data.flatMap { it.first }.count { !withAllergens.values.toSet().contains(it) }.toLong()

    }

    override fun executePart2(name: String): String {
        val data = parseInput(name)
        val mapping = getMapping(data)

        return ingredientsWithAllergens(mapping)
            .toList()
            .sortedBy { it.first }
            .joinToString(",") { it.second }
    }

}