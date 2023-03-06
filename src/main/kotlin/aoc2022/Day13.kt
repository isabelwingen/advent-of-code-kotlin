package aoc2022

import getInputAsLines
import kotlinx.serialization.json.*
import util.Day
import kotlin.streams.toList

class Day13: Day("13") {

    private fun parse(line: String): Any {
        return Json.parseToJsonElement(line)
    }

    private fun bla(json: JsonElement): List<Any> {
        if (json is JsonArray) {
            return json.stream().map { if (it is JsonPrimitive) it.content.toInt() else bla(it) }.toList()
        }
        throw IllegalStateException()
    }


    private fun getList(line: String): Any {
        val json = Json.parseToJsonElement(line)

        return bla(json)
    }

    private fun rightOrder(a: Any, b: Any): String {
        val p = if (a is Int && b is Int) {
            rightOrderX(a, b)
        } else if (a is List<*> && b is List<*>) {
            rightOrderX(a as List<Any>, b as List<Any>)
        } else if (a is Int && b is List<*>) {
            rightOrderX(listOf(a), b as List<Any>)
        } else if (a is List<*> && b is Int) {
            rightOrderX(a as List<Any>, listOf(b))
        } else {
            throw IllegalStateException("$a and $b")
        }
        //println("Compare $a and $b: $p")
        return p
    }

    private fun rightOrderX(a: Int, b: Int): String {
        return if (a < b) {
            "YES"
        } else if (a > b) {
            "NO"
        } else {
            "NEXT"
        }
    }

    private fun rightOrderX(a: List<Any>, b: List<Any>): String {
        for (i in a.indices) {
            if (!b.indices.contains(i)) {
                return "NO"
            }
            val res = rightOrder(a[i], b[i])
            if (res == "YES") {
                return "YES"
            } else if (res == "NO") {
                return "NO"
            }
        }
        return if (b.size > a.size) {
            "YES"
        } else {
            "NEXT"
        }
    }

    override fun executePart1(name: String): Long {
        val lists = getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { getList(it) }
        return lists.chunked(2)
            .mapIndexed { index, it -> rightOrder(it[0], it[1]) to index + 1 }
            .filter { it.first == "YES" }
            .sumOf { it.second }
            .toLong()
    }

    override fun executePart2(name: String): Long {
        val lines = getInputAsLines(name).toMutableList()
        lines.add("[[2]]")
        lines.add("[[6]]")
        val lists = lines
            .filter { it.isNotEmpty() }
            .map { getList(it) }
        val sorted = lists.sortedWith { a, b ->
            if (rightOrder(a, b) == "YES") {
                -1
            } else {
                1
            }
        }
        val div1 = listOf(listOf(2))
        val div2 = listOf(listOf(6))
        return sorted.indexOf(div1).inc() * sorted.indexOf(div2).inc().toLong()
    }

}