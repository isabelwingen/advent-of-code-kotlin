package aoc2019

import getInputAsLines
import util.Day
import java.util.LinkedList
import java.util.PriorityQueue

class Day18: Day("18") {

    private fun readMaze(name: String): List<List<Char>> {
        return getInputAsLines(name)
            .map { it.map { x -> x } }
            .filter { it.isNotEmpty() }
    }

    data class Node(val pos: Pair<Int, Int>, val value: Char) {
        override fun toString()= "$value"
    }

    data class Graph(private val map: Map<Set<Node>, Int>) {

        private val mmap = map.toMutableMap()

        private fun removeNode(node: Node): Graph {
            mmap.entries.removeIf { it.key.contains(node) }
            return this
        }

        private fun addEdge(nodeA: Node, nodeB: Node, weight: Int): Graph {
            val edge = setOf(nodeA, nodeB)
            if (mmap.getOrDefault(edge, Int.MAX_VALUE) > weight) {
                mmap[edge] = weight
            }
            return this
        }

        fun contractNode(node: Node): Graph {
            val edges = mmap.entries
                .filter { it.key.contains(node) }
                .map { it.key.first { k -> k != node } to it.value }
            for (i in 1 until edges.size) {
                for (j in 0 until i) {
                    addEdge(edges[i].first, edges[j].first, edges[i].second + edges[j].second)
                }
            }
            removeNode(node)
            return this
        }

        fun simplify(): Graph {
            mmap.keys.flatten().distinct().filter { it.value == '.' }.forEach { contractNode(it) }
            return this
        }

        override fun toString(): String {
            return mmap.entries
                .map { it.key.toList() to it.value }
                .map { Triple(it.first[0].value, it.first[1].value, it.second) }
                .map { "${it.first} <- ${it.third} -> ${it.second}" }
                .joinToString("\n") { it }
        }

        fun getNeighbours(node: Node): List<Pair<Node, Int>> {
            return mmap
                .entries
                .filter { it.key.contains(node) }
                .map { it.key.find { k -> k != node }!! to it.value }
        }

        private fun getStartNodes(): List<Node> {
            return mmap.keys.flatten().distinct().filter { it.value == '@' }
        }

        fun findMatchingDoor(key: Char) = mmap.keys.flatten().find { it.value == key.uppercaseChar() }

        fun deepCopy(): Graph {
            return Graph(mmap.toMap())
        }

        fun toInitialState(): State {
            val startNodes = this.getStartNodes()
            val visited = LinkedList<Node>()
            return State(this, startNodes, visited, 0)
        }

        fun numberOfKeys(): Int {
            return mmap.keys.flatten().filter { it.value.isLowerCase() }.distinct().size
        }

    }

    private fun createGraph(maze: List<List<Char>>): Graph {
        val map = mutableMapOf<Set<Node>, Int>()
        for (row in maze.indices) {
            for (col in maze[0].indices) {
                val value = maze[row][col]
                if (value != '#') {
                val node = Node(row to col, value)
                    if (row > 0 && maze[row-1][col] != '#') {
                        map[setOf(node, Node(row - 1 to col, maze[row-1][col]))] = 1
                    }
                    if (row < maze.size - 1 && maze[row+1][col] != '#') {
                        map[setOf(node, Node(row + 1 to col, maze[row+1][col]))] = 1

                    }
                    if (col > 0 && maze[row][col-1] != '#') {
                        map[setOf(node, Node(row to col - 1, maze[row][col-1]))] = 1
                    }
                    if (col < maze[0].size - 1 && maze[row][col+1] != '#') {
                        map[setOf(node, Node(row to col + 1, maze[row][col+1]))] = 1

                    }
                }
            }
        }
        return Graph(map.toMap())
    }

    data class State(val graph: Graph, val currentPositions: List<Node>, val visited: LinkedList<Node>, val steps: Int) {

        override fun toString(): String {
            return "Current Positions: ${currentPositions}, Path: $visited ($steps)"
        }

        private fun removeDoor(keyNode: Node): Graph {
            val doorNode = graph.findMatchingDoor(keyNode.value)
            return if (doorNode != null) {
                graph.deepCopy().contractNode(doorNode)
            } else {
                graph.deepCopy()
            }
        }

        private fun collectKey(currentNode: Node, keyNode: Node, stepsToKey: Int): State {
            assert(keyNode.value.isLowerCase())
            assert(!visited.contains(keyNode))
            val newGraph = removeDoor(keyNode).contractNode(currentNode)
            val newVisited = LinkedList(visited)
            newVisited.add(keyNode)
            val index = currentPositions.indexOf(currentNode)
            return State(newGraph,  currentPositions.replace(index, keyNode), newVisited, steps + stepsToKey)
        }

        fun branchToCollectableKeys(currentPosition: Node): List<State> {
            val y = graph.getNeighbours(currentPosition)
                .filter { it.first.value.isLowerCase() && !visited.contains(it.first) }
                .map { collectKey(currentPosition, it.first, it.second) }
            return y
        }

        fun branchToCollectableKeys(): List<State> {
            val x = currentPositions.flatMapIndexed { _, currentPosition -> branchToCollectableKeys(currentPosition) }
            return x
        }

        fun isBadStateInComparisonTo(otherState: State) =
            currentPositions == otherState.currentPositions
                    && visited.all { otherState.visited.contains(it) }
                    && steps >= otherState.steps
    }

    private fun findAllKeys(graph: Graph): State {
        val compareByLength: Comparator<State> = compareBy { it.steps }

        val queue = PriorityQueue(compareByLength)
        val initialState = graph.toInitialState()
        queue.add(initialState)
        var result = initialState
        val numberOfKeys = graph.numberOfKeys()
        while (queue.isNotEmpty()) {
            val currentState = queue.poll()
            if (currentState.visited.filter { it.value != '@' }.size == numberOfKeys) {
                if (result == initialState || result.steps > currentState.steps) {
                    result = currentState
                }
            } else {
                queue.addAndImprove(currentState.branchToCollectableKeys())
            }
        }
        return result
    }

    override fun executePart1(name: String): Int {
        val graph = listOf(name)
            .map { readMaze(it) }
            .map { createGraph(it) }
            .map { it.simplify() }
            .first()
        return findAllKeys(graph).steps
    }

    override fun expectedResultPart1() = 5858

    private fun readMazeAndChangeEntrance(name: String): List<List<Char>> {
        val origMaze = readMaze(name).map { it.toMutableList() }.toMutableList()
        val (row,col) = origMaze.mapIndexed { i, chars -> chars.mapIndexed { j, c -> i to j to c } }
            .first { line -> line.any { it.second == '@' } }
            .first { it.second == '@' }
            .first
        origMaze[row-1][col-1] = '@'
        origMaze[row-1][col] = '#'
        origMaze[row-1][col+1] = '@'
        origMaze[row][col-1] = '#'
        origMaze[row][col] = '#'
        origMaze[row][col+1] = '#'
        origMaze[row+1][col-1] = '@'
        origMaze[row+1][col] = '#'
        origMaze[row+1][col+1] = '@'
        return origMaze.map { it.toList() }.toList()
    }

    override fun executePart2(name: String): Int {
        val graph = listOf(name)
            .map { readMazeAndChangeEntrance(it) }
            .map { createGraph(it) }
            .map { it.simplify() }
            .first()
        return findAllKeys(graph).steps
    }

    override fun expectedResultPart2() = 2144
}

private fun PriorityQueue<Day18.State>.addAndImprove(newStates: List<Day18.State>) {
    for (state in newStates) {
        if (this.none { queueState -> state.isBadStateInComparisonTo(queueState) }) {
            this.removeIf { it.isBadStateInComparisonTo(state) }
            this.add(state)
        }
    }
}

private fun <E> List<E>.replace(i: Int, e: E): List<E> {
    val p = this.toMutableList()
    p[i] = e
    return p.toList()
}


