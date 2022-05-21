package aoc2020


import getInputAsLines
import util.Day

class Day2: Day("2") {

    private class Policy (val range: IntRange, val char: Char, val password: String)

    private fun parseInput(name: String): List<Policy> {
        return getInputAsLines(name)
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

    override fun executePart1(name: String): Int {
        return parseInput(name).count { validForPart1(it) }
    }

    override fun expectedResultPart1() = 614

    private fun validForPart2(policy: Policy): Boolean {
        val a = policy.range.first
        val b = policy.range.last

        return (policy.password[a-1] == policy.char).xor(policy.password[b-1] == policy.char)
    }

    override fun executePart2(name: String): Int {
        return parseInput(name).count { validForPart2(it) }
    }

    override fun expectedResultPart2() = 354
}

