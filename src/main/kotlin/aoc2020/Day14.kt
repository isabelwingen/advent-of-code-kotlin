package aoc2020

import getInputAsLines
import util.Day
import kotlin.math.pow

class Day14: Day("14") {
    private fun <T> parseInput(path: String, transform: (Pair<String, String>) -> T): List<T> {
        return getInputAsLines(path)
            .filter { it.isNotBlank() }
            .map { it.split(" = ") }
            .map { it[0] to it[1] }
            .map { transform(it) }
    }

    private fun transformInputPairPart1(pair: Pair<String, String>): Pair<Int, String> {
        return if (pair.first == "mask") {
            -1 to pair.second
        } else {
            pair.first.substring(4).dropLast(1).toInt() to Integer.toBinaryString(pair.second.toInt()).padStart(36, '0')
        }
    }

    private fun transformInputPairPart2(pair: Pair<String, String>): Pair<String, Long> {
        return if (pair.first == "mask") {
            pair.second to -1L
        } else {
            pair.first.substring(4).dropLast(1).toInt().toString(2).padStart(36, '0') to pair.second.toLong()
        }
    }

    private fun applyMaskPart1(mask: String, value: String): Long {
        val res = value.toMutableList()
        for (i in mask.indices) {
            when (mask[i]) {
                '0' -> res[i] = '0'
                '1' -> res[i] = '1'
                'X' -> res[i] = value[i]
            }
        }
        return res.joinToString("").toLong(2)
    }

    private fun applyMaskPart2(mask: String, value: String): List<Long> {
        val res = value.toMutableList()
        for (i in mask.indices) {
            when (mask[i]) {
                '0' -> res[i] = value[i]
                '1' -> res[i] = '1'
                'X' -> res[i] = 'X'
            }
        }
        return getAllAddressesFromFloatingAddress(res.joinToString(""))
    }

    private fun getAllAddressesFromFloatingAddress(address: String): List<Long> {
        val floatingPoints = address.count { it == 'X' }
        val pos = address
            .mapIndexed { index, c -> index to c }
            .filter { it.second == 'X' }
            .map { it.first }
        val res = mutableListOf<String>()
        for (i in 0 until 2.0.pow(floatingPoints.toDouble()).toInt()) {
            val bits = i.toString(2).padStart(floatingPoints, '0')
            val newAd = address.toMutableList()
            for (j in pos.indices) {
                newAd[pos[j]] = bits[j]
            }
            res.add(newAd.joinToString(""))
        }
        return res
            .map { it.toLong(2) }
    }

    override fun executePart1(name: String): Long {
        val commands = parseInput(name) { transformInputPairPart1(it) }
        var mask = ""
        val mem = mutableMapOf<Int, Long>()
        for (command in commands) {
            if (command.first == -1) {
                mask = command.second
            } else {
                mem[command.first] = applyMaskPart1(mask, command.second)
            }
        }
        return mem.values.sum()
    }

    override fun executePart2(name: String): Long {
        val commands = parseInput(name) { transformInputPairPart2(it) }
        var mask = ""
        val mem = mutableMapOf<Long, Long>()
        for (command in commands) {
            if (command.second == -1L) {
                mask = command.first
            } else {
                val adds = applyMaskPart2(mask, command.first)
                for (add in adds) {
                    mem[add] = command.second
                }
            }
        }
        return mem.values.sum()
    }

}
