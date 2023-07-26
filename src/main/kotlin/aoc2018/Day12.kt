package aoc2018

import getInputAsLines
import util.Day

class Day12: Day("12") {

    private data class Rule(val left: IntArray, val right: Int) {

        companion object {
            fun create(line: String): Rule {
                val (a,b) = line.split(" => ")
                return Rule(a.map { if (it == '#') 1 else 0 }.toIntArray(), if (b[0] == '#') 1 else 0)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Rule

            if (!left.contentEquals(other.left)) return false
            if (right != other.right) return false

            return true
        }

        override fun hashCode(): Int {
            var result = left.contentHashCode()
            result = 31 * result + right
            return result
        }
    }

    private data class Game(var state: IntArray, val rules: List<Rule>, var positionOfZero: Long = 0L) {
        fun addBuffer() {
            state = IntArray(4) {0} + state + IntArray(4) {0}
            positionOfZero += 4
        }

        fun trim() {
            val emptyPrefixSize = state.takeWhile { it == 0 }.count()
            state = state.dropWhile { it == 0 }.dropLastWhile { it == 0 }.toIntArray()
            positionOfZero -= emptyPrefixSize
        }

        fun applyRules() {
            addBuffer()
            val newState = state.copyOf()
            for (i in state.indices.drop(2).dropLast(2)) {
                val matchingRule = rules.find { rule -> rule.left.contentEquals(state.sliceArray(i - 2..i + 2)) }
                newState[i] = matchingRule?.right ?: 0
            }
            state = newState
            trim()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Game

            if (!state.contentEquals(other.state)) return false
            if (rules != other.rules) return false
            if (positionOfZero != other.positionOfZero) return false

            return true
        }

        override fun hashCode(): Int {
            var result = state.contentHashCode()
            result = 31 * result + rules.hashCode()
            result = 31 * result + positionOfZero.hashCode()
            return result
        }


    }

    private fun getInput(name: String): Game {
        val input = getInputAsLines(name)
        require(input.size >= 3) { "Input must contain at least 3 lines." }

        val initialState = input[0].drop(15).mapNotNull { if (it == '#') 1 else if (it == '.') 0 else null }.toIntArray()
        val rules = input.drop(2).dropLast(1).map { Rule.create(it) }

        return Game(initialState, rules)
    }

    override fun executePart1(name: String): Any {
        val game = getInput(name)
        repeat(20) { game.applyRules() }
        return game.state.mapIndexed { index, c -> if (c == 1) index-game.positionOfZero else 0 }.sum()
    }

    override fun executePart2(name: String): Long {
        val game = getInput(name)
        var before = game.state.copyOf()
        while (true) {
            game.applyRules()
            if (game.state.contentEquals(before)) {
                break
            } else {
                before = game.state
            }
        }
        // After x steps a configuration is reached that is only shifted but not changed afterwards
        // the configuration moves on step left each time, as 103 generations equals an offset of -69, generation g equals (34-g)
        return game.state.mapIndexed { index, c -> if (c == 1) (index-(34-50000000000L)) else 0L }.sum()
    }
}