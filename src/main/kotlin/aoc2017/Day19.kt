package aoc2017

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day19: Day("19") {
    override fun executePart1(name: String): Any {
        val field = getInputAsLines(name, true)
            .map { it.toList().toCharArray() }
            .toList()
            .toTypedArray()
        var row = 0
        var col = field[0].indexOf('|')
        val letters = LinkedList<Char>()
        var direction = "down"
        var steps = 0
        while (true) {
            val currentChar = field[row][col]
            if (currentChar == '+') {
                if (direction == "down" || direction == "up") {
                    direction = if ('-' == field[row].getOrNull(col - 1)) {
                        "left"
                    } else {
                        "right"
                    }
                } else {
                    direction = if (field.getOrNull(row - 1) != null && '|' == field[row - 1][col]) {
                        "up"
                    } else {
                        "down"
                    }
                }
            } else if (currentChar == ' ') {
                break
            } else if (currentChar != '-' && currentChar != '|') {
                letters.add(currentChar)
            }
            when (direction) {
                "down" -> {
                    row++
                }
                "up" -> {
                    row--
                }
                "right" -> {
                    col++
                }
                else -> {
                    col--
                }
            }
            steps++
        }
        return letters.joinToString("") to steps
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}