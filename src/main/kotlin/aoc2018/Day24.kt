package aoc2018

import getInputAsLines
import splitBy
import util.Day
import kotlin.math.min

class Day24: Day("24") {


    data class GroupKey(val fractionType: FractionType, val id: Int) {
        override fun toString() = "${fractionType.name.lowercase()} $id"
    }

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

        override fun toString(): String {
            return if (units > 0) {
                "$fractionType $id: $units Units (h:$hitPoints) (a:$attackDamage/$attackType/$initiative) (w:${weakness.joinToString(",")}/im:${immunity.joinToString(",")}), "
            } else {
                "$fractionType $id: dead"
            }
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

    private fun my_println() {
        //println()
    }

    private fun my_println(str: String) {
        //println(str)
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

   

    private fun selectionOrder(groups: Map<GroupKey, Group>): List<GroupKey> {
        return groups.entries
            .sortedByDescending { it.value.initiative }
            .sortedByDescending { it.value.effectivePower() }
            .map { it.key }
    }

    private fun attackingOrder(groups: Map<GroupKey, Group>): List<GroupKey> {
        return groups.entries.sortedByDescending { it.value.initiative }.map { it.key }
    }

    private fun findTarget(attacker: Group, groups: Map<GroupKey, Group>): GroupKey? {
        var possibleTargets = groups.entries.filter { it.value.fractionType != attacker.fractionType }
        if (possibleTargets.isEmpty()) {
            return null
        }
        val highestDamage = possibleTargets.maxOf { it.value.damageFrom(attacker) }
        if (highestDamage == 0) {
            return null
        }
        possibleTargets = possibleTargets.filter { it.value.damageFrom(attacker) == highestDamage }
        val largestEffectivePower = possibleTargets.maxOf { it.value.effectivePower() }
        possibleTargets = possibleTargets.filter { it.value.effectivePower() == largestEffectivePower }
            .sortedByDescending { it.value.initiative }
        return possibleTargets.first().key
    }

    private fun targetSelectionPhase(groups: Map<GroupKey, Group>): Map<GroupKey, GroupKey?> {
        val targetMap = mutableMapOf<GroupKey, GroupKey?>()
        val remainingGroups =  groups.toMutableMap()
        for (attackerKey in selectionOrder(groups)) {
            val attacker = groups.getValue(attackerKey)
            val target = findTarget(attacker, remainingGroups)
            targetMap[attackerKey] = target
            if (target != null) {
                //my_println("Attacking group $attackerKey chooses target group $target")
                remainingGroups.remove(target)
            } else {
                my_println("Attacking group $attackerKey cannot deal damage to any group")
            }
        }
        return targetMap.toMap()
    }

    private fun dealDamage(attacker: Group, defender: Group): Boolean {
        val deadUnits = defender.damageFrom(attacker) / defender.hitPoints
        my_println("${attacker.key()} attacks defending group ${defender.key()}, killing ${min(deadUnits, defender.units)} units")
        defender.units -= deadUnits
        if (defender.units <= 0) {
            my_println("Defender group ${defender.key()} died through the attack")
        }
        return defender.units <= 0
    }

    private fun fight(groups: MutableMap<GroupKey, Group>) {
        val targetMap = targetSelectionPhase(groups)
        my_println()
        for (attackerKey in attackingOrder(groups)) {
            val attacker = groups[attackerKey]
            val targetKey = targetMap[attackerKey]
            if (attacker == null) {
                my_println("Attacking group $attackerKey died before it could attack")
            } else if (targetKey == null) {
                my_println("Attacking group $attackerKey has no target")
            } else if (groups[targetKey] == null) {
                my_println("Attacking group $attackerKey: target already dead")
            } else {
                val dead = dealDamage(attacker, groups[targetKey]!!)
                if (dead) {
                    groups.remove(targetKey)
                }
            }
        }
    }

    private fun combat(name: String, boost: Int = 0): Pair<FractionType, Long> {
        val groups = parse(name)
            .map {
                if (it.fractionType == FractionType.IMMUNE_SYSTEM) {
                    it.copy(attackDamage = it.attackDamage + boost)
                } else {
                    it
                }
            }
            .associateBy { it.key() }.toMutableMap()
        attackingOrder(groups).forEach { key ->
            val group = groups.getValue(key)
            my_println("${key}: ${group.units} units, ${group.effectivePower()} effective power, ${group.initiative}, initiative")
        }
        my_println()
        var i = 0
        while (any(FractionType.IMMUNE_SYSTEM, groups) && any(FractionType.INFECTION, groups)) {
            my_println()
            my_println("==== Round ${++i} ====")
            val unitsBefore = groups.values.sumOf { it.units.toLong() }
            fight(groups)
            val unitsAfter = groups.values.sumOf { it.units.toLong() }
            if (unitsBefore == unitsAfter) { // TIE break
                println("Tie break: $i")
                return FractionType.INFECTION to groups.values.filter { it.fractionType == FractionType.INFECTION }.sumOf { it.units.toLong() }
            }
        }
        return groups.values.first().fractionType to groups.values.sumOf { it.units.toLong() }
    }

    private fun any(fractionType: FractionType, groups: Map<GroupKey, Group>): Boolean {
        return groups.values.any { it.fractionType == fractionType }
    }

    override fun executePart1(name: String): Any {
        val (_, unitsRemaining) = combat(name)
        return unitsRemaining
    }

    override fun executePart2(name: String): Any {
        var lower = 0
        var upper = 10_000
        println("upper limit winner: ${combat(name, upper)}")
        while (lower != upper) {
            val middle = (lower + upper) / 2
            println("lower: $lower, upper: $upper, testing $middle...")
            val (winningFraction, _)  = combat(name, middle)
            println("$middle: $winningFraction")
            if (winningFraction == FractionType.INFECTION) {
                lower = middle + 1
            } else {
                upper = middle
            }
        }
        return combat(name, lower)
    }
}