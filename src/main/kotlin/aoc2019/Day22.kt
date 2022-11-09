package aoc2019

import getInputAsLines
import util.Day
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.abs

const val CUT = "cut"
const val NEW = "deal into new stack"
const val INC = "deal with increment"
const val NIL = "NOTHING"

const val  SIZE = 119315717514047L
const val TIMES = 101741582076661L

class Day22: Day("22") {

    private fun readInput(name: String): List<Pair<String, Long>> {
        return getInputAsLines(name)
            .dropLast(1)
            .asSequence()
            .map { it.split(" ") }
            .map { it.dropLast(1).joinToString(" ").lowercase() to it.last() }
            .map { if (it.second == "stack") (it.first + " " + it.second) to 0L else it.first to it.second.toLong() }
            .toList()
    }

    private fun executeDealIntoNew(list: List<Long>): List<Long> {
        return list.reversed()
    }

    private fun executeCut(cut: Int, list: List<Long>): List<Long> {
        return if (cut > 0) {
            list.drop(cut).plus(list.take(cut))
        } else {
            list.takeLast(abs(cut)).plus(list.dropLast(abs(cut)))
        }
    }

    private fun executeDealWithIncrement(inc: Int, list: List<Long>): List<Long> {
        val newList = MutableList(list.size) { -1L }
        var p = 0
        var q = 0
        while (newList.contains(-1)) {
            newList[p] = list[q]
            p += inc
            p %= list.size
            q += 1
        }
        return newList.toList()
    }

    fun executeCommand(command: Pair<String, Long>, list: List<Long>): List<Long> {
        return when (command.first) {
            CUT -> executeCut(command.second.toInt(), list)
            NEW -> executeDealIntoNew(list)
            INC -> executeDealWithIncrement(command.second.toInt(), list)
            else -> throw java.lang.IllegalStateException("illegal command")
        }
    }

    fun executeCommands(commands: List<Pair<String, Long>>, list: List<Long>): List<Long> {
        var l = list
        for (c in commands) {
            l = executeCommand(c, l)
        }
        return l
    }

    override fun executePart1(name: String): Any {
        val commands = readInput(name)
        val list = LongRange(0, 10006).toList()
        return executeCommands(commands, list).indexOf(2019)
    }

    override fun expectedResultPart1() = 2519

    fun mergeTwoCommands(c1: Pair<String, Long>, c2: Pair<String, Long>, count: Long): Pair<Pair<String, Long>, Pair<String, Long>> {
        val (a1, v1) = c1
        val (a2, v2) = c2
        val w1 = BigInteger.valueOf(v1)
        val w2 = BigInteger.valueOf(v2)
        val m = BigInteger.valueOf(count)
        return when (a1 to a2) {
            NEW to NEW -> (NIL to 0L) to (NIL to 0L)
            CUT to CUT -> (NIL to 0L) to (CUT to w1.plus(w2).mod(m).longValueExact())
            INC to INC -> (NIL to 0L) to (INC to w1.multiply(w2).mod(m).longValueExact())
            CUT to NEW -> (NEW to 0L) to (CUT to -v1)
            CUT to INC -> (INC to v2) to (CUT to w1.multiply(w2).mod(m).longValueExact())
            INC to NEW -> (INC to count - v1) to (CUT to 1)
            else -> c1 to c2
        }
    }

    fun simplifyCommands(c: List<Pair<String, Long>>, count: Long): List<Pair<String, Long>> {
        var commands = c.toMutableList()
        while (commands.size > 2) {
            for (i in commands.indices.toList().dropLast(1)) {
                val (c1, c2) = mergeTwoCommands(commands[i], commands[i + 1], count)
                commands[i] = c1
                commands[i + 1] = c2
            }
            val newCommands = commands.filter { it.first != NIL }.toMutableList()
            if (newCommands == commands) {
                break
            } else {
                commands = newCommands
            }
        }
        return commands.toList()
    }

    private fun times(commands: List<Pair<String, Long>>, times: Long, count: Long): List<Pair<String, Long>> {
        val binaryString = times.toString(2)
        val arr = MutableList<List<Pair<String, Long>>>(binaryString.length) { emptyList() }
        arr[0] = commands
        for (i in 1 until binaryString.length) {
            arr[i] = simplifyCommands(arr[i-1] + arr[i-1], count)
        }
        val newCommands = binaryString.reversed().flatMapIndexed { index, c -> if (c == '1') arr[index] else emptyList() }
        return simplifyCommands(newCommands, count)
    }

    private fun reverseCut(cut: Long, goal: Long, size: Long): Long {
        return if (goal > cut) {
            goal - cut - 1
        } else {
            goal + cut
        }
    }

    private fun reverseIncrement(inc: Long, goal: Long, size: Long): Long {
        return BigInteger.valueOf(inc)
            .modInverse(BigInteger.valueOf(size))
            .multiply(BigInteger.valueOf(goal))
            .mod(BigInteger.valueOf(size))
            .longValueExact()
    }

    private fun reverseNew(goal: Long, size: Long): Long {
        return size - 1 - goal
    }

    override fun executePart2(name: String): Any {
        var commands = readInput(name)
        commands = simplifyCommands(commands, SIZE)
        val newCommands = times(commands, TIMES, SIZE)
        var goal = 2020L
        for (c in newCommands.reversed()) {
            goal = when (c.first) {
                NEW -> reverseNew(goal, SIZE)
                INC -> reverseIncrement(c.second, goal, SIZE)
                CUT -> reverseCut(c.second, goal, SIZE)
                else -> throw java.lang.IllegalStateException()
            }
        }
        return goal
    }

    override fun expectedResultPart2() = 58966729050483L

}