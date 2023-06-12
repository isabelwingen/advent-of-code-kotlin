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


    private fun parseLine(line: String): List<List<Int>> {
        val (_,b,c,d,e) = line.split("Each ")
        val ore = listOf(b.split(" ")[3].toInt(), 0, 0)
        val clay = listOf(c.split(" ")[3].toInt(), 0, 0)
        val obsidian = listOf(d.split(" ")[3].toInt(), d.split(" ")[6].toInt(), 0)
        val geode = listOf(e.split(" ")[3].toInt(), 0, e.split(" ")[6].toInt())
        return listOf(ore, clay, obsidian, geode)
    }

    private fun parseInput(name: String): List<List<List<Int>>> {
        return getInputAsLines(name)
            .filter { x -> x.isNotBlank()}
            .map { parseLine(it) }
    }

    override fun executePart1(name: String): Long {
        val blueprints = parseInput(name)
        return blueprints.mapIndexed { index, blueprint ->  maxGeodes(blueprint, 24) * (index+1) }.sumOf { it }.toLong()
    }

    private fun maxGeodes(blueprint: List<List<Int>>, time: Int): Int {
        val queue = LinkedList<List<Int>>()
        queue.add(List(8) { if (it == 0) 1 else 0 })
        val seen = mutableSetOf<List<Int>>()
        var maxGeodes = 0
        val maxCosts = IntRange(0,2).map { material -> blueprint.maxOf { it[material] } }
        while (queue.isNotEmpty()) {
            val state = queue.pop()
            seen.add(state)
            if (state[SCORE] > maxGeodes) {
                maxGeodes = state[SCORE]
            }
            val potential = potential(time, state)
            if (potential < maxGeodes) {
                continue
            }
            if (state[STEPS] >= time-1) {
                // do nothing
            } else if (state[STEPS] == time-2 && state[STEPS] == time-3) { // time-3 is kinda dirty, may be dependend on input.
                addToQueue(build(GEODE_ROBOT, state, blueprint, maxCosts, time), queue, seen)
            } else {
                IntRange(0, 3).forEach { addToQueue(build(it, state, blueprint, maxCosts, time), queue, seen) }
            }
        }
        return maxGeodes
    }

    private fun potential(time: Int, state: List<Int>): Int {
        return IntRange(1, (time - state[STEPS] - 1)).sum() + state[SCORE]
    }

    private fun addToQueue(elem: List<Int>?, queue: LinkedList<List<Int>>, seen: MutableSet<List<Int>>) {
        if (elem != null) {
            if (!queue.contains(elem) && !seen.contains(elem)) {
                queue.add(0, elem)
            }
        }
    }

    fun build(robot_id: Int, state: List<Int>, blueprint: List<List<Int>>, maxCosts: List<Int>, time: Int): List<Int>? {
        val needed = blueprint[robot_id]
        // robots missing to build it
        IntRange(0, 2).forEach {
            if (needed[it] > 0 && state[it] == 0) {
                return null
            }
        }
        // we have enough robots of this kind (step 1)
        if (robot_id < 3) {
            if (state[robot_id] >= maxCosts[robot_id]) {
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

        val newState = state.toMutableList()
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
        return newState.toList()
    }


    override fun executePart2(name: String): Long {
        val blueprints = parseInput(name).take(3)
        val (a,b,c) = blueprints.map { maxGeodes(it, 32) }
        return (a*b*c).toLong()
    }
}