package aoc2018

import getInputAsLines
import util.Day
import java.util.LinkedList


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

    data class Player(var position: Pair<Int, Int>, val type: String = "elf", val attackPower: Int,  var life: Int = 200) {
        override fun toString() = "${if (type == "elf") "e" else "g"}(${position.first},${position.second}/${life}/${attackPower})"
    }

    data class Playfield(val area: Set<Pair<Int, Int>>, val players: MutableList<Player>, var finished: Boolean = false, var elfDied: Boolean = false)

    private fun neighbors(p: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val (row, col) = p
        return setOf(row-1 to col, row+1 to col, row to col-1, row to col+1);
    }

    private fun findNextStep(playfield: Playfield, unit: Player, openSpaces: List<Pair<Int, Int>>): Set<Pair<Int, Int>> {
        if (openSpaces.isEmpty()) {
            return emptySet()
        }
        val distance = playfield.area.associateWith { INF }.toMutableMap()
        val prev = playfield.area.associateWith { mutableSetOf<Pair<Int, Int>>() }.toMutableMap()
        val playerPositions = playfield.players.filter { it.life > 0 }.map { it.position }.toSet();
        distance[unit.position] = 0
        val queue = LinkedList<Pair<Int, Int>>()
        queue.addAll(playfield.area)
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
        val playerPositions = playfield.players.filter { it.life > 0 }.map { it.position }.toSet()
        val unitNeighbors = neighbors(unit.position)
        if (targets.any { unitNeighbors.contains(it.position) }) {
            return;
        } else {
            val openSpaces = targets.flatMap { neighbors(it.position) }
                .asSequence()
                .filter { playfield.area.contains(it) }
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

    private fun cleanUp(playfield: Playfield) {
        if (playfield.players.any { it.type == "elf" && it.life <= 0 }) {
            playfield.elfDied = true
        }
        playfield.players.removeIf { it.life <= 0 }
        playfield.players.sortBy { it.position.second }
        playfield.players.sortBy { it.position.first }
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
        cleanUp(playfield)
    }

    private fun createPlayfield(attackPower: Int, positions: Set<Pair<Int, Int>>, elves: Set<Pair<Int, Int>>, goblins: Set<Pair<Int, Int>>): Playfield {
        val players = (elves.map { Player(it, "elf", attackPower) } + goblins.map { Player(it, "goblin", 3) }).sortedBy { it.position.second }.sortedBy { it.position.first }
        return Playfield(positions, players.toMutableList())
    }

    override fun executePart1(name: String): Any {
        val (positions, e, g) = parse(name)
        val playfield = createPlayfield(3, positions, e, g)
        var c = -1
        while (!playfield.finished) {
            playOneRound(playfield)
            c++
        }
        return c.toLong() * (playfield.players.sumOf { it.life.toLong() })
    }



    override fun executePart2(name: String): Any {
        val (positions, e, g) = parse(name)
        val playfield = createPlayfield(26, positions, e, g)
        var c = -1
        while (!playfield.finished) {
            playOneRound(playfield)
            c++
        }
        return c.toLong() * (playfield.players.sumOf { it.life.toLong() })
    }
}