package aoc2023

import getInputAsLines
import util.Day

class Day7: Day("7") {


    data class Line(val hand: List<Char>, val bid: Int) {

        override fun toString() = "${hand.joinToString("")} ($bid)"

        fun type(): Int {
            val splitUpIntoTypes = { h: List<Char> -> h.groupBy { it }.values.map { it.count() }.sorted() }
            return when (splitUpIntoTypes(hand)) {
                listOf(5) -> FIVE_OF_A_KIND
                listOf(1, 4) -> FOUR_OF_A_KIND
                listOf(2, 3) -> FULL_HOUSE
                listOf(1, 1, 3) -> THREE_OF_A_KIND
                listOf(1, 2, 2) -> TWO_PAIR
                listOf(1, 1, 1, 2) -> ONE_PAIR
                listOf(1, 1, 1, 1, 1) -> HIGH_CARD
                else -> throw IllegalStateException("type")
            }
        }

        companion object {
            fun parse(string: String): Line {
                return string.split(" ")
                    .let { (a, b) -> Line(a.toCharArray().toList(), b.toInt()) }
            }

            private fun rank(char: Char): Int {
                return when (char) {
                    '2' -> 12
                    '3' -> 11
                    '4' -> 10
                    '5' -> 9
                    '6' -> 8
                    '7' -> 7
                    '8' -> 6
                    '9' -> 5
                    'T' -> 4
                    'J' -> 3
                    'Q' -> 2
                    'K' -> 1
                    'A' -> 0
                    else -> throw IllegalStateException()
                }
            }

            fun comparator(): Comparator<Line> {
                return Comparator { o1: Line, o2: Line -> o1.type().compareTo(o2.type()) }
                    .thenComparator { o1, o2 ->
                        var i = 0
                        var r = rank(o1.hand[i]).compareTo(rank(o2.hand[i]))
                        while (r == 0) {
                            i++
                            r = rank(o1.hand[i]).compareTo(rank(o2.hand[i]))
                        }
                        r
                    }
            }
        }
    }

    data class Line2(val hand: List<Char>, val bid: Int) {

        override fun toString() = "${hand.joinToString("")} ($bid)"

        fun type(): Int {
            val splitUpIntoTypes = { h: List<Char> ->
                val p = h.groupBy { it }.mapValues { it.value.count() }
                p['J'] to p.filter { it.key != 'J' }.values.toList().sorted()
            }
            return splitUpIntoTypes(hand).let { (js, other) ->
                when (js to other) {
                    5 to listOf<Int>() -> FIVE_OF_A_KIND

                    4 to listOf(1) -> FIVE_OF_A_KIND

                    3 to listOf(2) -> FIVE_OF_A_KIND
                    3 to listOf(1, 1) -> FOUR_OF_A_KIND

                    2 to listOf(3) -> FIVE_OF_A_KIND
                    2 to listOf(1, 2) -> FOUR_OF_A_KIND
                    2 to listOf(1, 1, 1) -> THREE_OF_A_KIND

                    1 to listOf(4) -> FIVE_OF_A_KIND
                    1 to listOf(1, 3) -> FOUR_OF_A_KIND
                    1 to listOf(2, 2) -> FULL_HOUSE
                    1 to listOf(1, 1, 2) -> THREE_OF_A_KIND
                    1 to listOf(1, 1, 1, 1) -> ONE_PAIR

                    null to listOf(5) -> FIVE_OF_A_KIND
                    null to listOf(1, 4) -> FOUR_OF_A_KIND
                    null to listOf(2, 3) -> FULL_HOUSE
                    null to listOf(1, 1, 3) -> THREE_OF_A_KIND
                    null to listOf(1, 2, 2) -> TWO_PAIR
                    null to listOf(1, 1, 1, 2) -> ONE_PAIR
                    null to listOf(1, 1, 1, 1, 1) -> HIGH_CARD

                    else -> throw IllegalStateException("$js, $other")
                }
            }
        }

        companion object {
            fun parse(string: String): Line2 {
                return string.split(" ")
                    .let { (a, b) -> Line2(a.toCharArray().toList(), b.toInt()) }
            }

            private fun rank(char: Char): Int {
                return when (char) {
                    'J' -> 13
                    '2' -> 12
                    '3' -> 11
                    '4' -> 10
                    '5' -> 9
                    '6' -> 8
                    '7' -> 7
                    '8' -> 6
                    '9' -> 5
                    'T' -> 4
                    'Q' -> 2
                    'K' -> 1
                    'A' -> 0
                    else -> throw IllegalStateException()
                }
            }

            fun comparator(): Comparator<Line2> {
                return Comparator { o1: Line2, o2: Line2 -> o1.type().compareTo(o2.type()) }
                    .thenComparator { o1, o2 ->
                        var i = 0
                        var r = rank(o1.hand[i]).compareTo(rank(o2.hand[i]))
                        while (r == 0) {
                            i++
                            r = rank(o1.hand[i]).compareTo(rank(o2.hand[i]))
                        }
                        r
                    }
            }
        }
    }

    override fun executePart1(name: String): Any {
        return getInputAsLines(name, true)
            .map { Line.parse(it) }
            .sortedWith(Line.comparator())
            .reversed()
            .mapIndexed { i, (_, bid) -> (i + 1) * bid }
            .sumOf { it.toLong() }
    }

    override fun executePart2(name: String): Any {
        return getInputAsLines(name, true)
            .map { Line2.parse(it) }
            .sortedWith(Line2.comparator())
            .reversed()
            .mapIndexed { i, (_, bid) -> (i + 1) * bid }
            .sumOf { it.toLong() }
    }

    companion object {
        const val FIVE_OF_A_KIND = 1
        const val FOUR_OF_A_KIND = 2
        const val FULL_HOUSE = 3
        const val THREE_OF_A_KIND = 4
        const val TWO_PAIR = 5
        const val ONE_PAIR = 6
        const val HIGH_CARD = 7
    }
}