package aoc2022

import getInputAsLines
import util.Day

class Day21: Day("21") {

    private fun parseOperation(str: String): List<String> {
        val operator = listOf('+', '-', '/', '*').first { str.contains(it) }
        val (a, b) = str.split(operator)
            .map { it.trim() }
        return listOf(a, operator.toString(), b)
    }

    private fun parseInput(name: String): Pair<MutableMap<String, Long>, MutableMap<String, List<String>>> {
        val pairs = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.split(":") }
            .map { it[0] to it[1].trim() }

        val numberMap = pairs
            .filter { !it.second.contains(" ") }
            .associate { it.first to it.second.toLong() }
            .toMutableMap()
        val opMap = pairs
            .filter { it.second.contains(" ") }
            .associate { it.first to parseOperation(it.second) }
            .toMutableMap()
        return numberMap to opMap
    }


    override fun executePart1(name: String): Long {
        val (numberMap, opMap) = parseInput(name)
        resolve(numberMap, opMap, mutableMapOf(), "root")
        return numberMap["root"]!!
    }

    private fun resolveUpwards(
        numberMap: MutableMap<String, Long>,
        opMap: MutableMap<String, List<String>>,
        reverseMap: MutableMap<String, List<String>>,
        node: String) {

        val x = opMap.filter { it.value.contains(node) }
        x.forEach { r, (a,op,b) ->
            if (a == node) {
                reverseMap[a] = when (op) {
                    "+" -> listOf(r, "-", b)
                    "-" -> listOf(r, "+", b)
                    "*" -> listOf(r, "/", b)
                    "/" -> listOf(r, "*", b)
                    "=" -> listOf(b)
                    else -> throw IllegalStateException()
                }
                resolveUpwards(numberMap, opMap, reverseMap, r)
            } else if (b == node) {
                reverseMap[b] = when (op) {
                    "+" -> listOf(r, "-", a)
                    "-" -> listOf(a, "-", r)
                    "*" -> listOf(r, "/", a)
                    "/" -> listOf(a, "/", r)
                    "=" -> listOf(a)
                    else -> throw IllegalStateException()
                }
                resolveUpwards(numberMap, opMap, reverseMap, r)
            }
        }
    }

    private fun resolve(
        numberMap: MutableMap<String, Long>,
        opMap: MutableMap<String, List<String>>,
        reverseMap: MutableMap<String, List<String>>,
        node: String): Long {
        return if (numberMap.containsKey(node)) {
            numberMap[node]!!
        } else if (reverseMap.containsKey(node) && reverseMap[node]!!.size == 1) {
            val (a) = reverseMap[node]!!
            val aValue = resolve(numberMap, opMap, reverseMap, a)
            reverseMap.remove(node)
            numberMap[node] = aValue
            aValue
        } else if (reverseMap.containsKey(node)) {
            val result = resolveOperation(reverseMap[node]!!, numberMap, opMap, reverseMap)
            reverseMap.remove(node)
            numberMap[node] = result
            result
        } else if (opMap.containsKey(node)) {
            val result = resolveOperation(opMap[node]!!, numberMap, opMap, reverseMap)
            opMap.remove(node)
            numberMap[node] = result
            result
        } else {
            throw IllegalStateException()
        }
    }

    private fun resolveOperation(operation: List<String>, numberMap: MutableMap<String, Long>, opMap: MutableMap<String, List<String>>, reverseMap: MutableMap<String, List<String>>): Long {
        val (a,op,b) = operation
        val aValue = resolve(numberMap, opMap, reverseMap, a)
        val bValue = resolve(numberMap, opMap, reverseMap, b)
        return when (op) {
            "+" -> aValue + bValue
            "-" -> aValue - bValue
            "*" -> aValue * bValue
            "/" -> aValue / bValue
            else -> throw IllegalStateException()
        }
    }

    private fun resolveOperation(aValue: Long, op: String, bValue: Long): Long {
        return when (op) {
            "+" -> aValue + bValue
            "-" -> aValue - bValue
            "*" -> aValue * bValue
            "/" -> aValue / bValue
            else -> throw IllegalStateException()
        }
    }

    override fun executePart2(name: String): Any {
        val (numberMap, opMap) = parseInput(name)
        val (a,_,b) = opMap["root"]!!
        opMap["root"] = listOf(a,"=",b)
        numberMap.remove("humn")

        val reverseMap = mutableMapOf<String, List<String>>()
        resolveUpwards(numberMap, opMap, reverseMap, "humn")
        resolve(numberMap, opMap, reverseMap, "humn")
        return numberMap["humn"]!!
    }
}