fun readInput(name: String): List<List<Char>> {
    return getResourceAsList(name)
        .filter { x -> x.isNotBlank() }
        .map { it.asIterable().toList() }

}

fun calculateSlope(path: String, x: Int, y: Int): Int {
    val map = readInput(path)
    var steps = map.count() / y
    var treeCount = 0
    for (step in 0 until steps) {
        val xx = (step*x) % map[0].count()
        val field = map[step*y][xx]
        if ('#' == field) {
            treeCount += 1
        }
    }
    return treeCount
}

fun executeDay3Part1(): Int {
    return calculateSlope("day3.txt", 3, 1)
}

fun executeDay3Part2(): Int {
    val a = calculateSlope("day3.txt", 1, 1)
    val b = calculateSlope("day3.txt", 3, 1)
    val c = calculateSlope("day3.txt", 5, 1)
    val d = calculateSlope("day3.txt", 7, 1)
    val e = calculateSlope("day3.txt", 1, 2)
    return a * b * c * d * e
}