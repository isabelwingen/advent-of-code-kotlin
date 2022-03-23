package algorithm

class GameOfLife <T>(
    private var livingCells: Set<T>,
    private val allCells: Set<T>?,
    private val getNeighbours: (t: T) -> Set<T>,
    private val comeAlive: (neighbours: Set<T>, alive: Set<T>) -> Boolean,
    private val die: (neighbours: Set<T>, alive: Set<T>) -> Boolean) {

    private var changed = true

    fun step() {
        val before = livingCells.toSet()
        val cellsThatKeepOnLiving = livingCells
            .filter { !die(getNeighbours(it), livingCells) }
        val cellsThatComeAlive = getCandidatesThatMayComeAlive()
            .filter { !livingCells.contains(it) }
            .filter { comeAlive(getNeighbours(it), livingCells) }
        livingCells = (cellsThatComeAlive + cellsThatKeepOnLiving).toSet()
        changed = before != livingCells
    }

    private fun getCandidatesThatMayComeAlive(): Set<T> {
        return allCells ?: livingCells
            .flatMap { getNeighbours(it) }
            .toSet()
    }

    fun getLivingCells(): Set<T> {
        return livingCells.toSet()
    }

    fun hasChanged(): Boolean {
        return changed
    }
}