package aoc2020

import getResourceAsList
import java.util.Comparator
import java.util.SortedMap

data class HexTile(val path: SortedMap<String, Int>) {
    fun transformPath(): HexTile {
        return HexTile(transformMap(path))
    }

    override fun toString(): String {
        return if (path.isEmpty()) {
            "~"
        } else {
            path
                .map { "${it.key}${it.value}" }
                .joinToString("")
        }
    }
}

private fun parseLine(line: String): List<String> {
    var i = 0
    val res = mutableListOf<String>()
    while (i < line.length) {
        var currentChar = line[i]
        var nextChar = if (i < line.length - 1) line[i + 1] else null
        if (currentChar == 'n' || currentChar == 's') {
            res.add(currentChar.toString() + nextChar.toString())
            i++
        } else {
            res.add(currentChar.toString())
        }
        i++
    }
    assert(line == res.joinToString(""))
    return res.toList()
}

private fun MutableMap<String, Int>.replace(a: String, b: String, with: String): MutableMap<String, Int> {
    if (containsKey(a) && containsKey(b)) {
        val min = minOf(this[a]!!, this[b]!!)
        this.putIfAbsent(with, 0)
        this[with] = this[with]!! + min
        this[a] = this[a]!! - min
        this[b] = this[b]!! - min
    }
    return this
}

private fun MutableMap<String, Int>.replace(path: List<String>, with: List<String>): MutableMap<String, Int> {
    if (path.all { containsKey(it) }) {
        val min = path.minOf { this[it]!! }
        with.forEach { this.putIfAbsent(it, 0) }
        with.forEach { this[it] = this[it]!! + min  }
        path.forEach { this[it] = this[it]!! - min }
    }
    return this
}

private fun MutableMap<String, Int>.absorb( a: String, b: String): MutableMap<String, Int> {
    if (containsKey(a) && containsKey(b)) {
        val minSideways = minOf(this[a]!!, this[b]!!)
        this[a] = this[a]!! - minSideways
        this[b] = this[b]!! - minSideways
    }
    return this
}

private fun MutableMap<String, Int>.increase( a: String): MutableMap<String, Int> {
    if (containsKey(a)) {
        this[a] = this[a]!! + 1
    } else {
        this[a] = 1
    }
    return this
}

private fun transformMap(line: Map<String, Int>): SortedMap<String, Int> {
    return line.toMutableMap()
        .replace("nw", "e", "ne")
        .replace("ne", "w", "nw")

        .replace("ne", "se", "e")
        .replace("e", "nw", "ne")

        .replace("e", "sw", "se")
        .replace("se", "ne", "e")

        .replace("se", "w", "sw")
        .replace("sw", "e", "se")

        .replace("sw", "nw", "w")
        .replace("w", "se","sw")

        .replace("w", "ne", "nw")
        .replace("nw", "sw", "w")

        .absorb("w", "e")
        .absorb("nw", "se")
        .absorb("ne", "sw")
        .filter { it.value != 0 }
        .toSortedMap(Comparator.comparing {it})
}

fun executeDay24Part1(input: Collection<String> = getResourceAsList("day24.txt")): Int {
    return input
        .asSequence()
        .filter { it.isNotBlank() }
        .map<String, List<String>> { parseLine(it) }
        .map { x -> x.groupBy { it }.mapValues { it.value.count() } }
        .map { m -> HexTile(m.toSortedMap(Comparator.comparing { it })) }
        .map { it.transformPath() }
        .map { it.transformPath() }
        .groupBy { it }
        .mapValues { it.value.count() }
        .filter { it.value % 2 == 1 }
        .count()
}

fun executeDay24Part2(input: Collection<String> = getResourceAsList("day24.txt")) {
    val initialState =  input
        .asSequence()
        .filter { it.isNotBlank() }
        .map<String, List<String>> { parseLine(it) }
        .map { x -> x.groupBy { it }.mapValues { it.value.count() } }
        .map { m -> HexTile(m.toSortedMap(Comparator.comparing { it })) }
        .map { it.transformPath() }
        .map { it.transformPath() }
        .groupBy { it }
        .mapValues { it.value.count() }
        .filter { it.value % 2 == 1 }
        .map { it.key }
        .toSet()
}




fun getNeighbours(tile: HexTile): Set<HexTile> {
    return listOf("w", "nw", "ne", "e", "se", "sw")
        .map { direction -> tile.path.toMutableMap().increase(direction).toSortedMap(Comparator.comparing {it}) }
        .map { HexTile(it) }
        .map { it.transformPath() }
        .map { it.transformPath() }
        .toSet()
}