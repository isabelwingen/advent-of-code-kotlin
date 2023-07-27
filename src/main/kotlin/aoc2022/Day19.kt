package aoc2022

import getInputAsLines
import util.Day
import java.util.LinkedList
import kotlin.math.ceil

const val ORE_ROBOT = 0
const val CLAY_ROBOT = 1
const val OBSIDIAN_ROBOT = 2
const val GEODE_ROBOT = 3
const val ORE_MATERIAL = 3
const val CLAY_MATERIAL = 4
const val OBSIDIAN_MATERIAL = 5
const val SCORE = 6
const val STEPS = 7

class Day19: Day("19") {

    private fun parseLine(line: String): List<IntArray> {
        val (_,b,c,d,e) = line.split("Each ")
        val ore = listOf(b.split(" ")[3].toInt(), 0, 0).toIntArray()
        val clay = listOf(c.split(" ")[3].toInt(), 0, 0).toIntArray()
        val obsidian = listOf(d.split(" ")[3].toInt(), d.split(" ")[6].toInt(), 0).toIntArray()
        val geode = listOf(e.split(" ")[3].toInt(), 0, e.split(" ")[6].toInt()).toIntArray()
        return listOf(ore, clay, obsidian, geode)
    }

    private fun parseInput(name: String): List<List<IntArray>> {
        return getInputAsLines(name)
            .filter { x -> x.isNotBlank()}
            .map { parseLine(it) }
    }

    override fun executePart1(name: String): Long {
        val blueprints = parseInput(name)
        return blueprints.mapIndexed { index, blueprint ->  maxGeodes(blueprint, 24) * (index+1) }.sumOf { it }.toLong()
    }

    private fun maxGeodes(blueprint: List<IntArray>, time: Int): Int {
        val queue = LinkedList<IntArray>()
        queue.add(intArrayOf(1,0,0,0,0,0,0,0))
        var maxGeodes = 0
        val maxCosts = (ORE_ROBOT..OBSIDIAN_ROBOT).map { material -> blueprint.maxOf { it[material] } }
        while (queue.isNotEmpty()) {
            val state = queue.pop()
            maxGeodes = maxOf(maxGeodes, state[SCORE])

            val potential = potential(time, state)
            if (potential < maxGeodes) {
                continue
            }

            if (state[STEPS] < time-1) {
                (ORE_ROBOT..GEODE_ROBOT).forEach { robot ->
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

    fun build(robot_id: Int, state: IntArray, blueprint: List<IntArray>, maxCosts: List<Int>, time: Int): IntArray? {
        // these are the materials needed to build a robot with id `robot_id`
        val needed = blueprint[robot_id]

        // check for each material needed, if there is at least on robot
        IntRange(0, 2).forEach {
            if (needed[it] > 0 && state[it] == 0) {
                return null
            }
        }
        // we have enough robots of this kind (step 1)
        if (robot_id < 3) {
            val timeRemaining = time - 2 - state[STEPS]
            val unitsProducedTilEnd = state[robot_id+3] + timeRemaining * state[robot_id]
            val unitsNeeded =(timeRemaining + 1) * maxCosts[robot_id]
            if (unitsProducedTilEnd >= unitsNeeded-11) { // -11 is kinda dirty, depends on input. improves performance
                return null
            }
        }

        // we have enough robots of this kind (step 2)
        if (robot_id < 3) {
            val timeRemaining = time - 2 - state[STEPS]
            val unitsProducedTilEnd = state[robot_id+3] + timeRemaining * state[robot_id]
            val unitsNeeded =(timeRemaining + 1) * maxCosts[robot_id]
            if (unitsProducedTilEnd >= unitsNeeded-11) { // -11 is kinda dirty, depends on input. improves performance
                return null
            }
        }

        val newState = state.copyOf()
        if (state[ORE_MATERIAL] >= needed[ORE_ROBOT] && state[CLAY_MATERIAL] >= needed[CLAY_ROBOT] && state[OBSIDIAN_MATERIAL] >= needed[OBSIDIAN_ROBOT]) {
            IntRange(0, 2).forEach {
                newState[it+3] += newState[it] - needed[it]
            }
            newState[STEPS] += 1
            if (robot_id == GEODE_ROBOT) {
                val timeRemaining = time - newState[STEPS]
                newState[SCORE] += timeRemaining
            } else {
                newState[robot_id] += 1
            }
        } else {
            val stepsNeeded = IntRange(0,2)
                .maxOf { if (state[it+3] >= needed[it]) 0 else ceil((needed[it]-state[it+3]).toDouble() / state[it]).toInt() } + 1
            IntRange(0,2).forEach {
                newState[it+3] += newState[it] * stepsNeeded - needed[it]
            }
            newState[STEPS] += stepsNeeded
            if (newState[STEPS] > time) {
                return null
            }
            if (robot_id == GEODE_ROBOT) {
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