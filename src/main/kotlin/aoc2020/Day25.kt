package aoc2020

import util.Day

const val PUBLIC_KEY_DOOR = 8421034
const val PUBLIC_KEY_CARD = 15993936
const val PRIME = 20201227

class Day25 : Day("25") {
    fun squareAndMultiply(x: Int, k: Int): Long {
        val commands = k
            .toString(2)
            .toCharArray()
            .drop(1)
            .joinToString("") { if (it == '0') "Q" else "QM" }
        return commands
            .fold(x.toLong()) { acc, c -> if (c == 'Q') (acc * acc) % PRIME else (acc * x) % PRIME }
    }

    fun findLoopSize(publicKey: Int): Int {
        var i = 0
        var res = 1
        while (i < PRIME) {
            i++
            res = (res * 7) % PRIME
            if (res == publicKey) {
                return i
            }
        }
        return i
    }

    override fun executePart1(name: String): Long {
        val cardLoopSize = findLoopSize(PUBLIC_KEY_DOOR)
        return squareAndMultiply(PUBLIC_KEY_CARD, cardLoopSize)
    }

    override fun executePart2(name: String) = 0

}