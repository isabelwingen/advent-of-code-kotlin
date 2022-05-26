package aoc2019

import algorithm.IntCode
import asLong
import getInput
import splitBy
import util.Day

class Day17: Day("17") {
    override fun executePart1(name: String): Any {
        val prog = IntCode("Day17", name)
        val chars = mutableListOf<Char>()
        while (!prog.isHalted()) {
            chars.add(prog.execute().toInt().toChar())
        }
        val s = chars.joinToString("")

        val scaffolds = s.toList()
            .splitBy { it == '\n' }
            .filter { it.first() != '\n' }
            .map { it.toMutableList() }
            .toMutableList()

        var res = 0
        for (r in scaffolds.indices) {
            if (r == 0 || r == scaffolds.size - 1) continue
            for (c in scaffolds.first().indices) {
                if (c == 0 || c == scaffolds.first().size - 1) continue
                val left = scaffolds[r][c-1]
                val right = scaffolds[r][c+1]
                val up = scaffolds[r-1][c]
                val down = scaffolds[r+1][c]
                if (listOf(left,right,up,down).all { it == '#' } && scaffolds[r][c] == '#') {
                    scaffolds[r][c] = 'O'
                    res += c * r
                }
            }
        }

        return res

    }

    override fun expectedResultPart1() = 4220

    fun findRoboterPosition(chars: List<List<Char>>): Pair<Pair<Int, Int>, String> {
        for (r in chars.indices) {
            for (c in chars.first().indices) {
                if (setOf('<', '>', 'v', '^').contains(chars[r][c])) {
                    val dir = when (chars[r][c]) {
                        '>' -> "LEFT"
                        '<' -> "RIGHT"
                        '^' -> "UP"
                        'v' -> "DOWN"
                        else -> throw IllegalStateException()
                    }
                    return (r to c) to dir
                }
            }
        }
        return (-1 to -1) to "NO"
    }

    private fun getNeighbours(chars: List<List<Char>>, r: Int, c: Int, dir: String): Map<String, Char> {
        val m = listOf(
            "UP" to chars.getOrNull(r - 1)?.getOrNull(c),
            "DOWN" to chars.getOrNull(r + 1)?.getOrNull(c),
            "LEFT" to chars.getOrNull(r)?.getOrNull(c - 1),
            "RIGHT" to chars.getOrNull(r)?.getOrNull(c + 1)
        )
            .filter { it.second != null }
            .associate { it.first to it.second!! }
            .toMutableMap()
        when (dir) {
            "UP" -> m.remove("DOWN")
            "DOWN" -> m.remove("UP")
            "LEFT" -> m.remove("RIGHT")
            "RIGHT" -> m.remove("LEFT")
        }
        return m.toMap()
    }

    override fun executePart2(name: String): Any {
        val prog = IntCode("Day17", name)
        val chars = mutableListOf<Char>()
        while (!prog.isHalted()) {
            chars.add(prog.execute().toInt().toChar())
        }
        val s = chars.joinToString("")

        val scaffolds = s.toList()
            .splitBy { it == '\n' }
            .filter { it.first() != '\n' }
            .map { it.toMutableList() }
            .toMutableList()

        println(scaffolds.joinToString("\n") {it.joinToString("")})

        // move through scaffolds
        val instructions = mutableListOf<Any>()

        var (p, dir) = findRoboterPosition(scaffolds)
        var (r,c) = p
        var i = 0
        while (r != 46 || c != 22) {
            val neighbours = getNeighbours(scaffolds, r, c, dir).toMutableMap()

            if (neighbours[dir] == '#') {
                i += 1
                when (dir) {
                    "LEFT" -> c -= 1
                    "RIGHT" -> c += 1
                    "UP" -> r -= 1
                    "DOWN" -> r += 1
                }
            } else {
                val dirTo = neighbours.toList().first { it.second == '#' }.first
                val directionChange = when (dir to dirTo) {
                    "UP" to "LEFT" -> "L"
                    "UP" to "RIGHT" -> "R"
                    "DOWN" to "LEFT" -> "R"
                    "DOWN" to "RIGHT" -> "L"
                    "LEFT" to "UP" -> "R"
                    "LEFT" to "DOWN" -> "L"
                    "RIGHT" to "UP" -> "L"
                    "RIGHT" to "DOWN" -> "R"
                    else -> {
                        println(instructions)
                        throw IllegalStateException("Could not turn from $dir to $dirTo")
                    }
                }
                instructions.add(i.toString().toInt())
                i = 0
                instructions.add(directionChange)
                dir = dirTo
            }
        }
        instructions.add(i)

        prog.changeMemoryValue(0, 2)

        val progA = "L,6,R,8,L,4,R,8,L,12\n"
        val progB = "L,12,R,10,L,4\n"
        val progC = "L,12,L,6,L,4,L,4\n"

        val main = "A,B,B,C,B,C,B,C,A,A\n"

        repeat(5) { print(prog.execute().toInt().toChar()) }
        print(prog.execute(main.map { it.code }).toInt().toChar())
        repeat(12) { print(prog.execute().toInt().toChar()) }
        print(prog.execute(progA.map { it.code }).toInt().toChar())
        repeat(11) { print(prog.execute().toInt().toChar()) }
        print(prog.execute(progB.map { it.code }).toInt().toChar())
        repeat(11) { print(prog.execute().toInt().toChar()) }
        print(prog.execute(progC.map { it.code }).toInt().toChar())
        repeat(22) { print(prog.execute().toInt().toChar()) }
        print(prog.execute("n\n".map { it.code }).toInt().toChar())
        repeat(3000) {
            val res = prog.execute()
            if (res < 255) {
                print(res.toInt().toChar())
            } else {
                return res
            }
        }
        return 0
    }

    override fun expectedResultPart2() = 809736L
}