package aoc2023

import getInputAsLines
import lcm
import util.Day
import java.util.LinkedList

class Day20: Day("20") {

    enum class ModuleType {
        FLIP_FLOP, CONJUNCTION, BROADCAST
    }

    abstract class Module(val name: String, val type: ModuleType, val children: List<String>) {

        override fun toString() = "$type: $name -> $children"

        abstract fun receivingPulse(transmittingPulse: TransmittingPulse): List<TransmittingPulse>

        abstract fun state(): String

        companion object {
            fun parse(line: String): Module {
                val (left, right) = line.split(" -> ")
                val children = right.split(",").map { it.trim() }.filterNot { it.isEmpty() }
                return if (left.contains("broadcaster")) {
                    BroadcastModule(children)
                } else if (left.contains("%")) {
                    FlipFlopModule(left.drop(1), children)
                } else {
                    ConjunctionModule(left.drop(1), children)
                }
            }
        }
    }

    enum class FlipFlopState {
        ON, OFF
    }

    enum class Pulse {
        HIGH, LOW
    }

    class FlipFlopModule(name: String, children: List<String>, var state: FlipFlopState = FlipFlopState.OFF): Module(name, ModuleType.FLIP_FLOP, children) {

        override fun toString() = "$type: $name -> $children ($state)"

        override fun receivingPulse(transmittingPulse: TransmittingPulse): List<TransmittingPulse> {
            return if (transmittingPulse.pulse == Pulse.LOW) {
                if (state == FlipFlopState.OFF) {
                    state = FlipFlopState.ON
                    children.map { TransmittingPulse(name, it, Pulse.HIGH) }
                } else {
                    state = FlipFlopState.OFF
                    children.map { TransmittingPulse(name, it, Pulse.LOW) }
                }
            } else {
                listOf()
            }
        }

        override fun state() = "$state"
    }

    class ConjunctionModule(name: String, children: List<String>, val cache: MutableMap<String, Pulse> = mutableMapOf()): Module(name, ModuleType.CONJUNCTION, children) {
        override fun toString() = "$type: $name -> $children ($cache)"

        override fun receivingPulse(transmittingPulse: TransmittingPulse): List<TransmittingPulse> {
            cache[transmittingPulse.from] = transmittingPulse.pulse
            return if (cache.values.all { it == Pulse.HIGH }) {
                children.map { TransmittingPulse(name, it, Pulse.LOW) }
            } else {
                children.map { TransmittingPulse(name, it, Pulse.HIGH) }
            }
        }

        override fun state() = cache.entries.sortedBy { it.key }.joinToString(",")
    }

    class BroadcastModule(children: List<String>): Module("broadcaster", ModuleType.BROADCAST, children) {
        override fun receivingPulse(transmittingPulse: TransmittingPulse): List<TransmittingPulse> {
            return children.map { TransmittingPulse(name, it, transmittingPulse.pulse) }
        }

        override fun state() = "B"
    }

    data class TransmittingPulse(val from: String, val to: String, val pulse: Pulse)

    private fun buildGraph(modules: Map<String,Module>): Map<String, List<String>> {
        return modules.mapValues { it.value.children }.toMutableMap()
    }

    private fun prefillCaches(modules: Map<String, Module>, edges: Map<String, List<String>>) {
        for (module in modules.values.filterIsInstance<ConjunctionModule>()) {
            edges.filter { it.value.contains(module.name) }.map { it.key }.toSet().forEach {
                module.cache[it] = Pulse.LOW
            }
        }
    }

    data class PushTheButtonResult(val lowPulses: Long, val highPulses: Long)

    private fun pushTheButton(modules: Map<String, Module>, time: Int = 0, callbackFunc: (TransmittingPulse, Int) -> Unit = { _, _ -> }): PushTheButtonResult {
        val queue = LinkedList<TransmittingPulse>()
        queue.add(TransmittingPulse("buttonModule", "broadcaster", Pulse.LOW))
        var lowPulses = 0L
        var highPulses = 0L
        while (queue.isNotEmpty()) {
            val currentPulse = queue.poll()
            callbackFunc(currentPulse, time)
            if (currentPulse.pulse == Pulse.HIGH) {
                highPulses++
            } else {
                lowPulses++
            }
            if (modules.containsKey(currentPulse.to)) {
                queue.addAll(modules[currentPulse.to]!!.receivingPulse(currentPulse))
            }
        }
        return PushTheButtonResult(lowPulses, highPulses)
    }

    private fun getModules(name: String): Map<String, Module> {
        val modules = getInputAsLines(name, true)
            .map { Module.parse(it) }
            .associateBy { it.name }
        val edges = buildGraph(modules)
        prefillCaches(modules, edges)
        return modules
    }

    override fun executePart1(name: String): Any {
        val modules = getModules(name)
        val pushes = (1..1000).map {
            val (a, b) = pushTheButton(modules, it)
            a to b
        }
        return pushes.sumOf { it.first } * pushes.sumOf { it.second }
    }

    override fun executePart2(name: String): Any {
        val modules = getModules(name)

        val periods = mutableMapOf<String, Pair<Boolean, Int>>()
        // analysing the input reveals that in order for rx to receive a low pulse, hb need to send a low pulse,
        // hb sends a low pulse, if every of its inputs has sent a high pulse, which happens for every input at a certain interval
        val callbackFunc = fun(pulse: TransmittingPulse, time: Int) {
            if (pulse.to == "hb" && pulse.pulse == Pulse.HIGH) {
                if (periods.containsKey(pulse.from)) {
                    if (!periods[pulse.from]!!.first) {
                        periods[pulse.from] = true to (time - periods[pulse.from]!!.second)
                    }
                } else {
                    periods[pulse.from] = false to time
                }
            }
        }

        repeat(100_000) { pushTheButton(modules, it, callbackFunc) }
        return periods.values.map { it.second.toLong() }.reduce { a, b -> lcm(a, b) }
    }
}