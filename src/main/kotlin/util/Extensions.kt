import kotlin.math.round

fun Char.asLong(): Long =
    this.toString().toLong()

fun Char.isDigit(): Boolean =
    setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9').contains(this)

fun Iterable<Long>.product(): Long =
    this.reduce { a, b -> a * b }

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