package aoc2018

import util.Day

const val INPUT = 825401

class Day14: Day("14") {

    override fun executePart1(name: String): Any {
        val recipes = mutableListOf<Short>(3, 7)
        var elf1 = 0
        var elf2 = 1
        while (recipes.size < INPUT + 10) {
            "${recipes[elf1] + recipes[elf2]}"
                .map { it.toString().toShort() }
                .forEach { recipes.add(it) }
            elf1 = (elf1 + recipes[elf1] + 1) % recipes.size
            elf2 = (elf2 + recipes[elf2] + 1) % recipes.size
        }
        return recipes.drop(INPUT).take(10).joinToString("")
    }

    override fun executePart2(name: String): Any {
        val recipes = mutableListOf<Short>(3, 7)
        var elf1 = 0
        var elf2 = 1
        val limit = "$INPUT".map { it.toString().toShort() }
        var i = 0
        while (true) {
            val p = "${recipes[elf1] + recipes[elf2]}".map { it.toString().toShort() }
            println(p)
            for (recipe in p) {
                recipes.add(recipe)

                if (recipe == limit.last()) {
                    if (recipes.takeLast(limit.size) == limit) {
                        return recipes.dropLast(limit.size).count()
                    }
                }
            }
            elf1 = (elf1 + recipes[elf1] + 1) % recipes.size
            elf2 = (elf2 + recipes[elf2] + 1) % recipes.size
        }
    }
}