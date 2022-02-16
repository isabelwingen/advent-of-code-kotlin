import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

internal class Day11KtTest {

    inline fun <T> MutableList<T>.mapInPlace(mutator: (T)->T) {
        val iterate = this.listIterator()
        while (iterate.hasNext()) {
            val oldValue = iterate.next()
            val newValue = mutator(oldValue)
            if (newValue !== oldValue) {
                iterate.set(newValue)
            }
        }
    }

    @Test
    fun executeDay11Part1() {
        val someList = arrayListOf(1, 20, 10, 55, 30, 22, 11, 0, 99)
        someList.mapInPlace { if (it <= 20) it + 20 else it }
        println(someList)
    }

    @Test
    fun executeDay11Part2() {
        val seats = listOf(
            listOf(Seat.OCCUPIED, Seat.FLOOR, Seat.EMPTY, Seat.FLOOR, Seat.OCCUPIED),
            listOf(Seat.FLOOR, Seat.FLOOR, Seat.FLOOR, Seat.FLOOR, Seat.FLOOR),
            listOf(Seat.OCCUPIED, Seat.FLOOR, Seat.OCCUPIED, Seat.FLOOR, Seat.OCCUPIED),
            listOf(Seat.FLOOR, Seat.FLOOR, Seat.FLOOR, Seat.FLOOR, Seat.FLOOR),
            listOf(Seat.OCCUPIED, Seat.FLOOR, Seat.OCCUPIED, Seat.FLOOR, Seat.OCCUPIED),
        )
        println(viewNeighbours(2, 2, seats))

    }

}