class Edge(val from: String, val to: String, val value: Int) {
    @Override
    override fun toString(): String {
        return "$from -> $to ($value)"
    }
}

fun parseLine(line: String): List<Edge> {
    val x = line.split("bags contain")
    val source = x[0].trim()
    if (line.contains("no other bags")) {
        return listOf()
    } else {
        return x[1]
            .trim()
            .split(",")
            .map { it.trim() }
            .map { it.removeSuffix(".") }
            .map { it.split(" ") }
            .map { Edge(source, it.subList(1, it.count() - 1).joinToString(separator = " "), it[0].toInt()) }
    }
}

fun executeDay7Part1(): Int {
    val edges = getResourceAsList("day7.txt")
        .filter { it.isNotBlank() }
        .map { parseLine(it) }
        .reduceRight { a, b -> a + b}
    val res = mutableSetOf<String>()
    var queue = mutableSetOf("shiny gold")
    while (!queue.isEmpty()) {
        res.addAll(queue)
        queue = edges
            .filter { queue.contains(it.to) }
            .map { it.from }
            .toMutableSet()
    }
    return res.toSet().count() - 1
}


fun getPath(edge: Edge, edges: List<Edge>): Int {
    val nextEdges = edges
        .filter { it.from == edge.to }
    return if (nextEdges.isEmpty()) {
        edge.value
    } else {
        edge.value
        + nextEdges
            .map { getPath(it, edges) * edge.value }
            .reduceRight { a, b -> a + b}
    }
}

fun executeDay7Part2(): Int {
    val edges = getResourceAsList("day7.txt")
        .filter { it.isNotBlank() }
        .map { parseLine(it) }
        .reduceRight { a, b -> a + b}
    return getPath(Edge("nil", "shiny gold", 1), edges) - 1
}