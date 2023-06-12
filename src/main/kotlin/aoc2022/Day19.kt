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
        val (a,b,c,d,e) = line.split("Each ")
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

    override fun executePart1(name: String): Any {
        val blueprints = parseInput(name)
        return maxGeodes(blueprints[0])
    }

    private fun maxGeodes(blueprint: List<List<Int>>) {
        val queue = LinkedList<List<Int>>()
        queue.add(List(8) { if (it == 0) 1 else 0 })
        val seen = mutableSetOf<List<Int>>()
        var maxGeodes = 0
        while (queue.isNotEmpty()) {
            val state = queue.pop()
            println("$maxGeodes, ${queue.size}: $state")
            seen.add(state)
            if (state[SCORE] > maxGeodes) {
                maxGeodes = state[SCORE]
            }
            if (state[STEPS] > 23) {
                // do nothing
            } else if (state[STEPS] == 22) {
                addToQueue(build(GEODE_ROBOT, state, blueprint), queue, seen)
            } else {
                IntRange(0, 3).forEach { addToQueue(build(it, state, blueprint), queue, seen) }
            }
        }
    }

    private fun addToQueue(elem: List<Int>?, queue: LinkedList<List<Int>>, seen: MutableSet<List<Int>>) {
        if (elem != null) {
            if (!queue.contains(elem) && !seen.contains(elem)) {
                queue.add(elem)
            }
        }
    }

    fun build(robot_id: Int, state: List<Int>, blueprint: List<List<Int>>): List<Int>? {
        val (needed_ore, needed_clay, needed_obsidian) = blueprint[robot_id]
        if (needed_ore > 0 && state[ORE_ROBOT] == 0) {
            return null
        }
        if (needed_clay > 0 && state[CLAY_ROBOT] == 0) {
            return null
        }
        if (needed_obsidian > 0 && state[OBSIDIAN_ROBOT] == 0) {
            return null
        }
        val newState = state.toMutableList()
        if (state[ORE_MATERIAL] >= needed_ore && state[CLAY_MATERIAL] >= needed_clay && state[OBSIDIAN_MATERIAL] >= needed_obsidian) {
            newState[ORE_MATERIAL] += newState[ORE_ROBOT] - needed_ore
            newState[CLAY_MATERIAL] += newState[CLAY_ROBOT] - needed_clay
            newState[OBSIDIAN_MATERIAL] += newState[OBSIDIAN_ROBOT] - needed_obsidian
            newState[STEPS] += 1
            if (robot_id == GEODE_ROBOT) {
                val timeRemaining = 24 - newState[STEPS]
                newState[SCORE] += timeRemaining
            } else {
                newState[robot_id] += 1
            }
        } else {
            val stepsToOre =
                if (state[ORE_MATERIAL] >= needed_ore) {
                    0
                } else {
                   ceil((needed_ore-state[ORE_MATERIAL]).toDouble() / state[ORE_ROBOT]).toInt()
                }
            val stepsToClay =
                if (state[CLAY_MATERIAL] >= needed_clay) {
                    0
                } else {
                    ceil((needed_clay-state[CLAY_MATERIAL]).toDouble() / state[CLAY_ROBOT]).toInt()
                }
            val stepsToObisidian =
                if (state[OBSIDIAN_MATERIAL] >= needed_obsidian) {
                    0
                } else {
                    ceil((needed_obsidian-state[OBSIDIAN_MATERIAL]).toDouble() / state[OBSIDIAN_ROBOT]).toInt()
                }
            val stepsNeeded = listOf(stepsToOre, stepsToClay, stepsToObisidian).maxOf { it } + 1
            newState[ORE_MATERIAL] += newState[ORE_ROBOT] * stepsNeeded - needed_ore
            newState[CLAY_MATERIAL] += newState[CLAY_ROBOT] * stepsNeeded - needed_clay
            newState[OBSIDIAN_MATERIAL] += newState[OBSIDIAN_ROBOT] * stepsNeeded - needed_obsidian
            newState[STEPS] += stepsNeeded
            if (newState[STEPS] > 24) {
                return null
            }
            if (robot_id == GEODE_ROBOT) {
                val timeRemaining = 24 - newState[STEPS]
                newState[SCORE] += timeRemaining
            } else {
                newState[robot_id] += 1
            }
        }
        return newState.toList()
    }


    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}