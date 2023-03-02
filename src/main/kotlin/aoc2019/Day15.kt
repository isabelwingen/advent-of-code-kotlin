package aoc2019

import aoc2019.util.IntCode
import util.Day
import java.lang.IllegalStateException
import java.util.LinkedList

class Day15: Day("15") {

    private fun back(dir: Int): Int {
        return when(dir) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            WEST -> EAST
            EAST -> WEST
            else -> throw IllegalStateException()
        }
    }

    private fun goInDirection(element: Element, dir: Int): Pair<Long, Element> {
        var (x, y) = element.currentPos
        val prog = element.intCode.copy()
        val res = prog.execute(dir)
        val newPath = MutableList(element.path.size + 1)
        { if (it < element.path.size) element.path[it] else dir }

        when (dir) {
            NORTH -> y -= 1
            SOUTH -> y += 1
            WEST ->  x -= 1
            EAST ->  x += 1
        }

        return res to Element(newPath, prog, x to y)
    }

    class Element(val path: List<Int>, val intCode: IntCode, val currentPos: Pair<Int, Int>)

    private fun findOxygenTank(file: String): List<Int> {
        val queue = LinkedList<Element>()
        val intCode = IntCode("Day15", file)
        queue.add(Element(emptyList(), intCode, 0 to 0))
        while (queue.isNotEmpty()) {
            val current = queue.pop()!!
            val newDirections = mutableListOf(NORTH, SOUTH, WEST, EAST)
            if (current.path.isNotEmpty()) {
                newDirections.remove(back(current.path.last()))
            }
            for (dir in newDirections) {
                val (res, newElement) = goInDirection(current, dir)
                if (res == HALLWAY) {
                    queue.add(newElement)
                } else if (res == GOAL) {
                    return newElement.path
                }
            }
        }
        return emptyList()
    }

    override fun executePart1(name: String): Long {
        return findOxygenTank(name).size.toLong()
    }


    override fun executePart2(name: String): Long {
        // move robotor to oxygen tank
        val pathToOxygenTank = findOxygenTank(name)
        val intCode = IntCode("Day15", name)
        pathToOxygenTank.forEach { intCode.execute(it) }

        val queue = LinkedList<Element>()
        queue.add(Element(emptyList(), intCode, 0 to 0))
        val seen = mutableSetOf<Pair<Int, Int>>()
        seen.add(0 to 0)
        var longestPath = 0
        while (queue.isNotEmpty()) {
            val current = queue.pop()!!
            val newDirections = mutableListOf(NORTH, SOUTH, WEST, EAST)
            if (current.path.isNotEmpty()) {
                newDirections.remove(back(current.path.last()))
            }
            for (dir in newDirections) {
                val (res, newElement) = goInDirection(current, dir)
                if (!seen.contains(newElement.currentPos)) {
                    seen.add(newElement.currentPos)
                    if (res == HALLWAY || res == GOAL) {
                        longestPath = newElement.path.size
                        queue.add(newElement)
                    }
                }
            }
        }
        return longestPath.toLong()
    }

    companion object {
        val HALLWAY = 1L
        val GOAL = 2L

        val NORTH = 1
        val SOUTH = 2
        val WEST = 3
        val EAST = 4
    }
}