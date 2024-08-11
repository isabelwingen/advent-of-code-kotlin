package aoc2017

import getInputAsLines
import util.Day
import java.util.*

class Day18: Day("18") {
    private interface Instruction
    private data class Snd(val x: Any): Instruction
    private data class Set(val x: String, val y: Any): Instruction
    private data class Add(val x: String, val y: Any): Instruction
    private data class Mul(val x: String, val y: Any): Instruction
    private data class Mod(val x: String, val y: Any): Instruction
    private data class Rcv(val x: String): Instruction
    private data class Jgz(val x: Any, val y: Any): Instruction

    private fun toStringOrLong(a: String): Any {
        return try {
            a.toLong()
        } catch (e: Exception) {
            a
        }
    }

    private fun parseInstruction(str: String): Instruction {
        val l = str.split(" ").map { toStringOrLong(it) }
        val a = l.first()
        val x = l[1]
        return when (a) {
            "set" -> {
                Set(x as String, l[2])
            }
            "snd" -> {
                Snd(x)
            }
            "add" -> {
                Add(x as String, l[2])
            }
            "mul" -> {
                Mul(x as String, l[2])
            }
            "mod" -> {
                Mod(x as String, l[2])
            }
            "rcv" -> {
                Rcv(x as String)
            }
            "jgz" -> {
                Jgz(x, l[2])
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }

    private fun getValue(value: Any, registers: Map<String, Long>): Long {
        return when (value) {
            is String -> {
                registers.getValue(value)
            }
            is Long -> {
                value
            }
            else -> {
                throw IllegalStateException("value $value, ${value.javaClass}")
            }
        }
    }

    override fun executePart1(name: String): Any {
        val instructions = getInputAsLines(name, true)
            .map { parseInstruction(it) }
        val registers = mutableMapOf<String, Long>().withDefault { 0L }
        var index = 0L
        var lastPlayedSound = 0L
        while (index in instructions.indices) {
            val instruction = instructions[index.toInt()]
            if (instruction is Snd) {
                lastPlayedSound = getValue(instruction.x, registers)
            } else if (instruction is Set) {
                registers[instruction.x] = getValue(instruction.y, registers)
            } else if (instruction is Add) {
                registers[instruction.x] = registers.getValue(instruction.x) + getValue(instruction.y, registers)
            } else if (instruction is Mul) {
                registers[instruction.x] = registers.getValue(instruction.x) * getValue(instruction.y, registers)
            } else if (instruction is Mod) {
                registers[instruction.x] = registers.getValue(instruction.x).mod(getValue(instruction.y, registers))
            } else if (instruction is Rcv) {
                if (getValue(instruction.x, registers) != 0L) {
                    return lastPlayedSound
                }
            } else if (instruction is Jgz) {
                if (getValue(instruction.x, registers) > 0L) {
                    index += getValue(instruction.y, registers)
                    index--
                }
            }
            index++
        }
        return -1
    }

    private data class Run(val inputQueue: LinkedList<Long>, val outputQueue: LinkedList<Long>, var index: Long, val registers: MutableMap<String, Long>, val instructions: List<Instruction>, var sendCount: Int = 0)

    private fun runProgramUntilHalt(run: Run): Run {
        var (inputQueue, outputQueue, index, registers, instructions, count) = run
        while (index in instructions.indices) {
            val instruction = instructions[index.toInt()]
            if (instruction is Snd) {
                outputQueue.addLast(getValue(instruction.x, registers))
                count++
            } else if (instruction is Set) {
                registers[instruction.x] = getValue(instruction.y, registers)
            } else if (instruction is Add) {
                registers[instruction.x] = registers.getValue(instruction.x) + getValue(instruction.y, registers)
            } else if (instruction is Mul) {
                registers[instruction.x] = registers.getValue(instruction.x) * getValue(instruction.y, registers)
            } else if (instruction is Mod) {
                registers[instruction.x] = registers.getValue(instruction.x).mod(getValue(instruction.y, registers))
            } else if (instruction is Rcv) {
                if (inputQueue.isEmpty()) {
                    return run.copy(index = index, sendCount = count)
                } else {
                    registers[instruction.x] = inputQueue.pollFirst()
                }
            } else if (instruction is Jgz) {
                if (getValue(instruction.x, registers) > 0L) {
                    index += getValue(instruction.y, registers)
                    index--
                }
            }
            index++
        }
        return run.copy(index = index, sendCount = count)
    }

    private fun programNotHalted(run: Run): Boolean {
        return run.registers.size == 1 || ((run.index in run.instructions.indices) && run.inputQueue.isNotEmpty())
    }

    override fun executePart2(name: String): Any {
        val instructions = getInputAsLines(name, true)
            .map { parseInstruction(it) }
        val registers0 = mutableMapOf("p" to 0L).withDefault { 0L }
        val registers1 = mutableMapOf("p" to 1L).withDefault { 0L }
        val queue0 = LinkedList<Long>()
        val queue1 = LinkedList<Long>()
        var run0 = Run(queue0, queue1, 0, registers0, instructions)
        var run1 = Run(queue1, queue0, 0, registers1, instructions)
        while (programNotHalted(run0) || programNotHalted(run1)) {
            run0 = runProgramUntilHalt(run0)
            run1 = runProgramUntilHalt(run1)
        }
        return run1.sendCount
    }

}