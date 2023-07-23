package aoc2018

import getInput
import util.Day
import java.lang.Math.abs

class Day5: Day("5") {
    private fun resolve(polymer: MutableList<Char>): MutableList<Char> {
        var i = 0
        while (i < polymer.lastIndex) {
            val a = polymer[i]
            val b = polymer[i+1]
            if (abs(a.code-b.code) == 32) {
                polymer.removeAt(i+1)
                polymer.removeAt(i)
                if (i > 0) {
                    i--
                }
            } else {
                i++
            }
        }
        return polymer.toMutableList()
    }

    override fun executePart1(name: String): Any {
        return resolve(getInput(name).trim().toCharArray().toMutableList()).count()
    }

    override fun executePart2(name: String): Any {
        val originalPolymer = getInput(name).trim().toCharArray()
        var min = Int.MAX_VALUE
        for (a in CharRange('a', 'z')) {
            val b = (a.code-32).toChar()
            val newPolymer = originalPolymer.filter { it != a && it != (a-32) }
            val x = resolve(newPolymer.toMutableList()).count()
            if (x < min) {
                min = x
            }
        }
        return min
    }
}