package aoc2020

private const val INPUT = "1,0,16,5,17,4"

private fun execute(limit: Int): Int {
    val input = INPUT.split(",").map { it.toInt() }
    val index = IntArray(30_000_000) { -1 }
    for (i in 0 until input.size - 1) {
        index[input[i]] = i
    }
    var nextNumber = input.last()
    var counter = input.size - 1;
    while (counter < limit - 1) {
        if (index[nextNumber] != -1) {
            val old = nextNumber
            nextNumber = counter - index[nextNumber]
            index[old] = counter
        } else {
            index[nextNumber] = counter
            nextNumber = 0
        }
        counter++
    }

    return nextNumber
}

fun executeDay15Part1(): Int {
    return execute(2020)
}

fun executeDay15Part2(): Int {
    return execute(30000000)
}

