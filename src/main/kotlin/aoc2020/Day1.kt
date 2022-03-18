fun executeDay1Part1(): Int {
    val coll = getResourceAsList("day1.txt")
        .filter { x -> x.isNotBlank() }
        .map { x -> Integer.parseInt(x) }
        .sortedWith(compareBy {it})

    for (i in coll.indices) {
        for (j in coll.size-1 downTo i+1) {
            val x = coll[i]
            val y = coll[j]
            if (x + y == 2020) {
                return x * y
            } else if (x + y < 2020) {
                break
            }
        }
    }
    return -1
}

fun executeDay1Part2(): Int {
    val coll = getResourceAsList("day1.txt")
        .filter { x -> x.isNotBlank() }
        .map { x -> Integer.parseInt(x) }
        .sortedWith(compareBy {it})

    for (i in coll.indices) {
        for (j in coll.size-1 downTo i+1) {
            for (k in i+1 until j-1) {
                val x = coll[i]
                val y = coll[j]
                val z = coll[k]
                if (x + y + z == 2020) {
                    return x * y * z
                }
            }
        }
    }
    return -1
}