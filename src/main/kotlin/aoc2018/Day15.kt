package aoc2018

import getInputAsLines
import util.Day
import java.util.LinkedList
import kotlin.Comparator
import java.util.PriorityQueue


private const val INF = 100_000

class Day15: Day("15") {

    private fun parse(name: String): Triple<MutableSet<Pair<Int, Int>>, MutableSet<Pair<Int, Int>>, MutableSet<Pair<Int, Int>>> {
        val elves = mutableSetOf<Pair<Int, Int>>()
        val goblins = mutableSetOf<Pair<Int, Int>>()
        val positions = mutableSetOf<Pair<Int, Int>>()
        val lines = getInputAsLines(name).filter { it.isNotBlank() }
        for (row in lines.indices) {
            for (col in lines[0].indices) {
                if (lines[row][col] != '#') {
                    positions.add(row to col)
                }
                if (lines[row][col] == 'G') {
                    goblins.add(row to col)
                }
                if (lines[row][col] == 'E') {
                    elves.add(row to col)
                }
            }
        }
        return Triple(positions, elves, goblins)
    }

    data class Area(val key: Int, val positions: Set<Pair<Int, Int>>, val doors: Map<Int, Set<Pair<Int, Int>>>) {
        override fun toString(): String {
            return positions.toString()
        }
    }

    data class Player(var position: Pair<Int, Int>,  var type: String = "elf", var life: Int = 200, var attackPower: Int = 3) {
        override fun toString() = "${if (type == "elf") "e" else "g"}(${position.first},${position.second}/${life}/${attackPower})"
    }

    data class Playfield(val areas: Collection<Area>, var players: MutableList<Player>, var finished: Boolean = false)

    private fun neighbors(p: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val (row, col) = p
        return setOf(row-1 to col, row+1 to col, row to col-1, row to col+1);
    }

    private fun findNextStep(playfield: Playfield, unit: Player, openSpaces: List<Pair<Int, Int>>): Set<Pair<Int, Int>> {
        if (openSpaces.isEmpty()) {
            return emptySet()
        }
        val distance = playfield.areas.flatMap { it.positions }.associateWith { INF }.toMutableMap()
        val prev = playfield.areas.flatMap { it.positions }.associateWith { mutableSetOf<Pair<Int, Int>>() }.toMutableMap()
        val playerPositions = playfield.players.filter { it.life > 0 }.map { it.position }.toSet();
        distance[unit.position] = 0
        val queue = LinkedList<Pair<Int, Int>>()
        queue.addAll(playfield.areas.flatMap { it.positions }.toSet())
        while (queue.isNotEmpty()) {
            val u = queue.minByOrNull { distance[it]!! }!!
            queue.remove(u)
            neighbors(u)
                .filter { queue.contains(it) }
                .filterNot { playerPositions.contains(it) }
                .forEach { v ->
                    val alt = distance[u]!! + 1
                    if (alt < distance[v]!!) {
                        distance[v] = alt
                        prev[v] = mutableSetOf(u)
                    } else if (alt == distance[v]!!) {
                        prev[v]!!.add(u)
                    }
                }
        }
        val sortedAsc = openSpaces.sortedBy { distance[it] }
        val candidate = sortedAsc
            .takeWhile { distance[it] == distance[sortedAsc[0]] }
            .minByOrNull { openSpaces.indexOf(it) }!!
        if (distance[candidate] == INF) {
            return emptySet();
        }
        var next = setOf(candidate)
        while (!next.flatMap { prev[it]!! }.contains(unit.position)) {
            next = next.flatMap { prev[it]!! }.toSet()
        }
        return next
    }

    private fun move(playfield: Playfield, unit: Player) {
        val targets = playfield.players.filter { it.type != unit.type }.filter { it.life > 0 }
        if (targets.isEmpty()) {
            playfield.finished = true
            return
        }
        val allFields = playfield.areas.flatMap { it.positions }.toSet()
        val playerPositions = playfield.players.filter { it.life > 0 }.map { it.position }.toSet()
        val unitNeighbors = neighbors(unit.position)
        if (targets.any { unitNeighbors.contains(it.position) }) {
            return;
        } else {
            val openSpaces = targets.flatMap { neighbors(it.position) }
                .asSequence()
                .filter { allFields.contains(it) }
                .filterNot { playerPositions.contains(it) }
                .sortedBy { it.second }
                .sortedBy { it.first }
                .toList()
            val candidates = findNextStep(playfield, unit, openSpaces)
            if (candidates.isEmpty()) {
                return;
            }
            unit.position = candidates.sortedBy { it.second }.minByOrNull { it.first }!!
        }
    }

    private fun attack(playfield: Playfield, unit: Player) {
        val neighbors = neighbors(unit.position)
        val targets = playfield.players
            .filter { it.life > 0 }
            .filter { it.type != unit.type }
            .filter { neighbors.contains(it.position) }
            .sortedBy { it.life }
        if (targets.isEmpty()) {
            return;
        }
        val candidate = targets
            .takeWhile { it.life == targets[0].life }
            .sortedBy { it.position.second }.minByOrNull { it.position.first }!!
        candidate.life -= unit.attackPower
    }

    private fun sort(playfield: Playfield) {
        playfield.players = playfield.players
            .filter { it.life > 0 }
            .sortedBy { it.position.second }
            .sortedBy { it.position.first }.toMutableList()
    }

    private fun turn(playfield: Playfield, unit: Player) {
        if (unit.life <= 0) {
            return
        }
        move(playfield, unit)
        if (playfield.finished) {
            return
        }
        attack(playfield, unit)
    }

    private fun playOneRound(playfield: Playfield) {
        playfield.players.forEach { turn(playfield, it) }
        sort(playfield)
    }

    override fun executePart1(name: String): Any {
        val (positions, e, g) = parse(name)
        val area = Area(1, positions, mapOf())
        val players = (e.map { Player(it) } + g.map { Player(it, "goblin") }).sortedBy { it.position.second }.sortedBy { it.position.first }
        val playfield = Playfield(listOf(area), players.toMutableList())
        var c = -1
        while (!playfield.finished) {
            playOneRound(playfield)
            c++
        }
        return c.toLong() * (playfield.players.sumOf { it.life.toLong() })
    }

    private fun bla(name: String) {
        val (positions, e, g) = parse(name)
        val area1 = Area(1, (positions.filter { it.first <= 8 } + (9 to 6)).toSet(), mapOf(2 to setOf(9 to 16)))
        val area2 = Area(
            2,
            (positions.filter { it.first in 9..26 }.filter { it.second in 0..17 } + (27 to 12) + (27 to 13) + (27 to 8)).toSet(),
            mapOf(1 to setOf(9 to 16), 3 to setOf(21 to 11, 21 to 17), 4 to setOf(27 to 8)))
        val area3 = Area(3, (positions.filter { it.first in 9..27 }.filter { it.second in 18..31 } + (21 to 11) + (21 to 17)).toSet(), mapOf(2 to setOf(21 to 11, 21 to 17)))
        val area4 = Area(4, (positions.filter { it.first >= 27 }.filter { it.second <= 9 }).toSet(), mapOf(2 to setOf(27 to 8)))
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}