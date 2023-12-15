package aoc2023

import getInputAsLines
import util.Day

private const val CYCLES = 1_000_000_000

class Day14: Day("14") {

    data class Field(val obstacles: Map<Int, List<Int>>, val movables: Map<Int, List<Int>>, val width: Int, val rows: Boolean = true) {
        fun transform(): Field {
            val newObstacles = (0 until width).associateWith { r ->
                obstacles.filter { (_,v) -> v.contains(r) }.map { (k,_) -> k }
            }
            val newMovables = (0 until  width).associateWith { r ->
                movables.filter { (_,v) -> v.contains(r) }.map { (k,_) -> k }
            }
            return Field(newObstacles, newMovables, width, !rows)
        }

        override fun toString(): String {
            if (!rows) {
                return transform().toString()
            }
            return (0 until width).joinToString("\n") { row ->
                (0 until width).map { col ->
                    if (obstacles.getOrDefault(row, emptyList()).contains(col)) {
                        '#'
                    } else if (movables.getOrDefault(row, emptyList()).contains(col)) {
                        'O'
                    } else {
                        '.'
                    }
                }.joinToString("")
            }
        }

        companion object {
            fun parse(lines: List<String>): Field {
                val obstacles = lines.indices.associateWith { rowIndex -> lines[rowIndex].indices.filter { lines[rowIndex][it] == '#' } }
                val movables = lines.indices.associateWith { rowIndex -> lines[rowIndex].indices.filter { lines[rowIndex][it] == 'O' } }
                return Field(obstacles, movables, lines.size, true)
            }
        }
    }

    private fun moveWest(field: Field): Field {
        val newMovables = field.movables.map { (rowIndex, movables) ->
            val obstacles =  listOf(-1) + field.obstacles[rowIndex]!!
            rowIndex to movables.groupBy { obstacles.last { o -> o < it } }
                .flatMap { (k,v) -> (1..v.count()).map { k + it } }
        }.toMap()
        return field.copy(movables = newMovables)
    }

    private fun moveEast(field: Field): Field {
        val newMovables = field.movables.map { (rowIndex, movables) ->
            val obstacles =  field.obstacles[rowIndex]!! + listOf(field.width)
            rowIndex to movables.groupBy { obstacles.first { o -> o > it } }
                .flatMap { (k,v) -> (1..v.count()).map { k - it } }
        }.toMap()
        return field.copy(movables = newMovables)
    }

    private fun moveNorth(field: Field): Field {
        return moveWest(field.transform()).transform()
    }

    private fun moveSouth(field: Field): Field {
        return moveEast(field.transform()).transform()
    }

    private fun cycle(field: Field): Field {
        return field
            .let { moveNorth(it) }
            .let { moveWest(it) }
            .let { moveSouth(it) }
            .let { moveEast(it) }
    }

    private fun totalLoad(field: Field): Int {
        if (field.rows) {
            return totalLoad(field.transform())
        }
        return field.movables.flatMap { it.value }.sumOf { field.width - it }
    }


    override fun executePart1(name: String): Any {
        return totalLoad(moveNorth(Field.parse(getInputAsLines(name, true))))
    }


    override fun executePart2(name: String): Int {
        var field = Field.parse(getInputAsLines(name, true))
        val seen = mutableListOf<Field>()
        val start: Int
        val period: Int
        while (true) {
            field = cycle(field)
            if (seen.contains(field)) {
                start = seen.indexOf(field) + 1
                period = seen.size - start + 1
                break
            } else {
                seen.add(field)
            }
        }

        val cycleTimes = (CYCLES -start) / period
        val executionsAfterCycle = CYCLES - cycleTimes * period - start
        for (i in 0 until executionsAfterCycle) {
            field = cycle(field)
        }
        return totalLoad(field)
    }
}