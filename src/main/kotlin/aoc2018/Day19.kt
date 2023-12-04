package aoc2018

import getInputAsLines
import util.Day

class Day19: Day("19") {

    data class Command(val instruction: String, val a: Int, val b: Int, val c: Int)

    data class System(val registers: List<Int>, val instructionPointer: Int, val boundedRegister: Int)

    private fun executeCommand(command: Command, system: System): System {
        val (instruction, a,b,c) = command
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
        return System(workingRegister.toList(), workingRegister[boundedRegister]+1, boundedRegister)
    }

    private fun parseInstruction(instruction: String): Command {
        val (instr, a, b, c) = instruction.split(" ").map { it.trim() }
        return Command(instr, a.toInt(), b.toInt(), c.toInt())
    }

    private fun executeProcess(name: String, register0Value: Int = 0): Int {
        val lines = getInputAsLines(name, true)
        val instructionPointerRegister = lines[0].split(" ")[1].trim().toInt()
        val registers = List(6) { if (it == 0) register0Value else 0 }
        val instructions = lines.drop(1).map(::parseInstruction)
        var system = System(registers, 0, instructionPointerRegister)
        while (system.instructionPointer in instructions.indices) {
            system = executeCommand(instructions[system.instructionPointer], system)
        }
        return system.registers[0]
    }

    override fun executePart1(name: String): Any {
        return executeProcess(name)
    }

    private fun Int.factors(): List<Int> =
        (1..this).mapNotNull { n ->
            if(this % n == 0) n else null
        }

    override fun executePart2(name: String): Any {
        return 10551376.factors().sum()
    }
}