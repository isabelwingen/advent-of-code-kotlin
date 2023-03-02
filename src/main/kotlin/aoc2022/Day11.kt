package aoc2022

import getInputAsLines
import splitBy
import util.Day
import java.util.*

class Day11: Day("11") {

    private fun parseMonkey(strings: List<String>): Monkey {
        val id = strings[0].split(" ")[1].dropLast(1).toInt()
        val startingItems = strings[1]
            .split(": ")[1]
            .split(", ")
            .map { it.toLong() }
        val operationStrings = strings[2].split(" = ")[1].split(" ")
        val operator = operationStrings[1]
        val left = operationStrings[0]
        val right = operationStrings[2]
        val operation: (Long) -> Long = if (left == "old" && right == "old") {
            if (operator == "+") {
                { it + it }
            } else {
                { it * it }
            }
        } else if (left == "old") {
            if (operator == "+") {
                { it + right.toInt() }
            } else {
                { it * right.toInt() }
            }
        } else if (right == "old") {
            if (operator == "+") {
                { it + left.toInt() }
            } else {
                { it * left.toInt() }
            }
        } else {
            throw IllegalStateException("Invalid operation")
        }
        val testValue = strings[3].split(" ").last().toInt()
        val iftrue  = strings[4].split(" ").last().toInt()
        val iffalse  = strings[5].split(" ").last().toInt()
        return Monkey(id, startingItems, operation, testValue, iftrue, iffalse)
    }

    private fun getMonkeys(name: String): List<Monkey> {
        return getInputAsLines(name)
            .splitBy { it.isEmpty() }
            .filter { !it.all { x -> x.isEmpty() } }
            .map { parseMonkey(it) }
    }

    private fun monkeyEngine(monkeys: List<Monkey>, rounds: Int, worryLevelReduce: (Long) -> (Long) = { it }): Long {
        val items = monkeys.associate { it.id to LinkedList(it.startingItems) }
        val monkeyBusiness = MutableList(monkeys.size) { 0L }
        var round = 0
        while (round < rounds) {
            for (monkey in monkeys.sortedBy { it.id }) {
                while (items[monkey.id]!!.isNotEmpty()) {
                    monkeyBusiness[monkey.id] += 1L
                    val currentItem = items[monkey.id]!!.pop()
                    val newWorry = worryLevelReduce.invoke(monkey.operation.invoke(currentItem))
                    if (newWorry.mod(monkey.test) == 0) {
                        items[monkey.iftrue]!!.add(newWorry)
                    } else {
                        items[monkey.iffalse]!!.add(newWorry)
                    }
                }
            }
            round++
        }
        return monkeyBusiness.sortedDescending().take(2).reduceRight {a,b -> a*b}
    }

    override fun executePart1(name: String): Any {
        return monkeyEngine(getMonkeys(name), 20) { it.div(3) }
    }

    override fun executePart2(name: String): Any {
        val monkeys = getMonkeys(name)
        val div = monkeys.map { it.test.toLong() }.reduceRight { a, b -> a * b }
        return monkeyEngine(monkeys, 10_000) { it.mod(div) }
    }

}

class Monkey(
    val id: Int,
    val startingItems: List<Long>,
    val operation: (Long) -> (Long),
    val test: Int,
    val iftrue: Int,
    val iffalse: Int
)