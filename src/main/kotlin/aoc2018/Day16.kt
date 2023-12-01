package aoc2018

import getInputAsLines
import splitBy
import util.Day
import java.util.LinkedList

private val COMMANDS = listOf("addr", "addi", "mulr", "muli", "banr", "bani", "borr", "bori", "setr", "seti", "gtir", "gtri", "gtrr", "eqri", "eqir", "eqrr")

class Day16: Day("16") {

    data class Computer(val registers: IntArray = IntArray(4) { 0 }) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Computer

            if (!registers.contentEquals(other.registers)) return false

            return true
        }

        override fun hashCode(): Int {
            return registers.contentHashCode()
        }
    }

    private fun executeCommand(command: Int, a: Int, b: Int, c: Int, registers: IntArray, commandoMap: Map<Int, String>): IntArray {
        return executeCommand(commandoMap[command]!!, a, b, c, registers)
    }

    private fun executeCommand(command: String, a: Int, b: Int, c: Int, registers: IntArray): IntArray {
        val after = registers.copyOf()
        after[c] = when (command) {
            "addr" ->  registers[a] + registers[b]
            "addi" -> registers[a] + b
            "mulr" -> registers[a] * registers[b]
            "muli" -> registers[a] * b
            "banr" -> (registers[a] and registers[b])
            "bani" -> (registers[a] and b)
            "borr" -> (registers[a] or registers[b])
            "bori" -> (registers[a] or b)
            "setr" -> registers[a]
            "seti" -> a
            "gtir" -> if (a > registers[b]) 1 else 0
            "gtri" -> if (registers[a] > b) 1 else 0
            "gtrr" -> if (registers[a] > registers[b]) 1 else 0
            "eqir" -> if (a == registers[b]) 1 else 0
            "eqri" -> if (registers[a] == b) 1 else 0
            "eqrr" -> if (registers[a] == registers[b]) 1 else 0
            else -> registers[c]
        }
        return after
    }

    private fun parseExample(example: List<String>): Triple<IntArray, List<Int>, IntArray> {
        val (a, b, c) = example
        val before = a.split(": ")[1].trim().dropLast(1).drop(1).split(",").map { it.trim().toInt() }.toIntArray()
        val command = b.split(" ").map { it.trim().toInt() }
        val after = c.split(": ")[1].trim().dropLast(1).drop(1).split(",").map { it.trim().toInt() }.toIntArray()
        return Triple(before, command, after)
    }

    private fun findMatchingCommands(example: Triple<IntArray, List<Int>, IntArray>): List<String> {
        val (before, d, after) = example
        val (_, a, b, c) = d
        return COMMANDS.filter { executeCommand(it, a, b, c, before).contentEquals(after) }
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name)
            .splitBy { it.isEmpty() }
            .filter { it.isNotEmpty() && it.first().isNotEmpty() }
            .takeWhile { it.first().startsWith("Before") }
            .map { parseExample(it) }
            .count { findMatchingCommands(it).size >= 3 }
    }

    private fun commandMap(name: String): Map<Int, String> {
        val commandToNumber = getInputAsLines(name)
            .splitBy { it.isEmpty() }
            .asSequence()
            .filter { it.isNotEmpty() && it.first().isNotEmpty() }
            .takeWhile { it.first().startsWith("Before") }
            .map { parseExample(it) }
            .map { it.second.first() to findMatchingCommands(it) }
            .groupBy { it.first }.mapValues { it.value.map { x -> x.second } }
            .mapValues { ps -> ps.value.reduce { acc, strings -> acc.filter { strings.contains(it) } }.toSet() }
        val res = mutableSetOf<Pair<Int, String>>()
        val queue = LinkedList<Pair<Int, MutableSet<String>>>()
        queue.addAll(commandToNumber.entries.map { it.toPair() }.map { it.first to it.second.toMutableSet() })
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.second.size == 1) {
                res.add(current.first to current.second.first())
                queue.forEach { it.second.remove(current.second.first()) }
            } else {
                queue.addLast(current)
            }
        }
        return res.toMap()
    }

    override fun executePart2(name: String): Any {
        val commandoMap = commandMap(name)
        val code = getInputAsLines(name)
            .splitBy { it.isEmpty() }
            .filter { it.isNotEmpty() && it.first().isNotEmpty() }
            .dropWhile { it.first().contains("Before") }
            .first()
            .map { it.split(" ").map { x -> x.trim().toInt() } }
        var registers = IntArray(4) { 0 }
        for ((d,a,b,c) in code) {
            registers = executeCommand(d, a, b, c, registers, commandoMap)
        }
        return registers[0]
    }
}

