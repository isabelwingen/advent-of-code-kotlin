package aoc2023

import getInputAsLines
import splitBy
import util.Day
import kotlin.math.min

class Day13: Day("13") {
    
    
    private fun difference(a: String, b: String): Int {
        if (a == b) {
            return 0
        }
        return a.indices.toList().count { a[it] != b[it] }
    }

    private fun findMirror(field: List<String>, difference: Int = 0, factor: Int = 100): Int {
        for (numberOfRowsBefore in 1 .. field.lastIndex) {
            val numberOfRowsAfter = field.size - numberOfRowsBefore
            val sizeOfMirroredArea = min(numberOfRowsBefore, numberOfRowsAfter)
            val rowsBefore = field.drop(numberOfRowsBefore - sizeOfMirroredArea).take(sizeOfMirroredArea)
            val rowsAfter = field.drop(numberOfRowsBefore).take(sizeOfMirroredArea).reversed()
            if (rowsBefore.indices.sumOf { difference(rowsBefore[it], rowsAfter[it]) } == difference) {
                return numberOfRowsBefore * factor
            }
        }
        if (factor == 100) {
            val transformedField = field[0].indices.map { field.map { row -> row[it] }.joinToString("") }
            return findMirror(transformedField, difference, 1)
        } else {
            throw IllegalStateException("Found no axis")
        }

    }


    override fun executePart1(name: String): Any {
        return getInputAsLines(name, false)
            .splitBy { it.isBlank() }
            .filterNot { it.isEmpty() || it.first().isBlank() }
            .sumOf { findMirror(it) }
    }

    override fun executePart2(name: String): Any {
        return getInputAsLines(name, false)
            .splitBy { it.isBlank() }
            .filterNot { it.isEmpty() || it.first().isBlank() }
            .sumOf { findMirror(it, 1) }
    }
}