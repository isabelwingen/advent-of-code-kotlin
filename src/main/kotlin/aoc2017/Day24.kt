package aoc2017

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day24: Day("24") {
    override fun executePart1(name: String): Any {
        val connectors = getInputAsLines(name, true)
            .map { it.split("/") }
            .map { it.map { x -> x.toInt() } }
        val queue = LinkedList<List<List<Int>>>()
        var result = listOf(listOf(0, 0))
        queue.add(result)
        while (queue.isNotEmpty()) {
            val path = queue.poll()
            println(path)
            val lastElement = if (path.size == 1) {
                0
            } else if (path.last().toSet().size == 1) {
                path.last().last()
            } else {
                val l = path.last()
                val q = path.dropLast(1).last()
                l.first { !q.contains(it) }
            }
            val candidates = connectors
                .filterNot { path.contains(it) }
                .filter { it.contains(lastElement) }
            if (candidates.isEmpty()) {
                if (result.flatten().sum() < path.flatten().sum()) {
                    result = path
                }
            } else {
                candidates.forEach { queue.add(path + listOf(it)) }
            }
        }
        return result.flatten().sum()
    }


    override fun executePart2(name: String): Any {
        val connectors = getInputAsLines(name, true)
            .map { it.split("/") }
            .map { it.map { x -> x.toInt() } }
        val queue = LinkedList<List<List<Int>>>()
        var result = mutableSetOf(listOf(listOf(0, 0)))
        queue.add(listOf(listOf(0,0)))
        while (queue.isNotEmpty()) {
            val path = queue.poll()
            val lastElement = if (path.size == 1) {
                0
            } else if (path.last().toSet().size == 1) {
                path.last().last()
            } else {
                val l = path.last()
                val q = path.dropLast(1).last()
                l.first { !q.contains(it) }
            }
            val candidates = connectors
                .filterNot { path.contains(it) }
                .filter { it.contains(lastElement) }
            if (candidates.isEmpty()) {
                if (result.first().size < path.size) {
                    result = mutableSetOf(path)
                } else if (result.first().size == path.size) {
                    result.add(path)
                }
            } else {
                candidates.forEach { queue.add(path + listOf(it)) }
            }
        }
        return result.maxOf { it.flatten().sum() }
    }
}