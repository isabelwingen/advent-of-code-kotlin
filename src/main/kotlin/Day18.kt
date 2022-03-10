private fun parseInput(): List<List<Pair<Int, Any>>> {
    return getResourceAsList("day18.txt")
        .filter { it.isNotBlank() }
        .map { expressionToColl(it) }
        .map { parse(it) }
}

private fun expressionToColl(expression: String): List<String> {
    return expression
        .replace("(", " ( ")
        .replace(")", " ) ")
        .split(" ")
        .filter { it.isNotBlank() }
}

enum class Op {
    ADD, MUL
}

private fun parse(expression: List<String>): List<Pair<Int, Any>> {
    val iter = expression.iterator()
    val res = mutableListOf<Pair<Int, Any>>()
    var depth = 0
    while (iter.hasNext()) {
        when (val x = iter.next()) {
            "(" -> depth++
            ")" -> depth--
            "+" -> res.add(depth to Op.ADD)
            "*" -> res.add(depth to Op.MUL)
            else -> res.add(depth to x.toLong())
        }
    }
    return res.toList()
}

private fun solve(expression: List<Pair<Int, Any>>): Long {
    if (expression.map { it.first }.distinct().size == 1) {
        var res = 0L
        val iter = expression.map { it.second }.iterator()
        if (iter.hasNext()) {
            res += iter.next().toString().toLong()
        }
        while (iter.hasNext()) {
            val x = iter.next();
            if (x is Op) {
                if (x == Op.ADD) {
                    res += iter.next().toString().toLong()
                } else {
                    res *= iter.next().toString().toLong()
                }
            }
        }
        return res
    } else {
        val highestDepth = expression.maxOfOrNull { it.first }
        val x = splitAt(expression) { it.first }
        return solve(x.map { if(it.first().first == highestDepth) listOf((highestDepth - 1) to solve(it)) else it }.flatten())
    }
}

fun executeDay18Part1(): Long {
    return parseInput().sumOf { solve(it) }
}

private fun solve2(expression: List<Pair<Int, Any>>): Long {
    if (expression.map { it.first }.distinct().size == 1) {
        var res = 0L
        val iter = expression.map { it.second }.iterator()
        if (iter.hasNext()) {
            res += iter.next().toString().toLong()
        }
        while (iter.hasNext()) {
            val x = iter.next();
            if (x is Op) {
                if (x == Op.ADD) {
                    res += iter.next().toString().toLong()
                } else {
                    res *= iter.next().toString().toLong()
                }
            }
        }
        return res
    } else {
        val highestDepth = expression.maxOfOrNull { it.first }
        val x = splitAt(expression) { it.first }
        return solve(x.map { if(it.first().first == highestDepth) listOf((highestDepth - 1) to solve(it)) else it }.flatten())
    }
}

private fun prepareExpression(expression: List<Pair<Int, Any>>) {
    if (expression.map { it.first }.distinct().size == 1) {
        var res = 0L
        var store = 0L
        var iter = expression.map { it.second }.iterator()
        if (iter.hasNext()) {
            var x = iter.next().toString().toLong()
            res = x
            store = x
        }
        while (iter.hasNext()) {

        }
    } else {

    }
}

fun executeDay18Part2(): List<Pair<Int, Any>> {
    return listOf("(1 + 2) * (3 + 4) * 5 + 6")
        .map { expressionToColl(it) }
        .map { parse(it) }
        .first()
}