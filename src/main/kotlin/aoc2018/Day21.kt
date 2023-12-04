package aoc2018

import getInputAsLines
import util.Day

class Day21: Day("21") {

    private fun executeCommand(command: Day19.Command, system: Day19.System): Day19.System {
        val (instruction, a, b, c) = command
        val (registers, pointer, boundedRegister) = system
        val workingRegister = registers.toMutableList()
        workingRegister[boundedRegister] = pointer
        workingRegister[c] = when (instruction) {
            "addr" ->  workingRegister[a] + workingRegister[b]
            "addi" -> workingRegister[a] + b
            "mulr" -> workingRegister[a] * workingRegister[b]
            "muli" -> workingRegister[a] * b
            "banr" -> (workingRegister[a] and workingRegister[b])
            "bani" -> (workingRegister[a] and b)
            "borr" -> (workingRegister[a] or workingRegister[b])
            "bori" -> (workingRegister[a] or b)
            "setr" -> workingRegister[a]
            "seti" -> a
            "gtir" -> if (a > workingRegister[b]) 1 else 0
            "gtri" -> if (workingRegister[a] > b) 1 else 0
            "gtrr" -> if (workingRegister[a] > workingRegister[b]) 1 else 0
            "eqir" -> if (a == workingRegister[b]) 1 else 0
            "eqri" -> if (workingRegister[a] == b) 1 else 0
            "eqrr" -> if (workingRegister[a] == workingRegister[b]) 1 else 0
            else -> workingRegister[c]
        }
        return Day19.System(workingRegister.toList(), workingRegister[boundedRegister] + 1, boundedRegister)
    }

    private fun parseInstruction(instruction: String): Day19.Command {
        val (instr, a, b, c) = instruction.split(" ").map { it.trim() }
        return Day19.Command(instr, a.toInt(), b.toInt(), c.toInt())
    }

    private fun executeProcess(name: String, register0Value: Int = 0): Int {
        val lines = getInputAsLines(name, true)
        val instructionPointerRegister = lines[0].split(" ")[1].trim().toInt()
        val registers = List(6) { if (it == 0) register0Value else 0 }
        val instructions = lines.drop(1).map(::parseInstruction)
        var system = Day19.System(registers, 0, instructionPointerRegister)
        val numbers = mutableListOf<Int>()
        while (system.instructionPointer in instructions.indices) {
            if (system.instructionPointer == 28) {
                if (numbers.contains(system.registers[1])) {
                    println("Result: ${numbers.last()}")
                } else {
                    numbers.add(system.registers[1])
                }
            }
            system = executeCommand(instructions[system.instructionPointer], system)
        }
        return system.registers[0]
    }

    override fun executePart1(name: String): Any {
        return executeProcess(name, 1797184)
    }

    override fun executePart2(name: String): Any {
        return executeProcess(name, 11011493)
    }
}