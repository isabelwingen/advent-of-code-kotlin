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

        fun ifChild(undefinedPart: UndefinedPart): UndefinedPart {
            return if (operator == '<') {
                undefinedPart.setNewMax(threshold-1, category)
            } else {
                undefinedPart.setNewMin(threshold+1, category)
            }
        }

        fun elseChild(undefinedPart: UndefinedPart): UndefinedPart {
            return if (operator == '<') {
                undefinedPart.setNewMin(threshold, category)
            } else {
                undefinedPart.setNewMax(threshold, category)
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
        val s: IntRange = 1..4000
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

        fun numberOfCombinations(): Long {
            val length = fun(i: IntRange): Long { return i.toList().size.toLong() }
            return length(x) * length(m) * length(a) * length(s)
        }
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
        for (i in rejectedCombinations.indices) {
            for (j in i+1..rejectedCombinations.lastIndex) {
                if (rejectedCombinations[i].overlaps(rejectedCombinations[j])) {
                    throw IllegalStateException("Caution: There are rejected regions that overlap!")
                }
            }
        }
    }

    data class QueueItem(val undefinedPart: UndefinedPart, val workflow: Workflow)

    private fun getAllCombinations(workflows: Map<String, Workflow>): Set<UndefinedPart> {
        val queue = LinkedList<QueueItem>()
        queue.add(QueueItem(UndefinedPart(), workflows.getValue("in")))
        val acceptedItems = mutableSetOf<UndefinedPart>()
        while (queue.isNotEmpty()) {
            val (undefinedPart, workflow) = queue.poll()
            if (workflow.rules.isEmpty()) {
                if (workflow.end == "A") {
                    acceptedItems.add(undefinedPart)
                } else if (workflow.end != "R") {
                    queue.add(QueueItem(undefinedPart, workflows.getValue(workflow.end)))
                }
            } else {
                val firstRule = workflow.rules.first()
                firstRule.ifChild(undefinedPart).apply {
                    if (firstRule.next == "A") {
                        acceptedItems.add(this)
                    } else if (firstRule.next != "R") {
                        queue.add(QueueItem(this, workflows.getValue(firstRule.next)))
                    }
                }
                queue.add(QueueItem(firstRule.elseChild(undefinedPart), workflow.copy(rules = workflow.rules.drop(1))))
            }
        }
        return acceptedItems.toSet()
    }

    override fun executePart2(name: String): Any {
        val (workflows, _) = getInputAsLines(name, false)
            .splitBy { it.isEmpty() }
            .filter { it.isNotEmpty() && it.first().isNotEmpty() }
        return getAllCombinations(workflows.map { Workflow.parse(it) }.associateBy { it.id })
            .also { checkThatThereAreNoOverlaps(it.toList()) }
            .sumOf { it.numberOfCombinations() }
    }
}