package aoc2020

import util.Day

private const val INPUT: String = "394618527"

class Day23 : Day("23") {
    private fun parseInput(): IntArray {
        return INPUT
            .map { it.toString().toInt() }
            .toIntArray()
    }

    private fun destination(current: Int, cups: IntArray, firstPickUp: Int, secondPickUp: Int, thirdPickup: Int): Int {
        var dest = if (current != 1) current - 1 else cups.maxOrNull()!!
        while (firstPickUp == dest || secondPickUp == dest || thirdPickup == dest) {
            dest = if (dest != 1) dest - 1 else cups.maxOrNull()!!
        }
        return dest
    }

    private fun pickUp(cups: IntArray, current: Int): IntArray {
        val first = cups[current]
        val second = cups[first]
        val third = cups[second]
        val fourth = cups[third]
        val dest = destination(current, cups, first, second, third)
        val afterDest = cups[dest]

        cups[current] = fourth
        cups[dest] = first
        cups[third] = afterDest
        return cups
    }

    /**
     * Create some kind of linked list
     * The order of the cups is decoded in the array:
     * The label of the cup that is immediately clockwise of the cup with the label i,
     * can be found in cups[i]
     * i.e cups[i] is a pointer to the next cup
     */
    private fun buildArray(input: IntArray): IntArray {
        val cups = IntArray(input.size + 1) { -1 }
        for (i in input.indices) {
            if (i != input.size - 1) {
                cups[input[i]] = input[i + 1]
            }
        }
        cups[input[input.size - 1]] = input[0]
        return cups
    }

    private fun execute(times: Int, input: IntArray = parseInput()): IntArray {
        var cups = buildArray(input)
        var current = input[0]
        for (i in 1 until times+1) {
            cups = pickUp(cups, current)
            current = cups[current]
        }
        return cups
    }

    override fun executePart1(name: String): Long {
        val cups = execute(100)
        val res = mutableListOf<Int>()
        var current = 1
        while (res.size < 8) {
            current = cups[current]
            res.add(current)
        }
        return res.joinToString("").toLong()
    }

    override fun executePart2(name: String): Long {
        val input = parseInput().toMutableList()
        val maxPlusOne = input.maxOrNull()!! + 1
        for (i in maxPlusOne until 1_000_001) {
            input.add(i)
        }
        val cups = execute(10_000_000, input.toIntArray())
        val a = cups[1]
        val b = cups[a]
        return a.toLong() * b.toLong()
    }

}
