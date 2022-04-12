package algorithm

import java.util.LinkedList
import kotlin.math.abs

class IntCode(private val name: String, private val intCode: IntArray) {
    private var memory = LongArray(intCode.size) { intCode[it].toLong() }
    private var input = LinkedList<Int>()
    private var pointer = 0
    private var halt = false
    private var output = -1L
    private var relativeBase = 0

    fun init(input: List<Int> = listOf()) {
        this.input = LinkedList(input)
        this.halt = false
        pointer = 0
        memory = LongArray(intCode.size) { intCode[it].toLong() }
    }

    private fun getMemory(pointer: Int): Long {
        if (pointer >= memory.size) {
            memory = LongArray(pointer + 2) { if (it < memory.size) memory[it] else 0}
        }
        return memory[pointer]
    }

    private fun setMemory(pointer: Int, value: Long) {
        if (pointer >= memory.size) {
            memory = LongArray(pointer + 2) { if (it < memory.size) memory[it] else 0}
        }
        memory[pointer] = value
    }

    // param starts with 1
    private fun getValue(opCode: String, param: Int): Long {
        return when(val mode = opCode[3 - param]) {
            '0' -> getMemory(getMemory(pointer + param).toInt())
            '1' -> getMemory(pointer + param)
            else -> throw java.lang.IllegalStateException("Illegal mode. $mode")
        }
    }

    private fun getAddress(opCode: String, param: Int): Int {
        when (opCode[3 - param]) {
            '0' -> return getMemory(pointer + param).toInt()
            else -> throw java.lang.IllegalStateException("Goal must always be in position mode. Pointer: $pointer, opCode: $opCode, param: $param")
        }
    }

    private fun executeOpCode1(opCode: String) {
        printCommand(opCode, 3)
        val a = getValue(opCode, 1)
        val b = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        println("$name   Result: Write ${a + b} to $goal*")
        setMemory(goal, a + b)
        pointer += 4
    }

    private fun executeOpCode2(opCode: String) {
        printCommand(opCode, 3)
        val a = getValue(opCode, 1)
        val b = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        println("$name   Result: Write ${a * b} to $goal*")
        setMemory(goal, a * b)
        pointer += 4
    }

    private fun executeOpCode3(opCode: String) {
        printCommand(opCode, 1)
        val addr = getAddress(opCode, 1)
        setMemory(addr, input.pop()!!.toLong())
        println("$name   Result: Write ${getMemory(addr)} to $addr*")
        pointer += 2
    }

    private fun executeOpCode4(opCode: String): Long {
        printCommand(opCode, 1)
        output = getValue(opCode, 1)
        pointer += 2
        println("$name:   Result: return $output and pause execution at $pointer")
        return output
    }

    private fun executeOpCode5(opCode: String) {
        printCommand(opCode, 2)
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        if (firstParam != 0L) {
            println("$name   Result: not zero. Set Pointer to ${secondParam.toInt()}")
            pointer = secondParam.toInt()
        } else {
            println("$name   Result: zero. Increase Pointer by 3")
            pointer += 3
        }
    }

    private fun executeOpCode6(opCode: String) {
        printCommand(opCode, 2)
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        if (firstParam == 0L) {
            println("$name   Result: zero. Set Pointer to ${secondParam.toInt()}")
            pointer = secondParam.toInt()
        } else {
            println("$name   Result: not zero. Increase Pointer by 3")
            pointer += 3
        }
    }

    private fun executeOpCode7(opCode: String) {
        printCommand(opCode, 3)
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        if (firstParam < secondParam) {
            println("$name   Result: true. Set $goal* to 1")
            setMemory(goal,1)
        } else {
            println("$name   Result: false. Set $goal* to 0")
            setMemory(goal,0)
        }
        pointer += 4
    }

    private fun executeOpCode8(opCode: String) {
        printCommand(opCode, 3)
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        if (firstParam == secondParam) {
            println("$name   Result: true. Set $goal* to 1")
            setMemory(goal,1)
        } else {
            println("$name   Result: false. Set $goal* to 0")
            setMemory(goal,0)
        }
        pointer += 4
    }

    private fun executeOpCode9(opCode: String) {
        printCommand(opCode, 1)
        val firstParam = getValue(opCode, 1)
        if (firstParam > 0) {
            println("$name   Increase relative base $relativeBase by $firstParam")
        } else {
            println("$name   Decrease relative base $relativeBase by ${abs(firstParam)}")
        }
        relativeBase += firstParam.toInt()
    }

    private fun executeOpCode99(): Long {
        pointer += 1
        halt = true
        return if (output == -1L) getMemory(0) else output
    }

    private fun printCommand(opCode: String, length: Int) {
        val params = memory.drop(pointer + 1).take(length)
        val stars = opCode.take(length).reversed()
        val paramString = params
            .mapIndexed { index, l -> if (stars[index] == '0') "$l*=${getMemory(l.toInt())}" else "$l" }
            .joinToString(", ")
        println("$name: Execute opCode $opCode($paramString) at position $pointer")
    }

    fun execute(input: Int = 0): Long {
        this.input.add(input)
        while (pointer < memory.size) {
            val opCode = getMemory(pointer).toString().padStart(5, '0')
            when {
                opCode.endsWith("99") -> return executeOpCode99()
                opCode.endsWith("1") -> executeOpCode1(opCode)
                opCode.endsWith("2") -> executeOpCode2(opCode)
                opCode.endsWith("3") -> executeOpCode3(opCode)
                opCode.endsWith("4") -> return executeOpCode4(opCode)
                opCode.endsWith("5") -> executeOpCode5(opCode)
                opCode.endsWith("6") -> executeOpCode6(opCode)
                opCode.endsWith("7") -> executeOpCode7(opCode)
                opCode.endsWith("8") -> executeOpCode8(opCode)
                opCode.endsWith("8") -> executeOpCode9(opCode)
                else -> throw java.lang.IllegalStateException()
            }
        }
        return getMemory(0)
    }

    fun isHalted() = halt
}