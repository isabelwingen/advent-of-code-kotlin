package aoc2023

import aoc2018.Day23
import getInputAsLines
import util.Day
import java.util.*
import kotlin.collections.HashMap

class Day16: Day("16") {

    enum class Object {
        NEGATIVE_MIRROR,
        POSITIVE_MIRROR,
        VERTICAL_SLICE,
        HORIZONTAL_SLICE;

        companion object {
            fun from(char: Char): Object {
                return when (char) {
                    '-' -> HORIZONTAL_SLICE
                    '|' -> VERTICAL_SLICE
                    '/' -> POSITIVE_MIRROR
                    '\\' -> NEGATIVE_MIRROR
                    else -> throw IllegalStateException("Unknown object: $char")
                }
            }
        }
    }

    data class Position(val row: Int, val col: Int)

    enum class Direction {
        LEFT, RIGHT, UP, DOWN
    }

    data class State(val position: Position, val direction: Direction) {

        fun move(dir: Direction = direction): State {
            return when (dir) {
                Direction.LEFT -> State(position.copy(col = position.col-1), dir)
                Direction.RIGHT -> State(position.copy(col = position.col+1), dir)
                Direction.UP -> State(position.copy(row = position.row-1), dir)
                Direction.DOWN -> State(position.copy(row = position.row+1), dir)
            }
        }

        private fun negativeMirror(dir: Direction = direction): State {
            return when (dir) {
                Direction.LEFT -> move(Direction.UP)
                Direction.RIGHT ->  move(Direction.DOWN)
                Direction.UP -> move(Direction.LEFT)
                Direction.DOWN -> move(Direction.RIGHT)
            }
        }

        private fun positiveMirror(dir: Direction = direction): State {
            return when (dir) {
                Direction.LEFT -> move(Direction.DOWN)
                Direction.RIGHT ->  move(Direction.UP)
                Direction.UP -> move(Direction.RIGHT)
                Direction.DOWN -> move(Direction.LEFT)
            }
        }

        private fun horizontalSlice(dir: Direction = direction): Set<State> {
            return if (dir == Direction.LEFT || dir == Direction.RIGHT) {
                setOf(move())
            } else {
                setOf(move(Direction.LEFT), move(Direction.RIGHT))
            }
        }

        private fun verticalSlice(dir: Direction = direction): Set<State> {
            return if (dir == Direction.UP || dir == Direction.DOWN) {
                setOf(move())
            } else {
                setOf(move(Direction.UP), move(Direction.DOWN))
            }
        }

        fun moveThroughObstacle(obstacle: Object, dir: Direction = direction): Set<State> {
            return when (obstacle) {
                Object.NEGATIVE_MIRROR -> setOf(negativeMirror(dir))
                Object.POSITIVE_MIRROR -> setOf(positiveMirror(dir))
                Object.HORIZONTAL_SLICE -> horizontalSlice(dir)
                Object.VERTICAL_SLICE -> verticalSlice(dir)
            }
        }
    }

    data class Field(val width: Int, val height: Int, val objects: Map<Position, Object>) {

        fun illegal(position: Position): Boolean {
            return position.row < 0 || position.col < 0 || position.row >= height || position.col >= width
        }
    }

    private fun parseField(lines: List<String>): Field {
        val width = lines[0].length
        val height = lines.size
        val objects = mutableMapOf<Position, Object>()
        for (row in lines.indices) {
            for (col in lines[0].indices) {
                lines[row][col].let {
                    if (it != '.') {
                        objects[Position(row, col)] = Object.from(it)
                    }
                }
            }
        }
        return Field(width, height, objects.toMap())
    }

    private fun runThrough(field: Field, startState: State): Int {
        val queue = LinkedList<State>()
        queue.add(startState)
        val energized = mutableSetOf<State>()
        while (queue.isNotEmpty()) {
            val state = queue.poll()
            if (energized.contains(state) || field.illegal(state.position)) {
                continue
            }
            energized.add(state)
            val thing = field.objects[state.position]
            if (thing == null) {
                queue.addFirst(state.move())
            } else {
                queue.addAll(0, state.moveThroughObstacle(thing))
            }
        }
        return energized.map { it.position }.distinct().count()
    }

    override fun executePart1(name: String): Any {
        val field = parseField(getInputAsLines(name, true))
        return runThrough(field, State(Position(0, 0), Direction.RIGHT))
    }

    override fun executePart2(name: String): Any {
        val field = parseField(getInputAsLines(name, true))
        return listOf(
            (0..field.width).map { runThrough(field, State(Position(0, it), Direction.DOWN)) },
            (0..field.width).map { runThrough(field, State(Position(field.height-1, it), Direction.UP)) },
            (0 .. field.height).map { runThrough(field, State(Position(it, 0), Direction.RIGHT)) },
            (0 .. field.height).map { runThrough(field, State(Position(it, field.width-1), Direction.LEFT)) })
            .flatten()
            .maxOf { it }
    }
}