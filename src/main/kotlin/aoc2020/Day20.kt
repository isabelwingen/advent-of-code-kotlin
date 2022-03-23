package aoc2020

import getResourceAsList
import splitBy
import java.util.ArrayDeque

data class Tile(val id: Int, val lines: List<CharSequence>) {
    override fun toString(): String {
        return "$id: $lines"
    }

    fun leftBorder(): String = lines.map { it.first() }.toString()

    fun rightBorder(): String = lines.map { it.last() }.toString()

    fun upperBorder(): String = lines[0].toString()

    fun lowerBorder(): String = lines.last().toString()

    private fun flipHorizontal(): Tile {
        return Tile(id, lines.map { it.reversed() })
    }

    fun flipVertical(): Tile {
        return Tile(id, lines.reversed())
    }

    private fun rotateRight(): Tile {
        val coll = mutableListOf<MutableList<Char>>()
        for (j in lines[0].indices) {
            coll.add(mutableListOf())
        }
        for (i in lines.indices) {
            for (j in lines[0].indices) {
                coll[j].add(lines[i][j])
            }
        }
        return Tile(id, coll.map { it.joinToString("") })
    }

    private fun rotateTwice(): Tile {
        val coll = lines.map { it.reversed() }.reversed()
        return Tile(id, coll)
    }

    private fun rotateLeft(): Tile {
        val coll = rotateRight().lines.map { it.reversed() }.reversed()
        return Tile(id, coll)
    }

    fun allOrientations(): List<Tile> {
        return listOf(
            this,
            flipHorizontal(),
            flipVertical(),
            rotateRight(),
            rotateTwice(),
            rotateLeft(),
            rotateRight().flipHorizontal(),
            rotateRight().flipVertical()
        )
    }
}

private fun parseInput11(path: String = "day20.txt"): List<Tile> {
    return getResourceAsList(path)
        .splitBy { it.isBlank() }
        .filter { it.isNotEmpty() }
        .filter { it.all { x -> x.isNotEmpty() } }
        .map { listOf(it[0].split(" ")[1].dropLast(1)) + it.subList(1, it.size) }
        .map { Tile(it[0].toInt(), it.subList(1, it.size)) }
}

fun Tile.match(otherTile: Tile): Boolean {
    val otherRotations = otherTile.allOrientations();
    for (s in otherRotations) {
        when {
            this.rightBorder() == s.leftBorder() -> return true
            this.leftBorder() == s.rightBorder() -> return true
            this.upperBorder() == s.lowerBorder() -> return true
            this.lowerBorder() == s.upperBorder() -> return true
        }
    }
    return false
}

private fun getMatches(tiles: List<Tile>, tile: Tile): Int {
    return tiles.filter { it != tile }.count { tile.match(it) }
}

fun executeDay20Part1(): Long {
    val x = parseInput11()
    return x
        .map { it.id to getMatches(x, it) }
        .filter { it.second == 2 }
        .map { it.first }
        .map { it.toLong() }
        .reduceRight { a, b -> a * b}
}

enum class Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN
}

fun Tile.getNeighbours(tiles: List<Tile>): Map<Direction, Tile> {
    val tilesToCheck = tiles.filter { it.id != this.id}
    val neighbours = mutableMapOf<Direction, Tile>()
    for (otherTile in tilesToCheck) {
        for (r in otherTile.allOrientations()) {
            if (this.leftBorder() == r.rightBorder()) {
                neighbours[Direction.LEFT] = r
            }
        }
    }
    for (otherTile in tilesToCheck) {
        for (r in otherTile.allOrientations()) {
            if (this.rightBorder() == r.leftBorder()) {
                neighbours[Direction.RIGHT] = r
            }
        }
    }
    for (otherTile in tilesToCheck) {
        for (r in otherTile.allOrientations()) {
            if (this.upperBorder() == r.lowerBorder()) {
                neighbours[Direction.UP] = r
            }
        }
    }
    for (otherTile in tilesToCheck) {
        for (r in otherTile.allOrientations()) {
            if (this.lowerBorder() == r.upperBorder()) {
                neighbours[Direction.DOWN] = r
            }
        }
    }
    return neighbours.toMap()
}

fun getUpperLeftCorner(tiles: List<Tile>): Tile {
    return tiles
        .asSequence()
        .map { it.id to getMatches(tiles, it) }
        .filter { it.second == 2 }
        .map { it.first }
        .map { tiles.associateBy { t -> t.id }[it]!! }
            // we need RIGHT/DOWN, but only RIGHT/UP is present, so we flip vertical
        .map { it.flipVertical() }
        .first { it.getNeighbours(tiles).keys == setOf(Direction.RIGHT, Direction.DOWN) }
}

fun createPuzzle(name: String = "day20.txt"): Tile {
    val tiles = parseInput11(name)
    val upperLeftCorner = getUpperLeftCorner(tiles)
    val stack = ArrayDeque<Tile>()
    stack.push(upperLeftCorner)
    val puzzleOrder = mutableListOf<Tile>()
    while (stack.isNotEmpty()) {
        val current = stack.pop()
        puzzleOrder.add(current)
        val neighbours = current.getNeighbours(tiles)
        // left border
        if (!neighbours.containsKey(Direction.LEFT)) {
            // there is another row below
            if (neighbours.containsKey(Direction.DOWN)) {
                stack.push(neighbours[Direction.DOWN]!!)
            }
        }
        if (neighbours.containsKey(Direction.RIGHT)) {
            stack.push(neighbours[Direction.RIGHT]!!)
        }
    }
    val chunkSize = Math.sqrt(tiles.size.toDouble()).toInt()
    val puzzleLines = puzzleOrder
        .map { it.lines }
        .map { it.dropLast(1).drop(1).map { t -> t.drop(1).dropLast(1) } }
        .mapIndexed { index, charSequences -> Tile(index, charSequences) }
        .chunked(chunkSize)
        .flatMap { line ->
            val bla = mutableListOf<String>()
            for (i in line[0].lines.indices) {
                bla.add(line.joinToString("") { it.lines[i].toString() })
            }
            bla.toList()
        }
    return Tile(0, puzzleLines)
}

private fun allMatches(regex: Regex, line: CharSequence): List<Int> {
    return line.indices
        .mapNotNull { regex.find(line, it) }
        .map { it.range.first }
        .distinct()
}

const val FIRST_LINE : String =  "..................#."
const val SECOND_LINE : String = "#....##....##....###"
const val THIRD_LINE : String =  ".#..#..#..#..#..#..."

private fun findLinesContainingSeamonsterTop(tile: Tile): List<Pair<Int, Int>> {
    val firstLineMatches = tile.lines
        .indices
        .flatMap { allMatches(FIRST_LINE.toRegex(), tile.lines[it]).map { x -> it to x } }
    val secondLineMatches = tile.lines
        .indices
        .flatMap { allMatches(SECOND_LINE.toRegex(), tile.lines[it]).map { x -> it to x } }
    val thirdLineMatches = tile.lines
        .indices
        .flatMap { allMatches(THIRD_LINE.toRegex(), tile.lines[it]).map { x -> it to x } }
    val res = mutableListOf<Pair<Int, Int>>()
    for (p in firstLineMatches) {
        if (secondLineMatches.contains(p.first + 1 to p.second) && thirdLineMatches.contains(p.first + 2 to p.second)) {
            res.add(p)
        }
    }
    return res.toList()
}

fun executeDay20Part2(name: String = "day20.txt"): Int {
    val puzzle = createPuzzle(name)
    val allHashes = puzzle.lines.joinToString("").count { it == '#' }
    val seamonsterFields = puzzle.allOrientations()
        .map { findLinesContainingSeamonsterTop(it) }
        .first { it.isNotEmpty() }
        .count()
    return allHashes - 15 * seamonsterFields
}