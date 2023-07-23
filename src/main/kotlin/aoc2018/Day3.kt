package aoc2018

import getInputAsLines
import util.Day

class Day3: Day("3") {

    private data class Claim(val id: Int, val upperLeftCorner: Pair<Int, Int>, val width: Int, val height: Int)

    private fun parseClaim(line: String): Claim {
        val (id,_,c,d) = line.split(" ")
        val (x,y) = c.dropLast(1).split(",").map { it.toInt() }
        val (width, height) = d.split("x").map { it.toInt() }
        return Claim(id.drop(1).toInt(), x to y, width, height)
    }

    private fun getInput(name: String): List<Claim> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { parseClaim(it) }
    }


    override fun executePart1(name: String): Any {
        val claims = getInput(name)
        val all = mutableSetOf<Pair<Int, Int>>()
        val double = mutableSetOf<Pair<Int, Int>>()
        for (claim in claims) {
            for (x in claim.upperLeftCorner.first until claim.upperLeftCorner.first+claim.width) {
                for (y in claim.upperLeftCorner.second until claim.upperLeftCorner.second+claim.height) {
                    val p = x to y
                    if (all.contains(p)) {
                        if (!double.contains(p)) {
                            double.add(p)
                        }
                    } else {
                        all.add(p)
                    }
                }
            }
        }
        return double.size
    }


    override fun executePart2(name: String): Any {
        var claims = getInput(name)

        val detectedDouble = claims.associateWith { false }.toMutableMap()

        for (i in 0 until 2) {
            val all = mutableSetOf<Pair<Int, Int>>()
            val double = mutableSetOf<Pair<Int, Int>>()
            for (claim in claims) {
                for (x in claim.upperLeftCorner.first until claim.upperLeftCorner.first + claim.width) {
                    for (y in claim.upperLeftCorner.second until claim.upperLeftCorner.second + claim.height) {
                        val p = x to y
                        if (all.contains(p)) {
                            detectedDouble[claim] = true
                            if (!double.contains(p)) {
                                double.add(p)
                            }
                        } else {
                            all.add(p)
                        }
                    }
                }
            }
            claims = claims.sortedBy { detectedDouble[it] }.reversed()
        }

        return detectedDouble.filter { (_,v) -> !v }.keys.first().id
    }
}