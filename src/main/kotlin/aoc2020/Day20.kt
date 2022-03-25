package aoc2020

import getResourceAsList
import splitBy
import java.lang.IllegalStateException

private data class Tile(val id: Int, val lines: List<IntArray>) {

    override fun toString(): String {
        return lines.joinToString("\n") { it.toList().joinToString("") { x -> x.toString() } }
    }

    val leftBorder = lines.map { it.first() }.toIntArray()
    val rightBorder = lines.map { it.last() }.toIntArray()
    val upperBorder = lines.first()
    val lowerBorder = lines.last()

    val originalBorders = setOf(
        leftBorder, rightBorder, upperBorder, lowerBorder
    )

    val allBorders = originalBorders
        .flatMap { setOf(it, it.reversedArray()) }
        .toSet()



    fun rotateClockwise(): Tile {
        val newLines = mutableListOf<IntArray>()
        for (i in lines.indices) {
            val row = IntArray(lines.size) { lines[it][i]}.reversedArray()
            newLines.add(row)
        }
        return Tile(id, newLines.toList())
    }

    fun rotate180(): Tile {
        return Tile(id, lines.map { it.reversedArray() }.reversed())
    }

    fun rotateCounterClockwise(): Tile {
        val newLines = mutableListOf<IntArray>()
        for (i in lines.size - 1 downTo 0) {
            val row = IntArray(lines.size) { lines[it][i]}
            newLines.add(row)
        }
        return Tile(id, newLines.toList())
    }

    fun flipLeftToRight(): Tile {
        return Tile(id, lines.map { it.reversedArray() })

    }

    fun flipUpToDown(): Tile {
        return Tile(id, lines.reversed())
    }

    fun direction(border: IntArray): String? {
        return when {
            border.contentEquals(leftBorder) -> "L"
            border.contentEquals(rightBorder) -> "R"
            border.contentEquals(upperBorder) -> "U"
            border.contentEquals(lowerBorder) -> "D"
            else -> null
        }
    }

    fun matchingBorder(otherTile: Tile): IntArray? {
        for (border1 in this.originalBorders) {
            for (border2 in otherTile.allBorders) {
                if (border1.contentEquals(border2)) {
                    return border1
                }
            }
        }
        return null
    }

    fun directionOfNeighbour(otherTile: Tile): String? {
        val border = matchingBorder(otherTile)
        return border?.let { direction(it) }
    }

    fun matchingOrientationToLeftBorder(shouldBeLeftBorder: IntArray): Tile {
        when {
            shouldBeLeftBorder.contentEquals(leftBorder) ->
                return this
            shouldBeLeftBorder.contentEquals(rightBorder) ->
                return this.flipLeftToRight()
            shouldBeLeftBorder.contentEquals(upperBorder) ->
                return this.rotateCounterClockwise().flipUpToDown()
            shouldBeLeftBorder.contentEquals(lowerBorder) ->
                return this.rotateClockwise()
            shouldBeLeftBorder.contentEquals(leftBorder.reversedArray()) ->
                return this.flipUpToDown()
            shouldBeLeftBorder.contentEquals(rightBorder.reversedArray()) ->
                return this.rotate180()
            shouldBeLeftBorder.contentEquals(upperBorder.reversedArray()) ->
                return this.rotateCounterClockwise()
            shouldBeLeftBorder.contentEquals(lowerBorder.reversedArray()) ->
                return this.rotateClockwise().flipUpToDown()
            else -> throw IllegalStateException()
        }
    }

    fun matchingOrientationToUpperBorder(shouldBeUpperBorder: IntArray): Tile {
        return when {
            shouldBeUpperBorder.contentEquals(leftBorder) -> this.rotateClockwise().flipLeftToRight()
            shouldBeUpperBorder.contentEquals(rightBorder) -> this.rotateCounterClockwise()
            shouldBeUpperBorder.contentEquals(upperBorder) -> this
            shouldBeUpperBorder.contentEquals(lowerBorder) -> this.flipUpToDown()
            shouldBeUpperBorder.contentEquals(leftBorder.reversedArray()) -> this.rotateClockwise()
            shouldBeUpperBorder.contentEquals(rightBorder.reversedArray()) -> this.rotateCounterClockwise().flipLeftToRight()
            shouldBeUpperBorder.contentEquals(upperBorder.reversedArray()) -> this.flipLeftToRight()
            shouldBeUpperBorder.contentEquals(lowerBorder.reversedArray()) -> this.rotate180()
            else -> throw IllegalStateException()
        }
    }


}

private fun toTile(tile: List<String>): Tile {
    val name = tile[0].split(" ")[1].dropLast(1).toInt()
    val lines = tile.subList(1, tile.size)
        .map { it.map { c -> if (c == '#') 1 else 0 } }
        .map { it.toIntArray() }
    return Tile(name, lines)
}

private fun parseInput(path: String = "day20.txt"): List<Tile> {
    return getResourceAsList(path)
        .splitBy { it.isBlank() }
        .filter { it.isNotEmpty() }
        .filter { it.all { x -> x.isNotEmpty() } }
        .map { toTile(it) }
}

private fun buildMap(tiles: Map<Int, Tile>): Map<Int, Set<Tile>> {
    val map = tiles.mapValues { mutableSetOf<Tile>() }
    for ((id, tile) in tiles) {
        for ((_, otherTile) in tiles) {
            if (map[id]!!.size >= 4) {
                continue
            }
            if (tile == otherTile) {
                continue
            }
            val mb = tile.matchingBorder(otherTile)
            if (mb != null) {
                map[id]!!.add(otherTile)
            }
        }
    }
    return map.toMap()
}

fun executeDay20Part1(): Long {
    val tiles = parseInput().associateBy { it.id }
    return buildMap(tiles)
        .filter { it.value.size == 2 }
        .map { it.key.toLong() }
        .reduceRight { a, b -> a * b }
}

fun IntArray.str(): String {
    return this.joinToString("") { it.toString() }
}

private fun getNeighbourMap(tile: Tile, map: Map<Int, Set<Tile>>): Map<String, Tile> {
    return map[tile.id]!!.associateBy { tile.directionOfNeighbour(it)!! }
}

private fun findStartTile(tiles: Map<Int, Tile>, map: Map<Int, Set<Tile>>): Tile? {
    val cornerTiles = map
        .filter { it.value.size == 2 }
    for ((id, neighbours) in cornerTiles) {
        val tile = tiles[id]!!
        val neighbourMap = neighbours.associateBy { tile.directionOfNeighbour(it)!! }
        if (neighbourMap.keys.sortedBy { it }.joinToString("") == "RU") {
           return tile.flipUpToDown()
        }
    }
   return null
}

private fun getLines(): List<String> {
    val tiles = parseInput()
        .associateBy { it.id }
    val map = buildMap(tiles)
    var start = findStartTile(tiles, map)!!
    var left = start
    var n = getNeighbourMap(start, map)
    var m = n.toMap()
    val result = mutableListOf<MutableList<Tile>>()
    for (i in 0 until 12) {
        result.add(mutableListOf())
    }
    for (row in 0 until 12) {
        for (col in 0 until 12) {
            result[row].add(start)
            if (n["R"] != null) {
                start = n["R"]!!.matchingOrientationToLeftBorder(start.rightBorder)
                n = getNeighbourMap(start, map)
            }
        }
        if (m["D"] == null) {
            break
        }
        start = m["D"]!!.matchingOrientationToUpperBorder(left.lowerBorder)
        n = getNeighbourMap(start, map)
        left = start
        m = n.toMap()
    }
    val strBuilder = StringBuilder()
    for (row in 0 until 12) {
        for (i in 1 until 9) {
            for (col in 0 until 12) {
                strBuilder.append(result[row][col].lines[i].drop(1).dropLast(1).joinToString("") {it.toString()})
            }
            strBuilder.append("\n")
        }
    }
    return strBuilder.toString().trim().split("\n")
}

private const val FIRST_LINE : String =  "..................1."
private const val SECOND_LINE : String = "1....11....11....111"
private const val THIRD_LINE : String =  ".1..1..1..1..1..1..."

private fun allMatches(regex: Regex, line: CharSequence): List<Int> {
    return line.indices
        .mapNotNull { regex.find(line, it) }
        .map { it.range.first }
        .distinct()
}
private fun findLinesContainingSeamonsterTop(lines: List<String>): List<Pair<Int, Int>> {
    val firstLineMatches = lines
        .indices
        .flatMap { allMatches(FIRST_LINE.toRegex(), lines[it]).map { x -> it to x } }
    val secondLineMatches = lines
        .indices
        .flatMap { allMatches(SECOND_LINE.toRegex(), lines[it]).map { x -> it to x } }
    val thirdLineMatches = lines
        .indices
        .flatMap { allMatches(THIRD_LINE.toRegex(), lines[it]).map { x -> it to x } }
    val res = mutableListOf<Pair<Int, Int>>()
    for (p in firstLineMatches) {
        if (secondLineMatches.contains(p.first + 1 to p.second) && thirdLineMatches.contains(p.first + 2 to p.second)) {
            res.add(p)
        }
    }
    return res.toList()
}

fun executeDay20Part2(): Int {
    val tile = Tile(0, getLines().map { it.map { s -> s.toString().toInt() }.toIntArray() }).rotate180()
    val lines = tile.lines.map { it.joinToString("") { s -> s.toString() } }
    val allHashes = lines.joinToString("").count { it == '1' }
    return allHashes - findLinesContainingSeamonsterTop(lines).count() * 15
}




