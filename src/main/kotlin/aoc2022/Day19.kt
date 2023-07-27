package aoc2022

import getInputAsLines
import util.Day
import java.util.LinkedList
import kotlin.math.ceil

const val ORE = 0
const val CLAY = 1
const val OBSIDIAN = 2
const val GEODE = 3
const val ORE_STASH = 3
const val CLAY_STASH = 4
const val OBSIDIAN_STASH = 5
const val SCORE = 6
const val STEPS = 7

class Day19: Day("19") {

    private fun parseLine(line: String): Array<IntArray> {
        val (_,b,c,d,e) = line.split("Each ")
        val ore = listOf(b.split(" ")[3].toInt(), 0, 0).toIntArray()
        val clay = listOf(c.split(" ")[3].toInt(), 0, 0).toIntArray()
        val obsidian = listOf(d.split(" ")[3].toInt(), d.split(" ")[6].toInt(), 0).toIntArray()
        val geode = listOf(e.split(" ")[3].toInt(), 0, e.split(" ")[6].toInt()).toIntArray()
        return arrayOf(ore, clay, obsidian, geode)
    }

    private fun parseInput(name: String): List<Array<IntArray>> {
        return getInputAsLines(name)
            .filter { x -> x.isNotBlank()}
            .map { parseLine(it) }
    }

    override fun executePart1(name: String): Long {
        val blueprints = parseInput(name)
        return blueprints.mapIndexed { index, blueprint ->  maxGeodes(blueprint, 24) * (index+1) }.sumOf { it }.toLong()
    }

    private fun maxGeodes(blueprint: Array<IntArray>, time: Int): Int {
        val queue = LinkedList<IntArray>()
        queue.add(intArrayOf(1,0,0,0,0,0,0,0))
        var maxGeodes = 0
        val maxCosts = (ORE..OBSIDIAN).map { material -> blueprint.maxOf { it[material] } }.toIntArray()
        while (queue.isNotEmpty()) {
            val state = queue.pop()
            maxGeodes = maxOf(maxGeodes, state[SCORE])

            val potential = potential(time, state)
            if (potential < maxGeodes) {
                continue
            }

            if (state[STEPS] < time-1) {
                (ORE..GEODE).forEach { robot ->
                    val nextState = build(robot, state, blueprint, maxCosts, time)
                    addToQueue(nextState, queue)
                }
            }
        }
        return maxGeodes
    }

    private fun potential(time: Int, state: IntArray): Int {
        return IntRange(1, (time - state[STEPS] - 1)).sum() + state[SCORE]
    }

    private fun addToQueue(elem: IntArray?, queue: LinkedList<IntArray>) {
        if (elem != null && !queue.contains(elem)) {
            queue.add(0, elem)
        }
    }

    fun build(robot_id: Int, state: IntArray, blueprint: Array<IntArray>, maxCosts: IntArray, time: Int): IntArray? {
        // these are the materials needed to build a robot with id `robot_id`
        val needed = blueprint[robot_id]

        // check for each material needed, if there is at least on robot
        (ORE..OBSIDIAN).forEach {
            if (needed[it] > 0 && state[it] == 0) {
                return null
            }
        }

        if (robot_id < 3) {
            if (state[robot_id] >= maxCosts[robot_id]) {
                return null
            }
        }

        if (robot_id < 3) {
            val timeRemaining = time - 1 - state[STEPS]
            val unitsProducedTilEnd = state[robot_id + 3] + timeRemaining * state[robot_id]
            val unitsNeeded = (timeRemaining + 1) * maxCosts[robot_id]
            if (unitsProducedTilEnd >= unitsNeeded-11) {
                return null
            }
        }

        val newState = state.copyOf()
        if (state[ORE_STASH] >= needed[ORE] && state[CLAY_STASH] >= needed[CLAY] && state[OBSIDIAN_STASH] >= needed[OBSIDIAN]) {
            (ORE..OBSIDIAN).forEach {
                newState[it+3] += newState[it] - needed[it]
            }
            newState[STEPS] += 1
            if (robot_id == GEODE) {
                val timeRemaining = time - newState[STEPS]
                newState[SCORE] += timeRemaining
            } else {
                newState[robot_id] += 1
            }
        } else {
            val stepsNeeded = (ORE..OBSIDIAN)
                .maxOf { if (state[it+3] >= needed[it]) 0 else ceil((needed[it]-state[it+3]).toDouble() / state[it]).toInt() } + 1
            (ORE..OBSIDIAN).forEach {
                newState[it+3] += newState[it] * stepsNeeded - needed[it]
            }
            newState[STEPS] += stepsNeeded
            if (newState[STEPS] > time) {
                return null
            }
            if (robot_id == GEODE) {
                val timeRemaining = time - newState[STEPS]
                newState[SCORE] += timeRemaining
            } else {
                newState[robot_id] += 1
            }
        }
        return newState
    }


    override fun executePart2(name: String): Long {
        val blueprints = parseInput(name).take(3)
        val (a,b,c) = blueprints.map { maxGeodes(it, 32) }
        return (a*b*c).toLong()
    }
}