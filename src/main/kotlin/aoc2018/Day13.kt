package aoc2018

import getInputAsLines
import util.Day

class Day13: Day("13") {


    private data class Rail(
        var left: Rail? = null,
        var right: Rail? = null,
        var up: Rail? = null,
        var down: Rail? = null,
        val value: Char,
        val position: Pair<Int, Int>
    ) {
        override fun toString() = "$value"

        override fun equals(other: Any?) = if (other is Rail) other.position == position else false
    }

    private data class Cart(
        var rail: Rail,
        var direction: Char,
        var step: Int = 0,
        var dead: Boolean = false
    )

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
                rails[row][col] = when (cell) {
                    '-', '<', '>' -> {
                        Rail(left = left, value = '-', position = row to col).also {
                            left!!.right = it
                        }
                    }
                    '|', '^', 'v' -> {
                        Rail(up = up, value = '|', position = row to col).also {
                            up!!.down = it
                        }
                    }
                    '+' -> {
                        Rail(left = left, up = up, value = '+', position = row to col).also {
                            left!!.right = it
                            up!!.down = it
                        }
                    }
                    '\\' -> {
                        if (col != 0 && setOf('-', '<', '+', '>').contains(left?.value)) {
                            // upper right corner
                            Rail(left = left, down = null, value = '\\', position = row to col).also {
                                left!!.right = it
                            }
                        } else {
                            // lower left corner
                            Rail(up = up, right = null, value = '\\', position = row to col).also {
                                up!!.down = it
                            }
                        }
                    }
                    '/' -> {
                        if (col != 0 && setOf('-', '<', '+', '>').contains(left?.value)) {
                            // lower right corner
                            Rail(left = left, up = up, value = '/', position = row to col).also {
                                left!!.right = it
                                up!!.down = it
                            }
                        } else {
                            // upper left corner
                            Rail(value = '/', position = row to col)
                        }
                    }
                    else -> null
                }
                if (cell in setOf('>', '<', '^', 'v')) {
                    rails[row][col]?.let { rail -> carts.add(Cart(rail, cell)) }
                }
            }
        }
        return carts.toList()
    }

    private fun doMove(cart: Cart, turnLeft: Char, turnRight: Char, next: (Cart) -> Rail) {
        cart.rail = next(cart)
        when (cart.rail.value) {
            '\\' -> {
                cart.direction = if (cart.direction in setOf('>', '<')) turnRight else turnLeft
            }
            '/' -> {
                cart.direction = if (cart.direction in setOf('>', '<')) turnLeft else turnRight
            }
            '+' -> {
                when (cart.step) {
                    0 -> {
                        cart.direction = turnLeft
                        cart.step = 1
                    }
                    1 -> {
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
                val p = carts.find { it != cart && it.rail.position == cart.rail.position }
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
                    val p = carts.find { it != cart && it.rail.position == cart.rail.position }
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