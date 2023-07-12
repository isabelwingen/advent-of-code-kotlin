package aoc2022

import getInputAsLines
import util.Day
import kotlin.math.max
import kotlin.math.pow

class Day25: Day("25") {

    val powersOfFiveAsInts = IntRange(0, 25).map { 5.0.pow(it) }

    private fun factor(char: Char): Int {
        return when (char) {
            '=' -> -2
            '-' -> -1
            '0' -> 0
            '1' -> 1
            '2' -> 2
            else -> throw IllegalStateException()
        }
    }

    private fun decode(snafu: CharArray): Long {
        var sum = 0L
        for (i in snafu.indices) {
            sum += factor(snafu[snafu.lastIndex-i]) * powersOfFiveAsInts[i].toLong()
        }
        return sum
    }

    private fun addSnafuDigis(d1: Char, d2: Char, carry: Char = '0'): CharArray {
        val p = (d1 to d2)
        return when (carry) {
            '0' -> {
                when (p) {
                    '0' to '=' -> "="
                    '0' to '-' -> "-"
                    '0' to '0' -> "0"
                    '0' to '1' -> "1"
                    '0' to '2' -> "2"
                    '1' to '=' -> "-"
                    '1' to '-' -> "0"
                    '1' to '0' -> "1"
                    '1' to '1' -> "2"
                    '1' to '2' -> "1="
                    '2' to '=' -> "0"
                    '2' to '-' -> "1"
                    '2' to '0' -> "2"
                    '2' to '1' -> "1="
                    '2' to '2' -> "1-"
                    '=' to '=' -> "-1"
                    '=' to '-' -> "-2"
                    '=' to '0' -> "="
                    '=' to '1' -> "-"
                    '=' to '2' -> "0"
                    '-' to '=' -> "-2"
                    '-' to '-' -> "="
                    '-' to '0' -> "-"
                    '-' to '1' -> "0"
                    '-' to '2' -> "1"
                    else -> throw IllegalStateException()
                }
            }
            '1' -> {
                when (p) {
                    '0' to '=' -> "-"
                    '0' to '-' -> "0"
                    '0' to '0' -> "1"
                    '0' to '1' -> "2"
                    '0' to '2' -> "1="
                    '1' to '=' -> "0"
                    '1' to '-' -> "1"
                    '1' to '0' -> "2"
                    '1' to '1' -> "1="
                    '1' to '2' -> "1-"
                    '2' to '=' -> "1"
                    '2' to '-' -> "2"
                    '2' to '0' -> "1="
                    '2' to '1' -> "1-"
                    '2' to '2' -> "10"
                    '=' to '=' -> "-2"
                    '=' to '-' -> "="
                    '=' to '0' -> "-"
                    '=' to '1' -> "0"
                    '=' to '2' -> "1"
                    '-' to '=' -> "="
                    '-' to '-' -> "-"
                    '-' to '0' -> "0"
                    '-' to '1' -> "1"
                    '-' to '2' -> "2"
                    else -> throw IllegalStateException()
                }
            }
            '-' -> {
                when (p) {
                    '0' to '=' -> "-2"
                    '0' to '-' -> "="
                    '0' to '0' -> "-"
                    '0' to '1' -> "0"
                    '0' to '2' -> "1"

                    '1' to '=' -> "="
                    '1' to '-' -> "-"
                    '1' to '0' -> "0"
                    '1' to '1' -> "1"
                    '1' to '2' -> "2"

                    '2' to '=' -> "-"
                    '2' to '-' -> "0"
                    '2' to '0' -> "1"
                    '2' to '1' -> "2"
                    '2' to '2' -> "1="

                    '=' to '=' -> "-0"
                    '=' to '-' -> "-1"
                    '=' to '0' -> "-2"
                    '=' to '1' -> "="
                    '=' to '2' -> "-"

                    '-' to '=' -> "-1"
                    '-' to '-' -> "-2"
                    '-' to '0' -> "="
                    '-' to '1' -> "-"
                    '-' to '2' -> "0"
                    else -> throw IllegalStateException()
                }
            }
            else -> {
                throw IllegalStateException()
            }
        }.toCharArray()
    }

    // 1=-0-2 plus 12111 equals 120221= (1747 plus 906 equals 22178)

    // 1=-0-2
    //  12111
    // ....1.
    // 1-111=
    private fun addSnafus(a: CharArray, b: CharArray): CharArray {
        if (a.size == 1 && b.size == 1) {
            return addSnafuDigis(a[0], b[0])
        }
        val aRev = a.reversed().toCharArray()
        val bRev = b.reversed().toCharArray()
        var sum = "".toCharArray()
        var carry = '0'
        for (i in 0 until max(aRev.size, bRev.size)) {
            val aa = aRev.getOrElse(i) { '0' }
            val bb = bRev.getOrElse(i) { '0' }
            val res = addSnafuDigis(aa, bb, carry)
            if (res.size == 1) {
                sum += res[0]
                carry = '0'
            } else {
                sum += res[1]
                carry = res[0]
            }
        }
        if (carry != '0') {
            sum += carry
        }
        return sum.reversedArray()
    }


    override fun executePart1(name: String): Any {
        val p = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { it.toCharArray() }
            .reduceRight { a: CharArray, b: CharArray -> addSnafus(a, b) }
        return p.toList().joinToString("")
    }


    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}

