package aoc2023

import getInputAsLines
import util.Day
import java.util.SortedMap

class Day15: Day("15") {

    private fun hash(string: String): Int {
        return string.fold(0) { acc, c ->
            ((acc + c.code) * 17).mod(256)
        }
    }
    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true).first().split(",")
            .sumOf { hash(it) }
    }

    override fun executePart2(name: String): Any {
        val operations = getInputAsLines(name, true).first().split(",")
            .map {
                if (it.contains("=")) {
                    val (a, b) = it.split("=")
                    a to b.toInt()
                } else {
                    it.split("-")[0] to -1
                }
            }
        val hashmap = HashMap<Int, MutableMap<String, Int>>()
        (0..255).forEach { hashmap[it] = mutableMapOf() }
        for ((label, lens) in operations) {
            val box = hash(label)
            if (lens == -1) {
                hashmap[box]!!.remove(label)
            } else {
                hashmap[box]!![label] = lens
            }
        }
        return hashmap.filter { (_,v) -> v.isNotEmpty() }.map { (k,v) -> v.values.mapIndexed { i, u -> (k+1) * (i+1) * u }.sum() }.sum()
    }
}