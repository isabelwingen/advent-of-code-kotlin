package aoc2017

import getInput
import getInputAsLines
import util.Day

class Day23: Day("23") {

    private interface Instruction
    private data class Set(val x: String, val y: Any): Instruction
    private data class Sub(val x: String, val y: Any): Instruction
    private data class Mul(val x: String, val y: Any): Instruction
    private data class Mod(val x: String, val y: Any): Instruction
    private data class Jnz(val x: Any, val y: Any): Instruction

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
            "sub" -> {
                Sub(x as String, l[2])
            }
            "mul" -> {
                Mul(x as String, l[2])
            }
            "mod" -> {
                Mod(x as String, l[2])
            }
            "jnz" -> {
                Jnz(x, l[2])
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
        var muls = 0L
        while (index in instructions.indices) {
            val instruction = instructions[index.toInt()]
            if (instruction is Set) {
                registers[instruction.x] = getValue(instruction.y, registers)
            } else if (instruction is Sub) {
                registers[instruction.x] = registers.getValue(instruction.x) - getValue(instruction.y, registers)
            } else if (instruction is Mul) {
                muls++
                registers[instruction.x] = registers.getValue(instruction.x) * getValue(instruction.y, registers)
            } else if (instruction is Mod) {
                registers[instruction.x] = registers.getValue(instruction.x).mod(getValue(instruction.y, registers))
            } else if (instruction is Jnz) {
                if (getValue(instruction.x, registers) != 0L) {
                    index += getValue(instruction.y, registers)
                    index--
                }
            }
            index++
        }
        return muls
    }

    // https://todd.ginsberg.com/post/advent-of-code/2017/day23/
    override fun executePart2(name: String): Any {
        val a = getInputAsLines(name, true).first().split(" ")[2].toInt() * 100 + 100000
        return (a .. a+17000 step 17).count {
            !it.toBigInteger().isProbablePrime(5)
        }
    }
}