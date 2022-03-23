package aoc2020

import getResourceAsList

private class Policy (val range: IntRange, val char: Char, val password: String)

private fun parseInput11(name: String = "day2.txt"): List<Policy> {
    return getResourceAsList(name)
        .filter { it.isNotBlank() }
        .map { transformToPolicy(it) }
}

private fun transformToPolicy(line: String): Policy {
    val coll = line.split(" ");
    val r = coll[0]
        .split("-")
        .map { Integer.parseInt(it) }

    return Policy(IntRange(r[0], r[1]), coll[1][0], coll[2])
}

private fun validForPart1(policy: Policy): Boolean {
    return policy.range.contains(policy.password.count { policy.char == it })
}

fun executeDay2Part1(name: String = "day2.txt"): Int {
    return parseInput11(name).count { validForPart1(it) }
}

private fun validForPart2(policy: Policy): Boolean {
    val a = policy.range.first
    val b = policy.range.last

    return (policy.password[a-1] == policy.char).xor(policy.password[b-1] == policy.char)
}

fun executeDay2Part2(name: String = "day2.txt"): Int {
    return parseInput11(name).count { validForPart2(it) }
}