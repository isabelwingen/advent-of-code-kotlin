fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

fun getResourceAsList(path: String): Collection<String> =
    getResourceAsText(path)!!
        .split("\n")

fun <T,S> splitAt(coll: Collection<T>, splitFun: (x: T) -> S): List<List<T>> {
    val res = mutableListOf<MutableList<T>>()
    val list = coll.toList()

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
