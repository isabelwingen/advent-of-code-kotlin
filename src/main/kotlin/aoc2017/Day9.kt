package aoc2017

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day9: Day("9") {
    private fun calculate(line: String): Pair<Int, Int> {
        var score = 0
        var depth = 0
        var inGarbage = false
        var ignoreNextChar = false
        var garbage = 0
        for (c in line) {
            if (inGarbage) {
                if (!ignoreNextChar) {
                    if (c == '>') {
                        inGarbage = false
                        ignoreNextChar = false
                    } else if (c == '!') {
                        ignoreNextChar = true
                    } else {
                        garbage++
                        ignoreNextChar = false
                    }
                } else {
                    ignoreNextChar = false
                }
            } else {
                when (c) {
                    '{' -> {
                        depth++
                    }
                    '}' -> {
                        score += depth
                        depth--
                    }
                    '<' -> {
                        inGarbage = true
                    }
                }
            }
        }
        return score to garbage
    }

    override fun executePart1(name: String): Any {
        val line = getInputAsLines(name, true).first()
        return calculate(line).first
    }

    override fun executePart2(name: String): Any {
        val line = getInputAsLines(name, true).first()
        return calculate(line).second
    }
}