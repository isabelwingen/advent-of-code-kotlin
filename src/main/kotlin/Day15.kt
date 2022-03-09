fun executeDay15Part1(input: List<Int>): Int {
    val index = mutableMapOf<Int, Int>()
    for (i in 0 until input.size - 1) {
        index[input[i]] = i
    }
    var nextNumber = input.last()
    var counter = input.size - 1;
    while (counter < 30000000 - 1) {
        if (index.containsKey(nextNumber)) {
            val old = nextNumber
            nextNumber = counter - index[old]!!
            index[old] = counter
        } else {
            index[nextNumber] = counter
            nextNumber = 0
        }
        counter++
    }

    return nextNumber
}