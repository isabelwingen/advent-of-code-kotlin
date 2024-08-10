package aoc2017

import util.Day
import java.util.LinkedList
import kotlin.math.min

class Day15: Day("15") {
    override fun executePart1(name: String): Any {
        var a = 516L
        var b = 190L
        val facA = 16807L
        val facB = 48271L
        val m = 2147483647L
        var res = 0
        for (i in 0..40_000_000) {
            a = (a * facA).mod(m)
            b = (b * facB).mod(m)
            val aBin = a.toString(2).takeLast(16).padStart(16, '0')
            val bBin = b.toString(2).takeLast(16).padStart(16, '0')
            if (aBin == bBin) {
                res++
            }
        }
        return res

    }

    override fun executePart2(name: String): Any {
        var a = 516L
        var b = 190L
        val facA = 16807L
        val facB = 48271L
        val m = 2147483647L
        var res = 0
        var c = 0
        val aQueue = LinkedList<String>()
        val bQueue = LinkedList<String>()
        for (i in 0 until 60_000_000) {
            if (min(aQueue.size, bQueue.size) == 5_000_000) {
                break
            }
            a = (a * facA).mod(m)
            if (a.mod(4) == 0) {
                aQueue.addLast(a.toString(2).takeLast(16).padStart(16, '0'))
            }
            b = (b * facB).mod(m)
            if (b.mod(8) == 0) {
                bQueue.addLast(b.toString(2).takeLast(16).padStart(16, '0'))
            }
            if (aQueue.isNotEmpty() && bQueue.isNotEmpty()) {
                c++
                if (aQueue.pop() == bQueue.pop()) {
                    res++
                }
            }
            if (c == 5_000_000) {
                return res
            }
        }
        return res
    }
}