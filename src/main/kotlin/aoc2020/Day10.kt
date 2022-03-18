import java.lang.Integer.min
import kotlin.math.pow

private fun parseInput(path: String): List<Int> {
    return getResourceAsList(path)
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .sorted()
}

fun executeDay10Part1(): Int {
    val list = parseInput("day10.txt")
    var ones = 1
    var threes = 1
    for (i in 0 until list.size -1) {
        if (list[i+1] - list[i] == 1) {
            ones += 1
        } else {
            threes += 1
        }
    }
    return ones * threes
}

fun getCombis(s: Int): Int {
    return when (s) {
        3 -> 2
        4 -> 4
        5 -> 7
        else -> 0
    }
}

private fun addMinAndMax(l: List<Int>): List<Int> {
    val coll = l.toMutableList()
    coll.add(0)
    coll.add(l.last() + 3)
    return coll.sorted()
}

fun executeDay10Part2(): Long {
    val list = addMinAndMax(parseInput("day10.txt"))
    val pairs = list.windowed(size = 2)
        .map { it[0] to it[1] }

    val res = mutableListOf<List<Int>>()
    var acc = mutableSetOf<Int>()
    for (pair in pairs) {
        if (pair.second - pair.first == 3) {
            res.add(acc.toList().sorted())
            acc = mutableSetOf()
        } else {
            acc = acc.union(setOf(pair.first, pair.second)).toMutableSet()
        }
    }
    return res
        .asSequence()
        .filter { it.isNotEmpty() }
        .filter { it.size > 2 }
        .groupBy { it.size }
        .map { it.key to getCombis(it.key).toDouble().pow(it.value.size.toDouble()) }
        .map { it.second.toLong() }
        .toList()
        .reduceRight { a, b -> a * b}
}

// 3 --> 2
// 4 --> 4
// 5 --> 7 5,4,4,4,3,3,3 12345 1235 1245 1345 125 135 145