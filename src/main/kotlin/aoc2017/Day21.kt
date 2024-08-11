package aoc2017

import getInputAsLines
import util.Day

class Day21: Day("21") {

    private data class Rule(val left: String, val right: String)

    private data class Grid(val rows: List<List<Char>>) {
        fun size() = rows.size

        //.#.   .#.   #..   ###
        //..#   #..   #.#   ..#
        //###   ###   ##.   .#.

        fun rotateRight(): Grid {
            return Grid(rows.indices.map { rows.reversed().map { row -> row[it] } }.map { it })
        }

        fun rotateLeft(): Grid {
            return Grid(rows.indices.reversed().map { rows.map { row -> row[it] } }.map { it })
        }

        fun flip(): Grid {
            return Grid(rows.map { it.reversed() })
        }

        override fun toString() = rows.joinToString("/") { it.joinToString("") }

        private fun matchRule(rule: Rule): Boolean {
            return allRotations().any { it.toString() == rule.left }
        }

        private fun applyRule(rule: Rule): Grid {
            return fromString(rule.right)
        }

        fun applyMatchingRule(rules: List<Rule>): Grid {
            val matchingRule = rules.first { matchRule(it) }
            return applyRule(matchingRule)
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
                return Grid(rule.split("/").map { it.toList() })
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
                return Grid(rows.map { it.toList() }.toList())
            }
        }
    }

    override fun executePart1(name: String): Any {
        var grid = Grid.fromString(".#./..#/###")
        val rules = getInputAsLines(name, true)
            .map { it.split(" => ") }
            .map { Rule(it[0], it[1]) }

        for (i in 0..17) {
            println("$i: ${grid.size()}")
            val newGrids = grid.breakUp().map { it.map { grid -> grid.applyMatchingRule(rules) } }
            grid = Grid.join(newGrids)
        }
        return grid.toString().count { it == '#' }
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}