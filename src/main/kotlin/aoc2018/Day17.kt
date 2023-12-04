package aoc2018

import getInputAsLines
import util.Day
import java.util.LinkedList

class Day17: Day("17") {

    private fun getRange(string: String): IntRange {
        val (a,b) = string.split(",")[1].split("=")[1].split("..").map { it.trim().toInt() }
        return a..b
    }

    private fun emptyBelow(pos: Pair<Int, Int>, solids: Set<Pair<Int, Int>>, waterPooled: MutableSet<Pair<Int, Int>>): Boolean {
        val y = pos.first + 1
        val x = pos.second
        return !(solids.contains(y to x) || waterPooled.contains(y to x))
    }

    private fun barricadeToTheRight(pos: Pair<Int, Int>, solids: Set<Pair<Int, Int>>, waterPooled: MutableSet<Pair<Int, Int>>): Pair<Pair<Int, Int>, Boolean> {
        var current = pos
        while (!solids.contains(current.first to current.second+1) && !emptyBelow(current, solids, waterPooled)) {
            current = current.first to current.second+1
        }
        return if (!emptyBelow(current, solids, waterPooled)) {
            current to true
        } else {
            current to false
        }
    }

    private fun barricadeToTheLeft(pos: Pair<Int, Int>, solids: Set<Pair<Int, Int>>, waterPooled: MutableSet<Pair<Int, Int>>): Pair<Pair<Int, Int>, Boolean> {
        var current = pos
        while (!solids.contains(current.first to current.second-1) && !emptyBelow(current, solids, waterPooled)) {
            current = current.first to current.second-1
        }
        return if (!emptyBelow(current, solids, waterPooled)) {
            current to true
        } else {
            current to false
        }
    }

    private fun run(name: String): Pair<Int, Int> {
        val (xRanges, yRanges) = getInputAsLines(name)
            .filter { it.isNotBlank() }
            .groupBy { it.split(",")[0].contains("x") }
            .toSortedMap()
            .values
            .map { r -> r.map { it.split(",")[0].split("=")[1].trim().toInt() to getRange(it) } }
        val solids = mutableSetOf<Pair<Int, Int>>()
        val waterFlownThrough = mutableSetOf<Pair<Int, Int>>()
        val waterPooled = mutableSetOf<Pair<Int, Int>>()
        yRanges.forEach { (x, yRange) -> yRange.forEach { y -> solids.add(y to x) } }
        xRanges.forEach { (y, xRange) -> xRange.forEach { x -> solids.add(y to x) } }
        val maxY = solids.maxOf { it.first }
        val minY = solids.minOf { it.first }
        val queue = LinkedList<Pair<Int, Int>>()
        var c = 0
        queue.add(0 to 500)
        while (queue.isNotEmpty()) {
            val current = queue.poll()

            if (waterPooled.contains(current)) {
                continue
            }
            if (current.first > maxY) {
                continue
            }
            c++
            waterFlownThrough.add(current)
            if (emptyBelow(current, solids, waterPooled)) {
                queue.add(current.first + 1 to current.second)
                continue
            }
            val (barricadeRightPos, barricadeRightPresent)  = barricadeToTheRight(current, solids, waterPooled)
            val (barricadeLeftPos, barricadeLeftPresent)  = barricadeToTheLeft(current, solids, waterPooled)
            if (barricadeRightPresent && barricadeLeftPresent) {
                var p = barricadeLeftPos
                while (p != barricadeRightPos) {
                    waterFlownThrough.add(p)
                    waterPooled.add(p)
                    p = p.first to p.second+1
                }
                waterFlownThrough.add(p)
                waterPooled.add(p)
                queue.add(current.first-1 to current.second)
                continue
            } else if (barricadeRightPresent) {
                var p = barricadeRightPos
                while (p != barricadeLeftPos) {
                    waterFlownThrough.add(p)
                    p = p.first to p.second-1
                }
                waterFlownThrough.add(p)

                queue.add(p)
            } else if (barricadeLeftPresent) {
                var p = barricadeLeftPos
                while (p != barricadeRightPos) {
                    waterFlownThrough.add(p)
                    p = p.first to p.second+1
                }
                waterFlownThrough.add(p)
                queue.add(p)
            } else {
                var p = barricadeLeftPos
                while (p != barricadeRightPos) {
                    waterFlownThrough.add(p)
                    p = p.first to p.second+1
                }
                waterFlownThrough.add(p)
                queue.add(barricadeLeftPos)
                queue.add(barricadeRightPos)
            }
        }
        return waterFlownThrough.count { it.first >= minY } to waterPooled.count { it.first >= minY }
    }

    override fun executePart1(name: String): Any {
        return run(name).first
    }

    override fun executePart2(name: String): Any {
        return run(name).second
    }
}