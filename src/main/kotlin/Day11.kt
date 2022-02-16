
enum class Seat(val seat: String) {
    EMPTY("L"),
    FLOOR("."),
    OCCUPIED("#");

    override fun toString(): String {
      return seat
    }
}

private fun transformLine(line: String): List<Seat> {
    return line
        .map { when (it) {
            'L' -> Seat.EMPTY
            '#' -> Seat.OCCUPIED
            else -> Seat.FLOOR
        } }
}

private fun parseInput(path: String): List<List<Seat>> {
    return getResourceAsList(path)
        .filter { it.isNotBlank() }
        .map { transformLine(it) }
}

private fun getNewStatus(
    row: Int, col: Int, limit: Int, seats: List<List<Seat>>, findNeighbours: (Int, Int) -> List<Seat>): Seat {
    val seat = seats[row][col]
    val neighbours = findNeighbours(row, col)
    if (seat == Seat.EMPTY && neighbours.all { it != Seat.OCCUPIED }) {
        return Seat.OCCUPIED
    }
    if (seat == Seat.OCCUPIED && neighbours.count { it == Seat.OCCUPIED } >= limit) {
        return Seat.EMPTY
    }
    return seat
}

private fun posInGrid(pos: Pair<Int, Int>, seats: List<List<Seat>>): Boolean {
    return 0 <= pos.first
            && pos.first < seats.size
            && 0 <= pos.second
            && pos.second < seats[0].size
}

private fun directNeighbours(row: Int, col: Int, seats: List<List<Seat>>): List<Seat> {
    return listOf(
        row - 1 to col - 1, row - 1 to col, row - 1 to col + 1,
        row to col - 1, row to col + 1,
        row + 1 to col - 1, row + 1 to col, row + 1 to col + 1
    )
        .filter { posInGrid(it, seats) }
        .map { seats[it.first][it.second] }
}

private fun neighbourInDirection(positions: List<Pair<Int, Int>>, seats: List<List<Seat>>): Seat? {
    return positions
        .filter { posInGrid(it, seats) }
        .map { seats[it.first][it.second] }
        .find { it != Seat.FLOOR }
}

private const val MAX_STEPS = 100

private fun directionVector(row: Int, col: Int, vector: Pair<Int, Int>): List<Pair<Int, Int>> {
    return IntRange(1, MAX_STEPS).map { row + it * vector.first to col + it * vector.second }
}

fun viewNeighbours(row: Int, col: Int, seats: List<List<Seat>>): List<Seat> {
    val directionVectorFun =  { a: Pair<Int, Int> -> directionVector(row, col, a) }
    val neighbourFun = { x: List<Pair<Int, Int>> -> neighbourInDirection(x, seats)}
    return listOfNotNull(
        neighbourFun(directionVectorFun(0 to 1)),
        neighbourFun(directionVectorFun(0 to -1)),
        neighbourFun(directionVectorFun(1 to 0)),
        neighbourFun(directionVectorFun(-1 to 0)),
        neighbourFun(directionVectorFun(1 to 1)),
        neighbourFun(directionVectorFun(1 to -1)),
        neighbourFun(directionVectorFun(-1 to 1)),
        neighbourFun(directionVectorFun(-1 to -1))
   )
}

fun executeStep(seats: List<List<Seat>>, limit: Int, neighbourFun: (Int, Int, List<List<Seat>>) -> List<Seat>): List<List<Seat>> {
    val modSeats = seats
        .map { it.toMutableList() }
        .toMutableList()
    val nFun = { a: Int, b: Int -> neighbourFun(a, b, seats) }
    for (r in seats.indices) {
        for (c in seats[0].indices) {
            modSeats[r][c] = getNewStatus(r, c, limit, seats, nFun)
        }
    }
    return modSeats
        .map { it.toList() }
        .toList()
}

fun executeDay11Part1(path: String): Int {
    var seats = parseInput(path)
    var before = listOf<List<Seat>>()
    while (seats != before) {
        before = seats.toList()
        seats = executeStep(seats.toList(), 4) { a, b, c -> directNeighbours(a, b, c)}
    }
    return seats
        .map { it.count { x -> x == Seat.OCCUPIED }}
        .reduceRight { a, b -> a + b}
}

fun countOccupiedSeats(seats: List<List<Seat>>): Int {
    return seats
        .map { it.count { x -> x == Seat.OCCUPIED }}
        .reduceRight { a, b -> a + b}
}

fun executeDay11Part2(path: String): Int {
    var seats = parseInput(path)
    var before = listOf<List<Seat>>()
    while (seats != before) {
        before = seats.toList()
        seats = executeStep(seats.toList(), 5) { a, b, c -> viewNeighbours(a, b, c)}
    }
    return countOccupiedSeats(seats)
}