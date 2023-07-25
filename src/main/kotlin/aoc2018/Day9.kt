package aoc2018

import getInput
import util.Day

class Day9: Day("9") {

    private data class Marble(val value: Long = 0, var next: Marble?, var before: Marble?) {
        override fun toString() = "$value"
    }

    private data class MarbleGame(var head: Marble) {
        fun addElement(element: Long): Long {
            if (element % 23 != 0L) {
                val toBeAddedAfter = head.next!!
                val x = toBeAddedAfter.next!!
                val y = Marble(element, x, toBeAddedAfter)
                toBeAddedAfter.next = y
                x.before = y
                head = y
                return 0L
            } else {
                val toBeRemoved = head.before!!.before!!.before!!.before!!.before!!.before!!.before!!
                toBeRemoved.before!!.next = toBeRemoved.next!!
                toBeRemoved.next!!.before = toBeRemoved.before!!
                head = toBeRemoved.next!!
                return element + toBeRemoved.value
            }
        }

        override fun toString(): String {
            var s = "${head.value} -> "
            var current = head.next!!
            while (current != head) {
                s += "${current.value} -> "
                current = current.next!!
            }
            return s
        }

        companion object {
            fun create(element: Long = 0L): MarbleGame {
                val x = Marble(element, null, null)
                x.next = x
                x.before = x
                return MarbleGame(x)
            }
        }
    }

    private fun play(players: Int, highestMarble: Long): Long {
        val l = MarbleGame.create()
        val scores = mutableMapOf<Int, Long>()
        LongRange(1, highestMarble).forEach {
            val player = ((it-1) % players).toInt()
            val score = l.addElement(it)
            if (score != 0L) {
                scores[player] = scores.getOrDefault(player, 0) + score
            }
        }
        return scores.values.maxOf { it }
    }

    override fun executePart1(name: String): Any {
        val l = getInput(name).split(" ")
        return play(l[0].toInt(), l[6].toLong())
    }

    override fun executePart2(name: String): Any {
        val l = getInput(name).split(" ")
        return play(l[0].toInt(), l[6].toLong() * 100)
    }
}