package aoc2019

import aoc2019.util.IntCode
import util.Day

class Day25: Day("25") {

    private fun executeUntilNewPrompt(prog: IntCode, command: String? = null): Long {
        val text = mutableListOf<Char>()
        if (command != null) {
            print(" => $command: ")
            val res = prog.execute("$command\n".toCharArray().toList().map { it.code.toLong() }).toInt().toChar()
            text.add(res)
        }
        while (true) {
            val res = prog.execute().toInt().toChar()
            text.add(res)
            print(res)
            if (text.size > 9) {
                val y = text.subList(text.size - 9, text.size).joinToString("")
                if (y.endsWith("Command?\n")) {
                    return 0L
                }
                if (y.endsWith("airlock.")) {
                    val s = text.joinToString("").split(" ")
                    println()
                    return s[s.size-8].toLong()
                }
            }
        }
    }

    override fun executePart1(name: String): Long {
        val prog = IntCode("25", "2019/day25.txt")
        executeUntilNewPrompt(prog)
        // go south to science lab
        executeUntilNewPrompt(prog, "south")
        executeUntilNewPrompt(prog, "take weather machine")
        executeUntilNewPrompt(prog, "north") // back to the start
        // go west to passages
        executeUntilNewPrompt(prog, "west")
        executeUntilNewPrompt(prog, "west")
        //(executeUntilNewPrompt(prog, "take giant electromagnet")) --> stuck
        executeUntilNewPrompt(prog, "north")
        executeUntilNewPrompt(prog, "take space heater")
        executeUntilNewPrompt(prog, "south")
        executeUntilNewPrompt(prog, "east")
        executeUntilNewPrompt(prog, "east") // back to the start
        //go west to storage and arcade
        executeUntilNewPrompt(prog, "west")
        executeUntilNewPrompt(prog, "south")
        executeUntilNewPrompt(prog, "take festive hat")
        executeUntilNewPrompt(prog, "south")
        executeUntilNewPrompt(prog, "take sand")
        executeUntilNewPrompt(prog, "north")
        executeUntilNewPrompt(prog, "east")
        executeUntilNewPrompt(prog, "take whirled peas")
        executeUntilNewPrompt(prog, "west")
        executeUntilNewPrompt(prog, "north")
        executeUntilNewPrompt(prog, "east") // back to start
        // go east to corridor
        executeUntilNewPrompt(prog, "east") // Now in Engineering
        executeUntilNewPrompt(prog, "take mug")
        executeUntilNewPrompt(prog, "east")
        //(executeUntilNewPrompt(prog, "take escape pod")) // You're launched into space! Bye!
        executeUntilNewPrompt(prog, "south")
        executeUntilNewPrompt(prog, "east")
        executeUntilNewPrompt(prog, "south")
        executeUntilNewPrompt(prog, "take easter egg")
        // go north to kitchen --> there is nothing
        executeUntilNewPrompt(prog, "north")
        executeUntilNewPrompt(prog, "north")
        // (executeUntilNewPrompt(prog, "take molten lava")) // no no no
        executeUntilNewPrompt(prog, "west")
        executeUntilNewPrompt(prog, "east")
        executeUntilNewPrompt(prog, "south")
        executeUntilNewPrompt(prog, "west")
        executeUntilNewPrompt(prog, "west")
        executeUntilNewPrompt(prog, "south")
        executeUntilNewPrompt(prog, "west")
        executeUntilNewPrompt(prog, "take shell")
        executeUntilNewPrompt(prog, "south")
        executeUntilNewPrompt(prog, "inv")
        executeUntilNewPrompt(prog, "drop whirled peas")
        executeUntilNewPrompt(prog, "drop weather machine")
        executeUntilNewPrompt(prog, "drop festive hat")
        executeUntilNewPrompt(prog, "drop shell")
        return executeUntilNewPrompt(prog, "south")
    }

    override fun executePart2(name: String): Any {
        TODO("Not yet implemented")
    }
}