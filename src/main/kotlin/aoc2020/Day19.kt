package aoc2020

import getResourceAsList
import splitBy

private fun parseInput(): Data {
    val coll = getResourceAsList("2020/day19.txt")
        .splitBy { it.isBlank() }
        .filter { it.all { x -> x.isNotBlank() } }
    return Data(coll[0].map { toRule(it) }.sortedBy { it.id }, coll[1])
}

private fun toRule(line: String): Rule {
    val x = line.split(": ")
    val y = x[1].split(" | ")
        .map { it.split(" ") }
        .map { p -> p.map { if (it.toIntOrNull() == null) it.replace("\"", "") else it.toInt() } }
    return Rule(x[0].toInt(), y)
}

data class Data(val rules: List<Rule>, val words: List<String>)

class Rule(val id: Int, val children: List<List<Any>>) {
    override fun toString(): String {
        return "$id -> $children"
    }
}

fun String.isLiteral(): Boolean = this == "a" || this == "b"

fun Rule.isLiteralRule(): Boolean = this.children.size == 1 && this.children[0].size == 1 && this.children[0][0].toString().isLiteral()

private fun checkWord(word: String, allRules: Map<Int, Rule>, checkedRules: List<Int>): Boolean {
    if (word.isEmpty() && checkedRules.isNotEmpty()) {
        return false
    }
    if (word.isNotEmpty() && checkedRules.isEmpty()) {
        return false
    }
    if (word.isEmpty() && checkedRules.isEmpty()) {
        return true
    }
    val firstRule = allRules[checkedRules.first()]!!
    return if (firstRule.isLiteralRule()) {
        if (word.startsWith(firstRule.children[0][0].toString())) {
            checkWord(word.substring(1), allRules, checkedRules.subList(1, checkedRules.size))
        } else {
            false
        }
    } else {
        firstRule.children
            .map { it.map { x -> x.toString().toInt() } }
            .map { it + checkedRules.subList(1, checkedRules.size) }
            .any { checkWord(word, allRules, it) }
    }
}

fun executeDay19Part1(): Int {
    var data = parseInput()
    return data.words
        .count { checkWord(it, data.rules.associateBy { r -> r.id }, listOf(0)) }
}

private fun calculateLiteralRules(allRules: Map<Int, Rule>, start: List<Int>): List<List<Rule>> {
    if (start.isEmpty()) {
        return listOf(listOf())
    }
    val firstRule = allRules[start.first()]!!
    return if (firstRule.isLiteralRule()) {
        calculateLiteralRules(allRules, start.subList(1, start.size))
            .map { listOf(firstRule) + it }
    } else {
        firstRule.children
            .map { it.map { x -> x.toString().toInt() } }
            .map { it + start.subList(1, start.size) }
            .flatMap { calculateLiteralRules(allRules, it) }
    }
}

private fun calculateWords(allRules: Map<Int, Rule>, start: List<Int>): List<String> {
    val rules = calculateLiteralRules(allRules, start)
    return rules
        .map { it.map { rule -> rule.children[0][0].toString() } }
        .map { it.joinToString("") }
}

private fun wordMatches(wordsOf42: List<String>, wordsOf31: List<String>, word: String): Boolean {
    if (word.length % 8 != 0) {
        return false
    }
    val chunked = word.chunked(8)
    val chunkesOf31 = chunked.reversed().takeWhile { wordsOf31.contains(it) }.size
    if (chunkesOf31 == 0) {
        return false
    }
    val a = chunked.reversed().drop(chunkesOf31).take(chunkesOf31).all { wordsOf42.contains(it) }
    val b = chunked.reversed().drop(2 * chunkesOf31).all { wordsOf42.contains(it) }
    if (chunked.reversed().drop(2 * chunkesOf31).isEmpty()) {
        return false
    }
    return a && b
}

private fun matchWords(wordsOf42: List<String>, wordsOf31: List<String>, words: List<String>): Int {
    return words
        .count { wordMatches(wordsOf42, wordsOf31, it) }
}

fun executeDay19Part2(): Int {
    val data = parseInput()
    val wordsOf42 = calculateWords(
        data.rules.associateBy { it.id },
        listOf(42))
    val wordsOf31 = calculateWords(
        data.rules.associateBy { it.id },
        listOf(31))
    return matchWords(wordsOf42, wordsOf31, data.words)
}