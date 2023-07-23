package aoc2018

import getInputAsLines
import util.Day
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Day4: Day("4") {

    private fun parseLine(line: String): Pair<LocalDateTime, String> {
        val (a,b) = line.split("]")
        val localDate = LocalDateTime.parse(a.drop(1), DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"))
        return localDate to b.drop(1)
    }

    private fun getInput(name: String): List<Pair<LocalDateTime, String>> {
        return getInputAsLines(name)
            .filter { it.isNotBlank() }
            .map { parseLine(it) }
            .sortedBy { it.first }
    }

    private fun getAsleepMap(name: String): Map<String, List<Set<Int>>> {
        val lines = getInput(name)
        val map = mutableMapOf<String, MutableList<MutableSet<Int>>>()
        var currentGuard: String? = null
        var feltAsleep = -1
        for (line in lines) {
            if (line.second.endsWith("begins shift")) {
                if (feltAsleep != -1) {
                    for (i in feltAsleep until 60) {
                        map[currentGuard]!!.last().add(i)
                    }
                    feltAsleep = -1
                }
                currentGuard = line.second.split(" ")[1]
                if (!map.containsKey(currentGuard)) {
                    map[currentGuard] = mutableListOf()
                }
                map[currentGuard]!!.add(mutableSetOf())
            } else if (line.second.endsWith("falls asleep")){
                feltAsleep = line.first.minute
            } else if (line.second.endsWith("wakes up")) {
                for (i in feltAsleep until line.first.minute) {
                    map[currentGuard]!!.last().add(i)
                }
                feltAsleep = -1
            }
        }
        return map
    }

    override fun executePart1(name: String): Long {
        val map = getAsleepMap(name)
        val mostAsleepGuard = map.maxByOrNull { it.value.sumOf { c -> c.size } }!!
        val l = IntRange(0, 59).map { mostAsleepGuard.value.count { s -> s.contains(it) } }
        return mostAsleepGuard.key.drop(1).toLong() * l.indexOf(l.maxOf { it })
    }

    override fun executePart2(name: String): Long {
        val map = getAsleepMap(name)
        val (a,b,_) = map
            .map { (k,v) -> k to IntRange(0, 59).map { v.count { s -> s.contains(it) } } }
            .map { (k,v) -> Triple(k.drop(1).toLong(), v.indexOf(v.maxOf { it }), v.maxOf { it })}
            .maxByOrNull { it.third }!!
        return a*b
    }
}