package aoc2022

import getInputAsLines
import util.Day

class Day14: Day("14") {

    private fun handleLine(line: String): HashMap<Int, HashMap<Int, Char>> {
        val board = HashMap<Int, HashMap<Int, Char>>() //x to y; col to row
        val points = line.split(" -> ")
            .map { it.split(",").map { c -> c.toInt() } }
            .map { it[0] to it[1] }
        for (i in 0 until points.size - 1) {
            val start = points[i]
            val end = points[i+1]
            var x = start.first
            var y = start.second
            board.putIfAbsent(x, hashMapOf())
            board[x]!!.putIfAbsent(y, '#')
            while (x to y != end) {
                if (start.first == end.first) { // y is moving
                    if (start.second <= end.second) { //y is increasing
                        y++
                    } else {
                        y--
                    }
                } else { // x is moving
                    if (start.first <= end.first) { //x is increasing
                        x++
                    } else {
                        x--
                    }
                }
                board.putIfAbsent(x, hashMapOf())
                board[x]!!.putIfAbsent(y, '#')
            }
        }
        return board
    }

    private fun createBoard(name: String): HashMap<Int, HashMap<Int, Char>> {
        val board = HashMap<Int, HashMap<Int, Char>>()
        getInputAsLines(name)
            .filter { it.isNotEmpty() }
            .map { handleLine(it) }
            .forEach { map -> map.forEach { (t, u) ->
                board.putIfAbsent(t, u)
                board[t]!!.putAll(u)
            } }

        return board
    }

    private fun isBoardPositionEmpty(y: Int, x: Int, board: HashMap<Int, HashMap<Int, Char>>): Boolean {
        return board.getOrDefault(x, HashMap()).getOrDefault(y, '.') == '.'
    }

    private fun placeSandAt(y: Int, x: Int, board: HashMap<Int, HashMap<Int, Char>>) {
        board.putIfAbsent(x, HashMap())
        board[x]!![y] = 'o'
    }

    override fun executePart1(name: String): Long {
        val board = createBoard(name)
        printBoard(board)
        while(true) {
            var sand_x = 500
            var sand_y = -1
            while (true) {
                if (isBoardPositionEmpty(sand_y+1, sand_x, board)) {
                    sand_y++
                } else if (isBoardPositionEmpty(sand_y+1, sand_x-1, board)) {
                    sand_y++
                    sand_x--
                } else if (isBoardPositionEmpty(sand_y+1, sand_x+1, board)) {
                    sand_y++
                    sand_x++
                } else {
                    placeSandAt(sand_y, sand_x, board)
                    break
                }
                if (sand_y > 164) {
                    println("Result: ")
                    printBoard(board)
                    return board.values.flatMap { it.values }.count { it == 'o' }.toLong()
                }
            }
        }
    }

    override fun executePart2(name: String): Long {
        val board = createBoard(name)
        printBoard(board)
        val floor = board.values.flatMap { it.keys }.maxOf { it } + 2
        while(true) {
            var sand_x = 500
            var sand_y = -1
            while (true) {
                if (sand_y+1 == floor) {
                    placeSandAt(sand_y, sand_x, board)
                    break
                } else if (isBoardPositionEmpty(sand_y+1, sand_x, board)) {
                    sand_y++
                } else if (isBoardPositionEmpty(sand_y+1, sand_x-1, board)) {
                    sand_y++
                    sand_x--
                } else if (isBoardPositionEmpty(sand_y+1, sand_x+1, board)) {
                    sand_y++
                    sand_x++
                } else {
                    placeSandAt(sand_y, sand_x, board)
                    if (sand_y == 0 && sand_x == 500) {
                        return board.values.flatMap { it.values }.count { it == 'o' }.toLong()
                    }
                    break
                }
            }
        }
    }

    private fun printHeader(minX: Int, maxX: Int) {
        print("    ")
        print(minX.toString().padStart(3, '0')[0])
        IntRange(minX+1, maxX-1)
            .toList()
            .map { it.toString().padStart(3, '0') }
            .map { it[0] to it[2] }
            .forEach { if (it.second == '5' || it.second == '0') print(it.first) else print(' ') }
        println(maxX.toString().padStart(3, '0')[0])
        print("    ")
        print(minX.toString().padStart(3, '0')[1])
        IntRange(minX+1, maxX-1)
            .toList()
            .map { it.toString().padStart(3, '0') }
            .map { it[1] to it[2] }
            .forEach { if (it.second == '5' || it.second == '0') print(it.first) else print(' ') }
        println(maxX.toString().padStart(3, '0')[1])
        print("    ")
        print(minX.toString().padStart(3, '0')[2])
        IntRange(minX+1, maxX-1)
            .toList()
            .map { it.toString().padStart(3, '0') }
            .map { it[2] }
            .forEach { if (it == '5' || it == '0') print(it) else print(' ') }
        println(maxX.toString().padStart(3, '0')[2])
    }

    fun printBoard(board: HashMap<Int, HashMap<Int, Char>> ) {
        val maxY = board.values.flatMap { it.keys }.maxOf { it } + 1
        val minX = board.keys.minOf { it }
        val maxX = board.keys.maxOf { it }
        printHeader(minX, maxX)
        for (y in 0 until maxY) {
            print("$y".padStart(3, '0').padEnd(4, ' '))
            for (x in minX until maxX + 1) {
                print(board.getOrDefault(x, HashMap()).getOrDefault(y, "."))
            }
            println()
        }
    }
}