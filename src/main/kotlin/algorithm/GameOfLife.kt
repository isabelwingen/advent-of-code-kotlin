package algorithm

class GameOfLife <T> (
    private var initialState: Set<T>,
    private val getNeighbours: (t: T) -> Set<T>,
    private val comeAlive: (neighbours: Set<T>, alive: Set<T>) -> Boolean,
    private val die: (neighbours: Set<T>, alive: Set<T>) -> Boolean) {

    fun step() {
        val cellsThatKeepOnLiving = initialState
            .filter { !die(getNeighbours(it), initialState) }
        val cellsThatComeAlive = initialState
            .flatMap(getNeighbours)
            .distinct()
            .filter { !initialState.contains(it) }
            .filter { comeAlive(getNeighbours(it), initialState) }
        initialState = (cellsThatComeAlive + cellsThatKeepOnLiving).toSet()
    }

    fun getLivingCells(): Set<T> {
        return initialState.toSet()
    }
}