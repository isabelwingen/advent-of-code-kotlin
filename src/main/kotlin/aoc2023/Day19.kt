package aoc2023

import getInputAsLines
import splitBy
import util.Day
import java.util.EnumMap
import java.util.LinkedList
import kotlin.math.max
import kotlin.math.min

class Day19: Day("19") {

    enum class Category {
        X, M, A, S
    }

    data class Part(val map: EnumMap<Category, Int>) {

        companion object {
            fun parse(part: String): Part {
                val (x,m,a,s) = part.drop(1).dropLast(1).split(",")
                    .map { it.split("=")[1].toInt() }
                val map = EnumMap(mapOf(Category.X to x, Category.M to m, Category.A to a, Category.S to s))
                return Part(map)
            }
        }
    }

    data class Rule(val category: Category, val operator: Char, val threshold: Int, val next: String) {
        fun check(part: Part): String? {
            if (operator == '<') {
                if (part.map.getValue(category) < threshold) {
                    return next
                }
            } else {
                if (part.map.getValue(category) > threshold) {
                    return next
                }
            }
            return null
        }

        fun ifChild(): Pair<String, Int> {
            return if (operator == '<') {
                "max" to threshold-1
            } else {
                "min" to threshold+1
            }
        }

        fun elseChild(): Pair<String, Int> {
            return if (operator == '<') {
                "min" to threshold
            } else {
                "max" to threshold
            }
        }

        companion object {
            fun parse(rule: String): Rule {
                val (variable, threshold, goal) = rule.split(":", ">", "<")
                val category = Category.valueOf(variable.uppercase())
                val operator = if (rule.contains("<")) '<' else '>'
                return Rule(category, operator, threshold.toInt(), goal)
            }
        }
    }

    data class Workflow(val id: String, val rules: List<Rule>, val end: String) {

        fun check(part: Part): String {
            for (rule in rules) {
                val res = rule.check(part)
                if (res != null) {
                    return res
                }
            }
            return end
        }

        companion object {
            fun parse(workflow: String): Workflow {
                val (name, value) = workflow.split("{", "}")
                val rules = value.split(",")
                val end = rules.last()
                return Workflow(name, rules.dropLast(1).map { Rule.parse(it) }, end)
            }
        }
    }

    private fun checkParts(workflows: Map<String, Workflow>, parts: List<Part>): Set<Part> {
        val accepted = mutableSetOf<Part>()
        for (part in parts) {
            var nextWorkflowId = "in"
            while (nextWorkflowId != "A" && nextWorkflowId != "R") {
                val nextWorkflow = workflows.getValue(nextWorkflowId)
                nextWorkflowId = nextWorkflow.check(part)
            }
            if (nextWorkflowId == "A") {
                accepted.add(part)
            }
        }
        return accepted.toSet()
    }

    data class UndefinedPart(
        val x: IntRange = 1..4000,
        val m: IntRange = 1..4000,
        val a: IntRange = 1..4000,
        val s: IntRange = 1..4000,
    ) {

        fun setNewMin(minVal: Int, category: Category): UndefinedPart {
            return when (category) {
                Category.X -> copy(x = max(x.first, minVal)..x.last)
                Category.M -> copy(m = max(m.first, minVal)..m.last)
                Category.A -> copy(a = max(a.first, minVal)..a.last)
                Category.S -> copy(s = max(s.first, minVal)..s.last)
            }
        }

        fun setNewMax(maxVal: Int, category: Category): UndefinedPart {
            return when (category) {
                Category.X -> copy(x = x.first..min(x.last, maxVal))
                Category.M -> copy(m = m.first..min(m.last, maxVal))
                Category.A -> copy(a = a.first..min(a.last, maxVal))
                Category.S -> copy(s = s.first..min(s.last, maxVal))
            }
        }

        fun overlaps(other: UndefinedPart): Boolean {
            return (x.contains(other.x.first()) || other.x.contains(x.first())) &&
                    (m.contains(other.m.first()) || other.m.contains(m.first())) &&
                    (a.contains(other.a.first()) || other.a.contains(a.first())) &&
                    (s.contains(other.s.first()) || other.s.contains(s.first()))
        }

        fun size(): Long {
            val length = fun(i: IntRange): Long { return i.toList().size.toLong() }
            return length(x) * length(m) * length(a) * length(s)
        }
    }

    data class QueueItem(val undefinedPart: UndefinedPart, val workflow: Workflow)

    private fun cleanUpCombinations(combinations: Set<UndefinedPart>): Set<UndefinedPart> {
        val cleanedUp = mutableSetOf<UndefinedPart>()
        for (combination in combinations) {
            if (combinations.filter { it != combination }.any {
                    it.x.contains(combination.x.first()) && it.x.contains(combination.x.last) &&
                    it.m.contains(combination.m.first()) && it.m.contains(combination.m.last) &&
                    it.a.contains(combination.a.first()) && it.a.contains(combination.a.last) &&
                    it.s.contains(combination.s.first()) && it.s.contains(combination.s.last)
            }) {
                println("is contained in another range")
            } else {
                cleanedUp.add(combination)
            }
        }
        return cleanedUp.toSet()
    }

    private fun getAllCombinations(workflows: Map<String, Workflow>): Set<UndefinedPart> {
        val queue = LinkedList<QueueItem>()
        queue.add(QueueItem(UndefinedPart(), workflows.getValue("in")))
        val acceptedItems = mutableSetOf<UndefinedPart>()
        while (queue.isNotEmpty()) {
            val (undefinedPart, workflow) = queue.poll()
            if (workflow.rules.isEmpty()) {
                if (workflow.end == "R") {
                    acceptedItems.add(undefinedPart)
                } else if (workflow.end != "A") {
                    queue.add(QueueItem(undefinedPart, workflows.getValue(workflow.end)))
                }
            } else {
                val firstRule = workflow.rules.first()
                val ifChild = firstRule.ifChild()
                val elseChild = firstRule.elseChild()
                if (ifChild.first == "max") {
                    val conditionTrue = undefinedPart.setNewMax(ifChild.second, firstRule.category)
                    if (firstRule.next == "R") {
                        acceptedItems.add(conditionTrue)
                    } else if (firstRule.next != "A") {
                        queue.add(QueueItem(conditionTrue, workflows.getValue(firstRule.next)))
                    }
                    queue.add(QueueItem(undefinedPart.setNewMin(elseChild.second, firstRule.category), workflow.copy(rules = workflow.rules.drop(1))))
                } else {
                    val conditionTrue = undefinedPart.setNewMin(ifChild.second, firstRule.category)
                    if (firstRule.next == "R") {
                        acceptedItems.add(conditionTrue)
                    } else if (firstRule.next != "A") {
                        queue.add(QueueItem(conditionTrue, workflows.getValue(firstRule.next)))
                    }
                    queue.add(QueueItem(undefinedPart.setNewMax(elseChild.second, firstRule.category), workflow.copy(rules = workflow.rules.drop(1))))
                }
            }
        }
        return acceptedItems.toSet()
    }

    override fun executePart1(name: String): Any {
        val (workflows, parts) = getInputAsLines(name, false)
            .splitBy { it.isEmpty() }
            .filter { it.isNotEmpty() && it.first().isNotEmpty() }
        return checkParts(
            workflows.map { Workflow.parse(it) }.associateBy { it.id },
            parts.map { Part.parse(it) })
            .sumOf { v -> v.map.values.sumOf { it } }
    }

    private fun checkThatThereAreNoOverlaps(rejectedCombinations: List<UndefinedPart>) {
        var overlap = false
        for (i in rejectedCombinations.indices) {
            for (j in i+1..rejectedCombinations.lastIndex) {
                if (rejectedCombinations[i].overlaps(rejectedCombinations[j])) {
                    overlap = true
                }
            }
        }
        if (overlap) {
            println("Caution: There are rejected Regions that overlap!")
        }
    }

    override fun executePart2(name: String): Any {
        val (workflows, _) = getInputAsLines(name, false)
            .splitBy { it.isEmpty() }
            .filter { it.isNotEmpty() && it.first().isNotEmpty() }
        return getAllCombinations(workflows.map { Workflow.parse(it) }.associateBy { it.id })
            .also { checkThatThereAreNoOverlaps(it.toList()) }
            .sumOf { it.size() }
            .let { 4000L*4000L*4000L*4000L - it }
    }
}