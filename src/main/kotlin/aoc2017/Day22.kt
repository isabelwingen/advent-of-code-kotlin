package aoc2017

import getInputAsLines
import util.Day

private const val INFECTED = '#'
private const val FLAGGED = '+'
private const val WEAK = '-'
private const val CLEAN = '.'

class Day22: Day("22") {
    override fun executePart1(name: String): Any {
        val f = getInputAsLines(name, true)
            .map { it.toList() }
            .toList()
        val field = mutableSetOf<Pair<Int, Int>>()
        f.forEachIndexed { row, chars ->
            chars.forEachIndexed { col, c ->
                if (c == '#') {
                    field.add(row to col)
                }
            }
        }
        var row = f.size/2
        var col = f[0].size/2
        var dir = "up"
        var infected = 0
        for (i in 0 until 10000) {
            val currentNode = if (field.contains(row to col)) {
                '#'
            } else {
                '.'
            }
            // turn
            if (currentNode == '#') {
                dir = when (dir) {
                    "up" -> "right"
                    "right" -> "down"
                    "down" -> "left"
                    "left" -> "up"
                    else -> throw IllegalStateException()
                }
            } else {
                dir = when (dir) {
                    "up" -> "left"
                    "left" -> "down"
                    "down" -> "right"
                    "right" -> "up"
                    else -> throw IllegalStateException()
                }
            }
            // clean or infect
            if (currentNode == '#') {
                field.remove(row to col)
            } else {
                infected++
                field.add(row to col)
            }
            // move
            when (dir) {
                "up" -> row--
                "down" -> row++
                "left" -> col--
                "right" -> col++
            }
        }
        return infected
    }

    override fun executePart2(name: String): Any {
        val f = getInputAsLines(name, true)
            .map { it.toList() }
            .toList()
        val nodes = mutableMapOf<Pair<Int, Int>, Char>().withDefault { CLEAN }
        f.forEachIndexed { row, chars ->
            chars.forEachIndexed { col, c ->
                if (c == INFECTED) {
                    nodes[row to col] = INFECTED
                }
            }
        }

        fun getNode(row: Int, col: Int): Char {
            return nodes.getValue(row to col)
        }



        var row = f.size/2
        var col = f[0].size/2
        var dir = "up"
        var count = 0
        for (i in 0 until 10000000) {
            val currentNode = getNode(row, col)
            // turn
            if (currentNode == INFECTED) {
                dir = when (dir) {
                    "up" -> "right"
                    "right" -> "down"
                    "down" -> "left"
                    "left" -> "up"
                    else -> throw IllegalStateException()
                }
            } else if (currentNode == CLEAN) {
                dir = when (dir) {
                    "up" -> "left"
                    "left" -> "down"
                    "down" -> "right"
                    "right" -> "up"
                    else -> throw IllegalStateException()
                }
            } else if (currentNode == FLAGGED) {
                dir = when (dir) {
                    "up" -> "down"
                    "left" -> "right"
                    "down" -> "up"
                    "right" -> "left"
                    else -> throw IllegalStateException()
                }
            }
            // transform
            when (currentNode) {
                CLEAN -> nodes[row to col] = WEAK
                WEAK -> {
                    nodes[row to col] = INFECTED
                    count++
                }
                INFECTED -> nodes[row to col] = FLAGGED
                else -> nodes.remove(row to col)
            }

            // move
            when (dir) {
                "up" -> row--
                "down" -> row++
                "left" -> col--
                "right" -> col++
            }
        }
        return count
    }
}