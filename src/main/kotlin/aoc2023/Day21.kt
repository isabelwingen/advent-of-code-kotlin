package aoc2023

import getInputAsLines
import util.Day
import util.Position
import java.math.BigInteger

class Day21: Day("21") {
    override fun executePart1(name: String): Any {
        val grid = getInputAsLines(name, true)
            .map { it.toCharArray().toList() }
        val startingPosition = grid.indexOfFirst { it.any { c -> c == 'S' } }.let { row ->
            Position(row, grid[row].indexOf('S'))
        }


        val emptyNeighbours = fun(position: Position): List<Position> {
            return position.getNeighbours(grid.indices, grid[0].indices)
                .filter { grid[it.row][it.col] != '#' }
        }

        var positions = setOf(startingPosition)
        var steps = 0
        while (steps < 64) {
            positions = positions.flatMap { emptyNeighbours(it) }.toSet()
            steps++
        }
        return positions.size
    }

    private fun isRock(grid: List<List<Char>>, row: Int, col: Int): Boolean {
        return grid[row.mod(131)][col.mod(131)] == '#'
    }

    override fun executePart2(name: String): Any {
        // 26_501_356 = 65 + 131*202300
        // 131 ist auch die Grid-Size und das Muster wiederholt sich periodisch
        // => Finde Regelmäßigkeit für n=131*k + 65

//        val grid = getInputAsLines(name, true)
//            .map { it.toCharArray().toList() }
//        val startingPosition = grid.indexOfFirst { it.any { c -> c == 'S' } }.let { row ->
//            Position(row, grid[row].indexOf('S'))
//        }
//
//        val emptyNeighbours = fun(position: Position): List<Position> {
//            return position.getNeighbours().filterNot { isRock(grid, it.row, it.col) }
//        }
//        var positions = setOf(startingPosition)
//        var steps = 1
//        var stepsToReachableTiles = mutableMapOf(0 to 1)
//        while (steps < 328) {
//            positions = positions.flatMap { emptyNeighbours(it) }.toSet()
//            stepsToReachableTiles[steps] = positions.size
//            steps++
//        }
//
//        val resultMap = stepsToReachableTiles
//        println("65: ${resultMap[65]}")
//        println("196: ${resultMap[196]}")
//        println("327: ${resultMap[327]}")

        // Sei x_n das Ergebnis nach n Schritten und y_n = x_n - x_n-1
        // Dann fällt auf, dass y_n+1 - y_n = const = 30_465L
        // Somit ist y_n = y_0 + n * 30_465 und x_n = ist die Summe von y_0 .. y_n-1 + x_0
        val x0 = 3859L
        var y0 = BigInteger.valueOf(30465L)
        var res = y0.plus(BigInteger.valueOf(x0))
        var count = 1L
        while (count < 202300) {
            y0 = y0.plus(BigInteger.valueOf(30_346L))
            res += y0
            count++
        }
        return res

    }
}