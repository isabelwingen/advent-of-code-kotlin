package aoc2017

import util.Day
import java.util.LinkedList

class Day17: Day("17") {
    override fun executePart1(name: String): Any {
        val steps = 380
        val queue = LinkedList<Int>()
        queue.add(0)
        var current = 0
        for (i in 1..10) {
            current = (current + steps).mod(queue.size)
            queue.add(current+1, i)
            current++
            println(queue)
        }
        println(queue)
        return queue[queue.indexOf(2017) + 1]
    }

    //[0, 1]
    //[0, 1, 2]
    //[0, 1, 3, 2]
    //[0, 1, 3, 4, 2]
    //[0, 1, 3, 4, 5, 2]
    //[0, 6, 1, 3, 4, 5, 2]
    //[0, 6, 1, 3, 7, 4, 5, 2]
    //[0, 8, 6, 1, 3, 7, 4, 5, 2]
    //[0, 8, 6, 1, 9, 3, 7, 4, 5, 2]
    //[0, 8, 6, 1, 9, 10, 3, 7, 4, 5, 2]
    //[0, 8, 6, 1, 9, 10, 3, 7, 4, 5, 2]
    override fun executePart2(name: String): Any {
        val steps = 380
        var queueSize = 1
        var after0 = 0
        var current = 0
        for (i in 1..50_000_000) {
            current = (current + steps).mod(queueSize) + 1
            queueSize++
            if (current == 1) {
                after0 = i
            }
        }
        return after0
    }
}