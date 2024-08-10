package aoc2017

import getInputAsLines
import util.Day

class Day13: Day("13") {

    private data class Layer(val id: Int, val depth: Int, val range: Int) {
        fun atZero(pico: Int, delay: Int) = (pico+delay).mod((range - 1) * 2) == 0
    }

    private fun parse(line: String, index: Int): Layer {
        val (d, r) = line.split(":").map { Integer.valueOf(it.trim()) }
        return Layer(index, d, r)
    }

    private fun runThroughFirewall(layers: Map<Int, Layer>, delay: Int = 0): Int {
        var score = 0
        if (layers[0] != null && layers[0]!!.atZero(0, delay)) {
            return 1
        }
        for (i in 1.. layers.values.maxOf { it.depth }) {
            val layer = layers[i]
            if (layer != null && layer.atZero(i, delay)) {
                score += layer.depth * layer.range
            }
        }
        return score
    }

    override fun executePart1(name: String): Any {
        val layers = getInputAsLines(name, true).mapIndexed { index, s -> parse(s, index) }.associateBy { it.depth }

        return runThroughFirewall(layers)
    }

    override fun executePart2(name: String): Any {
        val layers = getInputAsLines(name, true).mapIndexed { index, s -> parse(s, index) }.associateBy { it.depth }

        var i = 0
        while (i < 200_000_000) {
            if (runThroughFirewall(layers, i) == 0) {
                return i
            }
            i++
        }
        return -1
    }
}