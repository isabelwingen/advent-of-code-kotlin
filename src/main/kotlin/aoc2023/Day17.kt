package aoc2023

import getInputAsLines
import util.Day
import util.Direction
import util.Position
import java.util.*
import kotlin.Comparator

class Day17: Day("17") {

    data class Node(val position: Position, val comingFrom: Direction, val streak: Int = 0)

    private fun getField(name: String): List<List<Int>> = getInputAsLines(name, true)
        .map { it.map { c -> c.toString().toInt() } }

    private fun getNeighbours(u: Node, field: List<List<Int>>): Collection<Node> {
        return when (u.comingFrom) {
            Direction.UP -> setOf(
                Node(u.position.move(Direction.DOWN), Direction.UP, u.streak + 1),
                Node(u.position.move(Direction.LEFT), Direction.RIGHT, 1),
                Node(u.position.move(Direction.RIGHT), Direction.LEFT, 1)
            )
            Direction.DOWN -> setOf(
                Node(u.position.move(Direction.UP), Direction.DOWN, u.streak + 1),
                Node(u.position.move(Direction.LEFT), Direction.RIGHT, 1),
                Node(u.position.move(Direction.RIGHT), Direction.LEFT, 1)
            )
            Direction.LEFT -> setOf(
                Node(u.position.move(Direction.RIGHT), Direction.LEFT, u.streak + 1),
                Node(u.position.move(Direction.UP), Direction.DOWN, 1),
                Node(u.position.move(Direction.DOWN), Direction.UP, 1)
            )
            Direction.RIGHT -> setOf(
                Node(u.position.move(Direction.LEFT), Direction.RIGHT, u.streak + 1),
                Node(u.position.move(Direction.DOWN), Direction.UP, 1),
                Node(u.position.move(Direction.UP), Direction.DOWN, 1)
            )
        }
            .filter { field.indices.contains(it.position.row) && field[0].indices.contains(it.position.col) }
    }

    private fun getNeighbours2(u: Node, field: List<List<Int>>): Collection<Node> {
        return if (u.streak >= 4) {
            getNeighbours(u, field)
        } else {
            when (u.comingFrom) {
                Direction.UP -> setOf(Node(u.position.move(Direction.DOWN), Direction.UP, u.streak + 1))
                Direction.DOWN -> setOf(Node(u.position.move(Direction.UP), Direction.DOWN, u.streak + 1))
                Direction.LEFT -> setOf(Node(u.position.move(Direction.RIGHT), Direction.LEFT, u.streak + 1))
                Direction.RIGHT -> setOf(Node(u.position.move(Direction.LEFT), Direction.RIGHT, u.streak + 1))
            }
                .filter { field.indices.contains(it.position.row) && field[0].indices.contains(it.position.col) }
        }
    }

    private fun initializeDistanceMatrix(field: List<List<Int>>): MutableMap<Node, Int> {
        val distance = hashMapOf<Node, Int>().withDefault { 100_000 }
        val a = Node(Position(0, 1), comingFrom = Direction.LEFT, streak = 1)
        val b = Node(Position(1, 0), comingFrom = Direction.UP, streak = 1)
        distance[a] = field[a.position.row][a.position.col]
        distance[b] = field[b.position.row][b.position.col]
        return distance
    }

    private fun dijkstra(maxStreak: Int, field: List<List<Int>>, neighbourFun: (Node, List<List<Int>>) -> Collection<Node>): Int {
        val distance = initializeDistanceMatrix(field)

        val compareByDistance: Comparator<Node> = compareBy { distance.getValue(it) }
        val queue = PriorityQueue(compareByDistance)
        distance.keys.forEach { queue.add(it) }

        val seen = hashSetOf<Node>()
        while (queue.isNotEmpty()) {
            val u = queue.poll()
            seen.add(u)
            val unvisitedNeighbours = neighbourFun(u, field)
                .filter { it.streak <= maxStreak }
                .filterNot { seen.contains(it) }
            for (v in unvisitedNeighbours) {
                val alt = distance.getValue(u) + field[v.position.row][v.position.col]
                if (alt < distance.getValue(v)) {
                    distance[v] = alt
                    queue.remove(v)
                    queue.add(v)
                }
            }
        }
        return distance
            .filter { (k, _) -> k.position.row == field.lastIndex && k.position.col == field[0].lastIndex }
            .minOf { it.value }
    }

    override fun executePart1(name: String): Any {
        val field = getField(name)
        return dijkstra(3, field, ::getNeighbours)
    }

    override fun executePart2(name: String): Any {
        val field = getField(name)
        return dijkstra(10, field, ::getNeighbours2)
    }
}