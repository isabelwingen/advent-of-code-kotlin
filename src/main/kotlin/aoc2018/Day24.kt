package aoc2018

import getInputAsLines
import splitBy
import util.Day
import kotlin.math.min

class Day24: Day("24") {

    data class GroupKey(val fractionType: FractionType, val id: Int)

    data class Group(
        val fractionType: FractionType,
        val id: Int,
        var units: Int,
        val hitPoints: Int,
        val attackDamage: Int,
        val attackType: Type,
        val initiative: Int,
        val weakness: Set<Type> = emptySet(),
        val immunity: Set<Type> = emptySet()
    ) {

        fun key() = GroupKey(fractionType, id)

        fun effectivePower() = attackDamage * units

        fun damageFrom(other: Group): Int {
            var damage = other.effectivePower()
            if (weakness.contains(other.attackType)) {
                damage *= 2
            } else if (immunity.contains(other.attackType)) {
                damage = 0
            }
            return damage
        }

        companion object {

            fun parse(line: String, fractionType: FractionType, id: Int): Group {
                return if (line.contains("(")) {
                    parseWithWeakness(line, fractionType, id)
                } else {
                    parseWithoutWeakness(line, fractionType, id)
                }
            }

            private fun parseWithoutWeakness(line: String, fractionType: FractionType, id: Int): Group {
                val p = line.split(" ")
                val (units, _, _, _, hitPoints) = p
                val (attackDamage, type) = p.drop(12)
                val initiative = p.last()
                return Group(fractionType, id, units.toInt(), hitPoints.toInt(), attackDamage.toInt(), Type.valueOf(type.uppercase()), initiative.toInt())
            }

            private fun parseWithWeakness(line: String, fractionType: FractionType, id: Int): Group {
                val (left, middle, right) = line.split(Regex("([()])"))
                    .map { it.trim().split(" ") }
                val (units, _, _, _, hitPoints) = left
                val (attackDamage, type) = right.drop(5)
                val initiative = right.last()

                val p = middle.joinToString(" ").split(";")
                val weakness = mutableSetOf<Type>()
                val immunity = mutableSetOf<Type>()
                if (p.size == 2) {
                    p.map { it.trim() }.first { it.startsWith("immune") }.replace(",", "").split(" ").drop(2).forEach {
                        immunity.add(Type.valueOf(it.uppercase()))
                    }
                    p.map { it.trim() }.first { it.startsWith("weak") }.replace(",", "").split(" ").drop(2).forEach {
                        weakness.add(Type.valueOf(it.uppercase()))
                    }
                } else if (p.size == 1) {
                    if (p[0].startsWith("immune")) {
                        p[0].replace(",", "").split(" ").drop(2).forEach {
                            immunity.add(Type.valueOf(it.uppercase()))
                        }
                    } else {
                        p[0].replace(",", "").split(" ").drop(2).forEach {
                            weakness.add(Type.valueOf(it.uppercase()))
                        }
                    }
                }
                return Group(
                    fractionType,
                    id,
                    units.toInt(),
                    hitPoints.toInt(),
                    attackDamage.toInt(),
                    Type.valueOf(type.uppercase()),
                    initiative.toInt(),
                    weakness.toSet(),
                    immunity.toSet())
            }
        }
    }

    enum class Type {
        BLUDGEONING,
        SLASHING,
        FIRE,
        COLD,
        RADIATION
    }

    enum class FractionType {
        IMMUNE_SYSTEM,
        INFECTION
    }

    private fun parse(name: String): List<Group> {
        return getInputAsLines(name)
            .splitBy { it.isBlank() }
            .filterNot { it.isEmpty() }
            .filterNot { it.first().isBlank() }
            .map { it.drop(1) }
            .let { (immuneSystem, infection) ->
                immuneSystem.mapIndexed { index, it -> Group.parse(it, FractionType.IMMUNE_SYSTEM, index+1) } +
                        infection.mapIndexed { index, it -> Group.parse(it, FractionType.INFECTION, index+1) }
            }
    }

    private fun fight(groups: List<Group>) {
        val attackingGroups = groups.sortedByDescending { it.initiative }.sortedByDescending { it.effectivePower() }
            .filter { it.units > 0 }
        val defendingGroups = groups.filter { it.units > 0 }.toMutableSet()
        val pairings = mutableMapOf<GroupKey, GroupKey>()
        for (attackingGroup in attackingGroups) {
            defendingGroups.asSequence()
                .filter { it != attackingGroup }
                .filter { it.fractionType != attackingGroup.fractionType }
                .sortedByDescending { it.initiative }
                .sortedByDescending { it.effectivePower() }
                .maxByOrNull { it.damageFrom(attackingGroup) }
                .let {
                    if (it != null) {
                        pairings[attackingGroup.key()] = it.key()
                        defendingGroups.remove(it)
                    }
                }
        }
        groups.sortedByDescending { it.fractionType }.forEach {
            println("${it.fractionType} ${it.id}: ${it.units} units")
        }
        println()
        pairings.forEach { (k, v) ->
            val kNode = groups.first { it.key() == k }
            val vNode = groups.first { it.key() == v }
            println("${k.fractionType} group ${k.id} would deal defending group ${v.id} ${vNode.damageFrom(kNode)} damage")
        }
        println()
        for (attackingGroup in groups.sortedByDescending { it.initiative }) {
            if (attackingGroup.units > 0 && pairings.containsKey(attackingGroup.key())) {
                val defendingGroup = groups.first { it.key() == pairings[attackingGroup.key()]!! }
                val damage = defendingGroup.damageFrom(attackingGroup)
                val unitsKilled = (damage / defendingGroup.hitPoints)
                println("${attackingGroup.fractionType} group ${attackingGroup.id} (initiative: ${attackingGroup.initiative}) attacks defending group ${defendingGroup.id}, killing ${min(unitsKilled, defendingGroup.units)} units")
                defendingGroup.units -= unitsKilled
            }
        }
        println()

    }

    override fun executePart1(name: String): Any {
        val groups = parse(name)
        while (groups.filter { it.units > 0 }.any { it.fractionType == FractionType.IMMUNE_SYSTEM } && groups.filter { it.units > 0 }.any { it.fractionType == FractionType.INFECTION }) {
            fight(groups)
        }
        return groups.filter { it.units > 0 }.map { it.units }

    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}