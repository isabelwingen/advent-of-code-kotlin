private fun parseInput(path: String): List<List<String>> {
    val coll = mutableListOf<List<String>>()
    var currentList = mutableListOf<String>()
    for (line in getResourceAsList(path)) {
        if (line.isBlank()) {
            coll.add(currentList.toList())
            currentList = mutableListOf()
        } else {
            currentList.add(line)
        }
    }
    return coll.toList()
}

fun executeDay6Part1(): Int {
    val coll = mutableListOf<Set<Char>>()
    var currentList = mutableSetOf<Char>()
    for (line in getResourceAsList("day6.txt")) {
        if (line.isBlank()) {
            coll.add(currentList.toSet())
            currentList = mutableSetOf()
        } else {
            line.forEach { currentList.add(it) }
        }
    }
    return coll
        .map { it.count() }
        .reduceRight { a, b -> a + b }
}

fun intersectGroup(lines: List<String>): Set<Char> {
    return lines
        .map { it.toSet() }
        .reduceRight { a, b -> a.intersect(b)}
}

fun executeDay6Part2(): Int {
    val coll = mutableListOf<Set<Char>>()
    var currentList = mutableListOf<String>()
    for (line in getResourceAsList("day6.txt")) {
        if (line.isBlank()) {
            coll.add(intersectGroup(currentList.toList()))
            currentList = mutableListOf()
        } else {
            currentList.add(line)
        }
    }
    return coll
        .map { it.count() }
        .reduceRight { a, b -> a + b }
}