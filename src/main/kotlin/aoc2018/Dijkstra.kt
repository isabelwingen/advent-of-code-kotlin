package aoc2018

import java.util.LinkedList
import kotlin.random.Random
import java.util.PriorityQueue

class Dijkstra {

    data class QueueItem(val pos: Pair<Int, Int>, val value: Int)

    fun findPath(n: Int = 3): Any {
        val r = Random(123)
        val values = Array(n) { IntArray(n) { r.nextInt(0, n) } }
        values[0][0] = 0
        values[n-1][n-1] = 0
        println(values.map { it.toList() }.toList())

        val origin = Array(n) { Array<MutableSet<Pair<Int, Int>>>(n) { mutableSetOf() } }
        val distance = Array(n) { IntArray(n) { Int.MAX_VALUE} }
        distance[0][0] = 0
        val queue = LinkedList<Pair<Int, Int>>()
        (0 until n).forEach { x -> (0 until n).forEach { y -> queue.add(x to y) } }
        while (queue.isNotEmpty()) {
            val (x,y) = queue.poll()!!
            val q = listOf(x-1 to y, x+1 to y, x to y-1, x to y+1)
                .filter { (xx,yy) -> (0 until n).contains(xx) && (0 until n).contains(yy) }
                .filter { queue.contains(it) }
                .filter { values[it.first][it.second] >= 0 }
            q.forEach { (vx, vy) ->
                val alternativ = distance[x][y] + 1
                if (alternativ <= distance[vx][vy]) {
                    distance[vx][vy] = alternativ
                    origin[vx][vy].add(x to y)
                }
            }
            queue.sortBy { distance[it.first][it.second] }
        }
        val paths = mutableListOf<MutableList<Pair<Int, Int>>>()
        return distance[n-1][n-1]

    }
}