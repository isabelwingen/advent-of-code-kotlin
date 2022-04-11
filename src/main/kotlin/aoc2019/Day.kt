package aoc2019

interface Day {
    fun resourceFile(): String {
        return "2019/day${key()}.txt"
    }
    fun executePart1(name: String = resourceFile()): Any
    fun expectedResultPart1(): Any
    fun executePart2(name: String = resourceFile()): Any
    fun expectedResultPart2(): Any

    fun key(): String

}