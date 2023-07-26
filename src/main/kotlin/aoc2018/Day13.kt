package aoc2018

import getInputAsLines
import util.Day

class Day13: Day("13") {

    private fun printInput(name: String) {
        println()
        getInputAsLines(name)
            .filter { it.isNotBlank() }
            .forEach { println(it) }

    }

    private data class Rail(var left: Rail? = null, var right: Rail? = null, var up: Rail? = null, var down: Rail? = null, val value: Char, val position: Pair<Int, Int>) {
        override fun toString() = "$value"
    }

    private data class Cart(var rail: Rail, var direction: Char, var step: Int = 0)

    private fun parseInput(name: String): Pair<List<List<Rail?>>, List<Cart>> {
        val lines = getInputAsLines(name).filter { it.isNotBlank() }
        val numberOfCols = lines.maxOf { it.length }
        val rails = MutableList(lines.size) { MutableList<Rail?>(numberOfCols) { null } }
        val carts = mutableListOf<Cart>()
        for (row in lines.indices) {
            for (col in 0 until numberOfCols) {
                val cell = lines[row].getOrElse(col) { ' ' }
                if (cell == '-' || cell == '<' || cell == '>') {
                    val left = rails[row][col-1]
                    rails[row][col] = Rail(left = left, value = '-', position = row to col)
                    left!!.right = rails[row][col]
                } else if (cell == '|' || cell == '^' || cell == 'v') {
                    val up = rails[row-1][col]
                    rails[row][col] = Rail(up = up, value = '|', position = row to col)
                    up!!.down = rails[row][col]
                } else if (cell == '+') {
                    val left = rails[row][col-1]
                    val up = rails[row-1][col]
                    rails[row][col] = Rail(left = left, up = up, value = '+', position = row to col)
                    left!!.right = rails[row][col]
                    up!!.down = rails[row][col]
                } else if (cell == '\\') {
                    if (col != 0 && (lines[row][col-1] == '-' || lines[row][col-1] == '+' || lines[row][col-1] == '<' || lines[row][col-1] == '>')) {
                        //from down to left
                        val left = rails[row][col-1]
                        rails[row][col] = Rail(left = left, down = null, value = '\\', position = row to col)
                        left!!.right = rails[row][col]
                    } else {
                        //from up to right
                        val up = rails[row-1][col]
                        rails[row][col] = Rail(up = up, right = null, value = '\\', position = row to col)
                        up!!.down = rails[row][col]
                    }
                } else if (cell == '/') {
                    if (col != 0 && row != 0 && (lines[row][col-1] == '-' || lines[row][col-1] == '+' || lines[row][col-1] == '<' || lines[row][col-1] == '>')) {
                        //from left to up
                        val left = rails[row][col-1]
                        val up = rails[row-1][col]
                        rails[row][col] = Rail(left = left, up = up, value = '/', position = row to col)
                        left!!.right = rails[row][col]
                        up!!.down = rails[row][col]
                    } else {
                        //from down to right
                        rails[row][col] = Rail(value = '/', position = row to col)
                    }
                }
                if (cell == '>' || cell == '<' || cell == '^' || cell == 'v') {
                    carts.add(Cart(rails[row][col]!!, cell))
                }
            }
        }
        return rails.map { it.toList() }.toList() to carts.toList()
    }

    private fun doMove(cart: Cart, turnLeft: Char, turnRight: Char, next: (Cart) -> Rail) {
        cart.rail = next(cart)
        when (cart.rail.value) {
            '\\' -> {
                if (cart.direction == '>' || cart.direction == '<') {
                    cart.direction = turnRight
                } else {
                    cart.direction = turnLeft
                }
            }
            '/' -> {
                if (cart.direction == '>' || cart.direction == '<') {
                    cart.direction = turnLeft
                } else {
                    cart.direction = turnRight
                }
            }
            '+' -> {
                when (cart.step) {
                    0 -> {
                        cart.direction = turnLeft
                        cart.step = 1
                    }
                    1 -> {
                        cart.direction = cart.direction
                        cart.step = 2
                    }
                    2 -> {
                        cart.direction = turnRight
                        cart.step = 0
                    }
                }
            }
        }

    }

    private fun moveCart(cart: Cart) {
        when (cart.direction) {
            '>' -> {
                doMove(cart, '^', 'v') { c: Cart -> c.rail.right!! }
            }
            '<' -> {
                doMove(cart, 'v', '^') { c: Cart -> c.rail.left!! }
            }
            'v' -> {
                doMove(cart, '>', '<') { c: Cart -> c.rail.down!! }
            }
            '^' -> {
                doMove(cart, '<', '>') { c: Cart -> c.rail.up!! }
            }
        }
    }

    override fun executePart1(name: String): Pair<Int, Int> {
        val (_, carts) = parseInput(name)
        var i = 0
       while (i < 1000) {
           carts.forEach { moveCart(it) }
           val p = carts.map { it.rail.position }
           if (p.size != p.distinct().size) {
               println(p.size - p.distinct().size)
               return p.groupBy { it }.map { it.value }.first { it.size > 1 }.first()
           }
           i++
       }
        return 0 to 0
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}