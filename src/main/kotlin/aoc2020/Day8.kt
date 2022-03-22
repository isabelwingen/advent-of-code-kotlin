package aoc2020

import getResourceAsList

enum class Operation {
    NOP,
    ACC,
    JMP
}

fun toOperation(string: String): Operation {
    when (string) {
        "nop" -> return Operation.NOP
        "acc" -> return Operation.ACC
        "jmp" -> return Operation.JMP
    }
    return Operation.NOP
}

class Instruction(val operator: Operation, val argument: Int) {
    override fun toString(): String {
        return "$operator $argument"
    }
}

private fun parseInput(path: String): List<Instruction> {
    return getResourceAsList(path)
        .filter { it.isNotBlank() }
        .map { it.split(" ") }
        .map { Instruction(toOperation(it[0]), Integer.parseInt(it[1])) }
}

private fun executeProgramm(program: List<Instruction>): Pair<Int, Boolean> {
    var acc = 0
    val visited = mutableSetOf<Int>()
    var currentInstruction = 0
    while (currentInstruction < program.size && !visited.contains(currentInstruction)) {
        visited.add(currentInstruction)
        when (program[currentInstruction].operator) {
            Operation.NOP -> {
                currentInstruction += 1
            }
            Operation.ACC -> {
                acc += program[currentInstruction].argument
                currentInstruction += 1
            }
            Operation.JMP -> {
                currentInstruction += program[currentInstruction].argument
            }
        }
    }
    return Pair(acc, currentInstruction == program.size)
}

fun executeDay8Part1(): Int {
    val instructions = parseInput("example/day8_2.txt")
    return executeProgramm(instructions).first
}

fun executeDay8Part2(): Int {

    fun switchCommand(instruction: Instruction): Instruction {
        return when (instruction.operator) {
            Operation.NOP -> Instruction(Operation.JMP, instruction.argument)
            Operation.JMP -> Instruction(Operation.NOP, instruction.argument)
            else -> instruction
        }
    }

    val instructions = parseInput("day8.txt")
    val positionsToChange = instructions.mapIndexed { index, op -> Pair(index, op)}
        .filter { it.second.operator == Operation.NOP || it.second.operator == Operation.JMP }
        .map { it.first }
    val listOfPrograms = mutableListOf<List<Instruction>>()
    for (i in positionsToChange) {
        val x = instructions.toMutableList()
        x[i] = switchCommand(x[i])
        listOfPrograms.add(x.toList())
    }
    return listOfPrograms
        .toList()
        .map { executeProgramm(it) }
        .first { it.second }
        .first
}