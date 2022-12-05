package aoc2019

import aoc2019.util.IntCode
import util.Day
import java.util.LinkedList

class Day23: Day("23") {

    private fun removeAndAddAtBeginning(progs: MutableList<Pair<Int, IntCode>>, key: Int) {
        val pos = progs.withIndex().first { it.value.first == key }.index
        progs.add(0, progs.removeAt(pos))
    }

    override fun executePart1(name: String): Any {
        val progs = MutableList(50) { index -> index to IntCode("$index", name) }
        progs.forEach { it.second.execute(it.first.toLong(), true) }
        val outputs = HashMap<Int, LinkedList<Long>>()
        val inputs = HashMap<Int, LinkedList<Long>>()
        IntRange(0, 49).forEach { outputs[it] = LinkedList() }
        IntRange(0, 49).forEach { inputs[it] = LinkedList() }

        while (true) {
            // execute programs with given input (or -1 if none is given). Write output to output stream
            for (prog in progs) {
                val res = if (inputs[prog.first]!!.isEmpty()) {
                    prog.second.execute(true)
                } else {
                    val p = LinkedList<Long>()
                    p.addAll(inputs[prog.first]!!)
                    inputs[prog.first]!!.clear()
                    prog.second.execute(p, true)
                }
                if (res != -123456789L) { // received output
                    outputs[prog.first]!!.add(res)
                }
            }
            // write outputs to inputs
            outputs.forEach { entry ->
                if (entry.value.size >= 3) {
                    val address = entry.value.pop()!!.toInt()
                    if (address == 255) {
                        entry.value.pop()
                        return entry.value.pop()!!
                    }
                    inputs[address]!!.add(entry.value.pop()!!)
                    inputs[address]!!.add(entry.value.pop()!!)
                }
            }
        }
    }

    override fun expectedResultPart1(): Any {
        TODO("Not yet implemented")
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }

    override fun expectedResultPart2(): Any {
        TODO("Not yet implemented")
    }
}
