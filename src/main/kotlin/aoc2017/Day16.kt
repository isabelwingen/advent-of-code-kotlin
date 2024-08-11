package aoc2017

import getInput
import util.Day

class Day16: Day("16") {

    private interface Instruction

    private data class SInstruction(val x: Int): Instruction
    private data class XInstruction(val x: Int, val y: Int): Instruction
    private data class PInstruction(val x: Char, val y: Char): Instruction

    private fun parseCommand(command: String): Instruction {
        return if (command.startsWith('s')) {
            SInstruction(Integer.valueOf(command.drop(1)))
        } else if (command.startsWith('x')) {
            val (x, y) = command.drop(1).split("/")
            XInstruction(x.toInt(), y.toInt())
        } else if (command.startsWith('p')) {
            val (x, y) = command.drop(1).split("/").map { it.first() }
            PInstruction(x, y)
        } else {
            throw IllegalStateException()
        }
    }

    private fun dance(p: MutableList<Char>, instructions: List<Instruction>): MutableList<Char> {
        var programs = p.toMutableList()
        for (instruction in instructions) {
            if (instruction is SInstruction) {
                programs = (programs.takeLast(instruction.x) + programs.dropLast(instruction.x)).toMutableList()
            } else if (instruction is XInstruction) {
                val a = programs[instruction.x]
                programs[instruction.x] = programs[instruction.y]
                programs[instruction.y] = a
            } else if (instruction is PInstruction) {
                val i = programs.indexOf(instruction.x)
                val j = programs.indexOf(instruction.y)
                programs[i] = instruction.y
                programs[j] = instruction.x
            }
        }
        return programs
    }

    override fun executePart1(name: String): Any {
        val instructions = getInput(name).trim().split(",")
            .map { parseCommand(it) }
        val programs = (97..112).map { it.toChar() }.toMutableList()

        return dance(programs, instructions).joinToString("")
    }

    private fun dance(p: MutableList<Char>, instructions: List<Instruction>, n: Int): MutableList<Char> {
        var prog = p
        for (i in 0 until n) {
            prog = dance(prog, instructions)
        }
        return prog
    }

    override fun executePart2(name: String): Any {
        val instructions = getInput(name).trim().split(",")
            .map { parseCommand(it) }
        var programs = (97..112).map { it.toChar() }.toMutableList()
        val seen = mutableListOf<String>()
        for (i in 0 until 1_000_000_000) {
            programs = dance(programs, instructions)
            val progStr = programs.joinToString("")
            if (seen.contains(progStr)) {
                assert(seen.indexOf(progStr) == 0)

                break
            } else {
                seen.add(progStr)
            }
        }
        val p = (97..112).map { it.toChar() }
        return dance(p.toMutableList(), instructions, 1_000_000_000.mod(seen.size)).joinToString("")
    }
}