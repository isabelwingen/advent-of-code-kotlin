package aoc2017

import getInputAsLines
import util.Day

class Day21: Day("21") {

    private data class Rule(val left: String, val right: String)

    private data class Grid(val rows: List<String>) {
        fun size() = rows.size

        fun rotateRight(): Grid {
            return Grid(rows.indices.map { rows.reversed().map { row -> row[it] } }.map { it.joinToString("") })
        }

        fun rotateLeft(): Grid {
            return Grid(rows.indices.reversed().map { rows.map { row -> row[it] } }.map { it.joinToString("") })
        }

        fun flip(): Grid {
            return Grid(rows.map { it.reversed() })
        }

        override fun toString() = rows.joinToString("/")

        fun matchRule(rule: Rule): Boolean {
            return allRotations().any { it.toString() == rule.left }
        }

        fun applyRule(rule: Rule): Grid {
            return fromString(rule.right)
        }

        fun breakUp(): List<List<Grid>> {
            return if (size().mod(2) == 0) {
                val newSize = size() / 2
                (0 until newSize).map { row ->
                    (0 until newSize).map { col ->
                        Grid(rows.drop(row * 2).take(2).map { it.drop(col * 2).take(2) })
                    }
                }
            } else {
                val newSize = size() / 3
                (0 until newSize).map { row ->
                    (0 until newSize).map { col ->
                        Grid(rows.drop(row * 3).take(3).map { it.drop(col * 3).take(3) })
                    }
                }
            }
        }

        fun allRotations(): Set<Grid> {
           return setOf(
               this,
               this.rotateRight(),
               this.rotateLeft(),
               this.rotateRight().rotateRight(),
               this.rotateRight().flip(),
               this.rotateLeft().flip(),
               this.rotateRight().rotateRight().flip(),
               this.flip()
           )
        }

        companion object {

            fun fromString(rule: String): Grid {
                return Grid(rule.split("/").map { it })
            }

            fun join(grids: List<List<Grid>>): Grid {
                val gridSize = grids[0][0].size()
                val newSize = grids.size * gridSize
                val rows = MutableList(newSize) { MutableList(newSize) { '.' } }
                grids.forEachIndexed { outerRow, r ->
                    r.forEachIndexed { outerCol, grid ->
                        grid.rows.forEachIndexed { row, chars ->
                            chars.forEachIndexed { col, c ->
                                rows[outerRow * gridSize + row][outerCol * gridSize + col] = c
                            }
                        }
                    }
                }
                return Grid(rows.map { it.joinToString("") }.toList())
            }
        }
    }

    private fun run(rules: List<Rule>, times: Int): Int {
        var grid = Grid.fromString(".#./..#/###")

        val cache = mutableMapOf<Pair<Grid, Rule>, Boolean>()

        fun matchRule(grid: Grid, rule: Rule): Boolean {
            return cache.getOrPut(grid to rule) { grid.matchRule(rule) }
        }

        fun findMatchingRule(grid: Grid): Rule {
            return rules.first { matchRule(grid, it) }
        }

        for (i in 1..times) {
            println("$i: ${grid.size()}")
            val newGrids = grid.breakUp().map { it.map { grid -> grid.applyRule(findMatchingRule(grid)) } }
            grid = Grid.join(newGrids)
        }
        return grid.toString().count { it == '#' }
    }


    override fun executePart1(name: String): Any {
        val rules = getInputAsLines(name, true)
            .map { it.split(" => ") }
            .map { Rule(it[0], it[1]) }

        return run(rules, 5)
    }

    override fun executePart2(name: String): Any {
        val rules = getInputAsLines(name, true)
            .map { it.split(" => ") }
            .map { Rule(it[0], it[1]) }

        return run(rules, 18)
    }
}