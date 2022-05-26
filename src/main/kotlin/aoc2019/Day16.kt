package aoc2019

import getInput
import util.Day
import kotlin.math.abs


class Day16: Day("16") {

    private fun Array<IntArray>.multiply(rightMatrix: Array<IntArray>): Array<IntArray> {
        val r1 = this.size
        val c1 = this.first().size
        val c2 = rightMatrix.first().size
        val prod = Array(r1) { IntArray(c2) { 0 } }
        for (i in 0 until r1) {
            for (j in 0 until c2) {
                for (k in 0 until c1) {
                    prod[i][j] += this[i][k] * rightMatrix[k][j]
                }
            }
        }

        for (i in 0 until r1) {
            for (j in 0 until c2) {
                prod[i][j] = abs(prod[i][j]) % 10
            }
        }

        return prod
    }

    private fun Array<IntArray>.multiply(vector: IntArray): IntArray {
        return this.multiply(Array(vector.size) { row -> IntArray(1) {vector[row]} }).map { it.first() }.toIntArray()
    }

    private fun IntArray.multiply(vector: IntArray): IntArray {
        return IntArray(this.size) {this[it] * vector[it]}
    }
    fun <T> Sequence<T>.repeatIndefinitely(): Sequence<T> =
        generateSequence(this) { this }.flatten()

    fun <T> List<T>.repeatIndefinitely(): Sequence<T> =
        this.asSequence().repeatIndefinitely()

    private fun createLine(n: Int, length: Int): IntArray {

        return listOf(0, 1, 0, -1)
            .flatMap {  i -> List(n) { i } }
            .repeatIndefinitely()
            .drop(1)
            .take(length)
            .toList()
            .toIntArray()
    }

    private fun createMatrix(m: Int): Array<IntArray> {
        return Array(m) { createLine(it + 1, m) }
    }

    override fun executePart1(name: String): Any {
        var input = getInput(name)
            .trim()
            .map { it.toString().toInt() }
            .toIntArray()
        val a = createMatrix(input.size)

        for (i in 0 until 100) {
            input = a.multiply(input)
        }
        return input.toList().take(8).joinToString("").toInt()

    }

    private fun triangleMatrix(n: Int): Array<IntArray> {
        return Array(n) {row -> IntArray(n) {col -> if (col < row) 0 else 1} }
    }

    override fun expectedResultPart1() = 69549155

    override fun executePart2(name: String): Any {
        var input = List(10_000) { _ -> getInput(name).trim().map { it.toString().toInt() } }.flatten().toIntArray()
        val skip = input.take(7).joinToString("").toInt()
        input = input.sliceArray(IntRange(skip, input.size - 1))

        for (j in 0 until 100) {
            for (i in input.size - 2 downTo 0) {
                input[i] = (input[i] + input[i + 1]) % 10
            }
        }
        return input.take(8).joinToString("").toInt()
    }

    override fun expectedResultPart2() = 83253465
}