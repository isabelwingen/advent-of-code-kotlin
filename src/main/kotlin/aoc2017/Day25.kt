package aoc2017

import getInputAsLines
import splitBy
import util.Day

class Day25: Day("25") {

    private data class Command(val write: Int, val move: String, val next: String)

    private data class State(val name: String, val zeroCommand: Command, val oneCommand: Command)

    private data class TuringMachine(val startState: String, val steps: Int, val states: Map<String, State>)

    private fun parseCommand(command: List<String>): Command {
        return Command(
            command[0].split(" ").last().dropLast(1).trim().toInt(),
            command[1].split(" ").last().dropLast(1).trim(),
            command[2].split(" ").last().dropLast(1).trim(),
        )
    }

    private fun parseState(state: List<String>): State {
        val name = state[0].split(" ").last().dropLast(1)
        return State(
            name,
            parseCommand(state.drop(2).take(3)),
            parseCommand(state.drop(6).take(3))
        )
    }

    private fun parseInput(name: String): TuringMachine {
        val input = getInputAsLines(name, false)
            .splitBy { it.isBlank() }
            .filterNot { it.isEmpty() || it.first().isBlank() }
        val startState = input[0][0].split(" ").last().dropLast(1)
        val steps = input[0][1].split(" ")[5].trim().toInt()
        val states = input.drop(1).map { parseState(it) }
        return TuringMachine(startState, steps, states.associateBy { it.name })
    }

    override fun executePart1(name: String): Any {
        val turingMachine = parseInput(name)
        val ones = mutableSetOf<Int>()

        fun getValue(pos: Int) = if (ones.contains(pos)) { 1 } else { 0 }

        fun writeValue(pos: Int, value: Int) {
            if (value == 1) {
                ones.add(pos)
            } else {
                ones.remove(pos)
            }
        }

        var steps = 0
        var currentState = turingMachine.states[turingMachine.startState]!!
        var currentPosition = 0

        fun executeCommand(command: Command) {
            writeValue(currentPosition, command.write)
            if (command.move == "left") {
                currentPosition--
            } else {
                currentPosition++
            }
            currentState = turingMachine.states[command.next]!!
        }

        while (steps < turingMachine.steps) {
            val currentValue = getValue(currentPosition)
            if (currentValue == 0) {
                executeCommand(currentState.zeroCommand)
            } else {
                executeCommand(currentState.oneCommand)
            }
            steps++
        }
        return ones.size
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}