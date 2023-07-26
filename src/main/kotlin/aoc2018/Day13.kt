package aoc2018

import getInputAsLines
import util.Day
import kotlin.reflect.jvm.internal.impl.incremental.components.Position

class Day13: Day("13") {

    private data class Rail(var left: Rail? = null, var right: Rail? = null, var up: Rail? = null, var down: Rail? = null, val value: Char, val position: Pair<Int, Int>) {
        override fun toString() = "$value"

        override fun equals(other: Any?): Boolean {
            return if (other is Rail) {
                position == other.position
            } else {
                false
            }
        }
    }

    private data class Cart(var rail: Rail, var direction: Char, var step: Int = 0, var dead: Boolean = false)

    private fun parseInput(name: String): List<Cart> {
        val lines = getInputAsLines(name).filter { it.isNotBlank() }
        val numberOfCols = lines.maxOf { it.length }
        val rails = MutableList(lines.size) { MutableList<Rail?>(numberOfCols) { null } }
        val carts = mutableListOf<Cart>()

        for (row in lines.indices) {
            for (col in 0 until numberOfCols) {
                val cell = lines[row].getOrElse(col) { ' ' }
                val left = rails[row].getOrNull(col-1)
                val up = rails.getOrElse(row-1) { emptyList() }.getOrNull(col)
                if (cell == '-' || cell == '<' || cell == '>') {
                    rails[row][col] = Rail(left = left, value = '-', position = row to col)
                    left!!.right = rails[row][col]
                } else if (cell == '|' || cell == '^' || cell == 'v') {
                    rails[row][col] = Rail(up = up, value = '|', position = row to col)
                    up!!.down = rails[row][col]
                } else if (cell == '+') {
                    rails[row][col] = Rail(left = left, up = up, value = '+', position = row to col)
                    left!!.right = rails[row][col]
                    up!!.down = rails[row][col]
                } else if (cell == '\\') {
                    if (col != 0 && setOf('-', '<', '+', '>').contains(left?.value)) {
                        //upper right corner
                        rails[row][col] = Rail(left = left, down = null, value = '\\', position = row to col)
                        left!!.right = rails[row][col]
                    } else {
                        //lower left corner
                        rails[row][col] = Rail(up = up, right = null, value = '\\', position = row to col)
                        up!!.down = rails[row][col]
                    }
                } else if (cell == '/') {
                    if (col != 0 && setOf('-', '<', '+', '>').contains(left?.value)) {
                        //lower right corner
                        rails[row][col] = Rail(left = left, up = up, value = '/', position = row to col)
                        left!!.right = rails[row][col]
                        up!!.down = rails[row][col]
                    } else {
                        //upper left corner
                        rails[row][col] = Rail(value = '/', position = row to col)
                    }
                }
                if (cell == '>' || cell == '<' || cell == '^' || cell == 'v') {
                    carts.add(Cart(rails[row][col]!!, cell))
                }
            }
        }
        return carts.toList()
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
        val carts = parseInput(name)
        while (true) {
            carts.sortedBy { it.rail.position.second }.sortedBy { it.rail.position.first }.forEach { cart ->
                moveCart(cart)
                val p = carts.filter { it != cart }.find { it.rail.position == cart.rail.position }
                if (p != null) {
                    return p.rail.position
                }
            }
        }
    }

    override fun executePart2(name: String): Any {
        var carts = parseInput(name)
        while (carts.size > 1) {
            for (cart in carts.sortedBy { it.rail.position.second }.sortedBy { it.rail.position.first }) {
                if (!cart.dead) {
                    moveCart(cart)
                    val p = carts.filter { it != cart }.find { it.rail.position == cart.rail.position }
                    if (p != null) {
                        cart.dead = true
                        p.dead = true
                    }
                }
            }
            carts = carts.filterNot { it.dead }
        }
        return carts.first().rail.position
    }
}