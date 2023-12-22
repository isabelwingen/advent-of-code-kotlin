import kotlin.math.round

fun Char.asLong(): Long =
    this.toString().toLong()

fun <T,S> Collection<T>.splitBy(splitFun: (x: T) -> S): List<List<T>> {
    val res = mutableListOf<MutableList<T>>()
    val list = this.toList()

    var i = 0
    var lastVal = splitFun(list[0])
    res.add(mutableListOf(list[0]))
    for (c in list.subList(1, list.size)) {
        val nextVal = splitFun(c)
        if (nextVal == lastVal) {
            res[i].add(c)
        } else {
            i++
            res.add(mutableListOf(c))
        }
        lastVal = nextVal
    }
    return res.map { it.toList() }
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun <E> List<List<E>>.getNeighbouringIndices(position: Pair<Int, Int>): List<Pair<Int, Int>> {
    val (x,y) = position
    val neighbours = mutableListOf<Pair<Int, Int>>()
    if (x > 0) {
        neighbours.add((x-1) to y)
    }
    if (x < this.size - 1) {
        neighbours.add((x+1) to y)
    }
    if (y > 0) {
        neighbours.add(x to (y-1))
    }
    if (y < this[0].size - 1) {
        neighbours.add(x to (y+1))
    }
    return neighbours.toList()
}

fun lcm(a: Long, b: Long): Long {
    var gcd = 1L

    var i = 1L
    while (i <= a && i <= b) {
        if (a % i == 0L && b % i == 0L)
            gcd = i
        ++i
    }
    return (a * b) / gcd
}