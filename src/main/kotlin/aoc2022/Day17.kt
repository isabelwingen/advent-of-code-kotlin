package aoc2022

import getInputAsLines
import util.Day
import kotlin.math.abs

class Day17: Day("17") {

    private fun getWind(name: String) = getInputAsLines(name)[0].toCharArray()

    private fun createChamber() = List(50) { if (it == 0) CharArray(7) { '-' } else CharArray(7) { ' ' } }

    private fun rollChamber(v: Int, chamber: List<CharArray>): List<CharArray> {
        return (IntRange(1, v). map { CharArray(7) { ' ' } } + chamber).subList(0, 50)
    }

    private fun shape(index: Int): (Int, Int) -> Set<Pair<Int, Int>> {
        return when (index) {
            0 -> { p,o -> setOf(p-4 to 2+o, p-4 to 3+o, p-4 to 4+o, p-4 to 5+o) }
            1 -> { p,o -> setOf(p-4 to 3+o, p-5 to 2+o, p-5 to 3+o, p-5 to 4+o, p-6 to 3+o) }
            2 -> { p,o -> setOf(p-4 to 2+o, p-4 to 3+o, p-4 to 4+o, p-5 to 4+o, p-6 to 4+o) }
            3 -> { p,o -> setOf(p-4 to 2+o, p-5 to 2+o, p-6 to 2+o, p-7 to 2+o) }
            4 -> { p,o -> setOf(p-4 to 2+o, p-4 to 3+o, p-5 to 2+o, p-5 to 3+o) }
            else -> throw IllegalStateException()
        }
    }

    data class Identifier(val shapeIndex: Int, val windIndex: Int)

    private fun solve(name: String, limit: Long = 1_000_000): Long {
        val wind = getWind(name)
        var chamber = createChamber() // rolling, new lines on top, drop at the end
        var shapeCount = 0L
        var shapeIndex = 0
        var windIndex = 0
        var highestRock = 0L
        var relativPositionOfHighestRock = 0
        val seen = HashMap<Identifier, Triple<Int, Long, Long>>()
        while (shapeCount < limit) {
            var leftright = 0
            var down = 0
            val createShapeWithOffset = shape(shapeIndex)
            var nextShape = createShapeWithOffset(relativPositionOfHighestRock+down, leftright)
            // check if there are enough lines. If not, roll chamber
            val minLine = nextShape.minOf { it.first }
            if (minLine < 0) {
                val abs = abs(minLine)
                chamber = rollChamber(abs, chamber)
                relativPositionOfHighestRock += abs
            }
            val id = Identifier(shapeIndex, windIndex)
            if (seen.contains(id)) {
                val (i, s, r) = seen[id]!!
                if (i > 1) {
                    val cycleLengthShapes = shapeCount-s
                    val cycleLengthRocks = highestRock-r
                    val fullCyclesToGo = (limit - shapeCount) / cycleLengthShapes
//                    println("Detected cycle:")
//                    println("  shapeIndex = $shapeIndex")
//                    println("  windIndex  = $windIndex")
//                    println("  Cycle length shapes = $cycleLengthShapes")
//                    println("  Cycle length highest rock = $cycleLengthRocks")
//                    println("  Shapes fallen = $shapeCount")
//                    println("  Height reached = $highestRock")
//                    println("  Full cycles to go = $fullCyclesToGo")
//                    println("  ${limit - shapeCount - cycleLengthShapes*fullCyclesToGo}")
                    // jump forward as far as possible
                    shapeCount += cycleLengthShapes*fullCyclesToGo
                    highestRock += cycleLengthRocks*fullCyclesToGo
                    seen.clear()
                } else {
                    seen[id] = Triple(i + 1, shapeCount, highestRock)
                }
            } else {
                seen[id] = Triple(1, shapeCount, highestRock)
            }
            while (true) {
                // move left/right
                val nextWind = wind[windIndex]
                if (windIndex == wind.size-1) {
                    windIndex = 0
                } else {
                    windIndex++
                }
                if (nextWind == '<') leftright-- else leftright++
                nextShape = createShapeWithOffset(relativPositionOfHighestRock+down, leftright)
                // if there are collisions, move back
                if (!nextShape.all { it.second >= 0 && chamber[it.first].size > it.second && chamber[it.first][it.second] == ' ' }) {
                    // move back
                    leftright += if (nextWind == '<') 1 else -1
                }
                // move down
                down++
                nextShape = createShapeWithOffset(relativPositionOfHighestRock+down, leftright)
                // if there are collisions, halt
                if (!nextShape.all { chamber[it.first][it.second] == ' ' }) {
                    // come to halt -> use shape before there were collisions
                    val finalShape = createShapeWithOffset(relativPositionOfHighestRock+down-1, leftright)
                    finalShape.forEach { chamber[it.first][it.second] = '#' }
                    val newHighestRockRelative = finalShape.minOf { it.first }
                    if (newHighestRockRelative < relativPositionOfHighestRock) {
                        highestRock += (relativPositionOfHighestRock - newHighestRockRelative)
                        relativPositionOfHighestRock = newHighestRockRelative
                    }
                    break
                }
            }
            shapeCount++
            // prepare next loop
            if (shapeIndex == 4) {
                shapeIndex = 0
            } else {
                shapeIndex++
            }
        }
        return highestRock
    }

    override fun executePart1(name: String): Long {
        return solve(name, 2022)
    }

    override fun executePart2(name: String): Long {
        val m = 1_000_000_000_000
        return solve(name, m)
    }
}