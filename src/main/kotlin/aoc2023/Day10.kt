package aoc2023

import aoc2018.Day23
import getInputAsLines
import splitBy
import util.Day

class Day10: Day("10") {

    data class Position(val row: Int, val col: Int) {
        fun goNorth(): Position = Position(row-1, col)
        fun goSouth(): Position = Position(row+1, col)
        fun goWest() = Position(row, col-1)
        fun goEast() = Position(row, col+1)
    }

    abstract class Pipe {
        abstract fun enterThrough(direction: Char): (Position) -> Position
    }

    class NorthSouthPipe: Pipe() {
        override fun enterThrough(direction: Char): (Position) -> Position {
            return when (direction) {
                NORTH -> { pos: Position -> pos.goSouth() }
                SOUTH -> { pos: Position -> pos.goNorth() }
                else -> throw IllegalStateException("Could not enter through $direction")
            }
        }
    }

    class NorthWestPipe: Pipe() {
        override fun enterThrough(direction: Char): (Position) -> Position {
            return when (direction) {
                NORTH -> { pos: Position -> pos.goWest() }
                WEST -> { pos: Position -> pos.goNorth() }
                else -> throw IllegalStateException("Could not enter through $direction")
            }
        }
    }

    class NorthEastPipe: Pipe() {
        override fun enterThrough(direction: Char): (Position) -> Position {
            return when (direction) {
                NORTH -> { pos: Position -> pos.goEast() }
                EAST -> { pos: Position -> pos.goNorth() }
                else -> throw IllegalStateException("Could not enter through $direction")
            }
        }
    }

    data class State(val currentPosition: Position, val pipe: Char, val comingFrom: Char) {
        private fun makeNewState2(goingTo: Char, lines: List<CharArray>): State {
            val newPos = when (goingTo) {
                SOUTH -> currentPosition.goSouth()
                NORTH -> currentPosition.goNorth()
                WEST -> currentPosition.goWest()
                EAST -> currentPosition.goEast()
                else -> throw IllegalStateException("Unknown direction: $goingTo")
            }
            val newPipe = lines[newPos.row][newPos.col]
            val newComingFrom = when (goingTo) {
                NORTH -> SOUTH
                SOUTH -> NORTH
                EAST -> WEST
                WEST -> EAST
                else -> throw IllegalStateException("Unknown direction: $goingTo")
            }
            return State(newPos, newPipe, newComingFrom)
        }

        fun transition(lines: List<CharArray>): State {
            return when (pipe) {
                NORTH_SOUTH -> when (comingFrom) {
                    NORTH -> makeNewState2(SOUTH, lines)
                    SOUTH -> makeNewState2(NORTH, lines)
                    else -> throw IllegalStateException("Could not transition")
                }
                NORTH_WEST -> when (comingFrom) {
                    NORTH -> makeNewState2(WEST, lines)
                    WEST -> makeNewState2(NORTH, lines)
                    else -> throw IllegalStateException("Could not transition")
                }
                NORTH_EAST -> when (comingFrom) {
                    NORTH -> makeNewState2(EAST, lines)
                    EAST -> makeNewState2(NORTH, lines)
                    else -> throw IllegalStateException("Could not transition")
                }
                SOUTH_EAST -> when (comingFrom) {
                    SOUTH -> makeNewState2(EAST, lines)
                    EAST -> makeNewState2(SOUTH, lines)
                    else -> throw IllegalStateException("Could not transition")
                }
                SOUTH_WEST -> when (comingFrom) {
                    SOUTH -> makeNewState2(WEST, lines)
                    WEST -> makeNewState2(SOUTH, lines)
                    else -> throw IllegalStateException("Could not transition")
                }
                EAST_WEST -> when (comingFrom) {
                    EAST -> makeNewState2(WEST, lines)
                    WEST -> makeNewState2(EAST, lines)
                    else -> throw IllegalStateException("Could not transition")
                }
                else -> throw java.lang.IllegalStateException("Unknown pipe: $pipe")
            }
        }
    }

    private fun findStartSteps(lines: List<CharArray>): Set<State> {
        val startRow = lines.indexOfFirst { it.contains(START) }
        val startCol = lines[startRow].indexOf(START)
        val steps = mutableSetOf<State>()
        Position(startRow-1, startCol).let {
            val value = lines.getOrElse(it.row) { CharArray(0) }.getOrElse(it.col) { GROUND }
            if (setOf(NORTH_SOUTH, SOUTH_WEST, SOUTH_EAST).contains(value)) {
                steps.add(State(it, value, SOUTH))
            }
        }
        Position(startRow+1, startCol).let {
            val value = lines.getOrElse(it.row) { CharArray(0) }.getOrElse(it.col) { GROUND }
            if (setOf(NORTH_SOUTH, NORTH_WEST, NORTH_EAST).contains(value)) {
                steps.add(State(it, value, NORTH))
            }
        }
        Position(startRow, startCol-1).let {
            val value = lines.getOrElse(it.row) { CharArray(0) }.getOrElse(it.col) { GROUND }
            if (setOf(EAST_WEST, SOUTH_EAST, NORTH_EAST).contains(value)) {
                steps.add(State(it, value, EAST))
            }
        }
        Position(startRow, startCol+1).let {
            val value = lines.getOrElse(it.row) { CharArray(0) }.getOrElse(it.col) { GROUND }
            if (setOf(EAST_WEST, SOUTH_WEST, NORTH_WEST).contains(value)) {
                steps.add(State(it, value, WEST))
            }
        }
        return steps.toSet()
    }

    override fun executePart1(name: String): Any {
        val lines = getInputAsLines(name, true)
            .map { it.toCharArray() }
        var currentStep = findStartSteps(lines).first()
        var c = 1
        while (currentStep.pipe != 'S') {
            currentStep = currentStep.transition(lines)
            c++
        }
        return c / 2
    }

    override fun executePart2(name: String): Any {
        val lines = getInputAsLines(name, true)
            .map { it.toCharArray() }
        val newLines = Array(lines.size) { CharArray(lines[0].size) { GROUND } }
        var currentStep = findStartSteps(lines).first()
        while (currentStep.pipe != 'S') {
            currentStep.currentPosition.let {
                newLines[it.row][it.col] = lines[it.row][it.col]
            }
            currentStep = currentStep.transition(lines)
        }
        currentStep.currentPosition.let {
            newLines[it.row][it.col] = lines[it.row][it.col]
        }

        var innies = 0L
        var connectionsToRowAbove: Int
        for (line in newLines) {
            connectionsToRowAbove = 0
            for (i in line.indices) {
               line[i].let {
                   if (it == '.') {
                       if (connectionsToRowAbove.mod(2) == 1) {
                           innies++
                       }
                   } else if (it == NORTH_EAST || it == NORTH_WEST || it == NORTH_SOUTH || it == 'S') {
                       connectionsToRowAbove ++
                   }
               }
            }
        }
        return innies
    }

    companion object {
        const val NORTH = 'N'
        const val SOUTH = 'S'
        const val WEST = 'W'
        const val EAST = 'E'
        const val NORTH_SOUTH = '|'
        const val EAST_WEST = '-'
        const val SOUTH_EAST = 'F'
        const val NORTH_EAST = 'L'
        const val NORTH_WEST = 'J'
        const val SOUTH_WEST = '7'
        const val GROUND = '.'
        const val START = 'S'
    }
}