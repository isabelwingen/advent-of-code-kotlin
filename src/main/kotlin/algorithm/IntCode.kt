package algorithm

import getInput
import java.util.LinkedList
import kotlin.math.abs

class IntCode(private val name: String, private val intCode: LongArray) {
    private var memory = LongArray(intCode.size) { intCode[it] }
    var input = LinkedList<Int>()
    private var pointer = 0
    private var halt = false
    private var output = -1L
    private var relativeBase = 0

    override fun toString(): String {
        return memory.toList().joinToString(", ")
    }

    constructor(name: String, file: String) : this(
        name,
        getInput(file)
            .trim()
            .split(",")
            .map { it.toLong() }
            .toLongArray())

    fun init(input: List<Int> = listOf()) {
        this.input = LinkedList(input)
        this.halt = false
        pointer = 0
        memory = LongArray(intCode.size) { intCode[it] }
    }

    private fun println1(string: String) {
        //println(string)
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

    fun changeMemoryValue(address: Int, value: Long) {
        memory[address] = value
    }

    // param starts with 1
    private fun getValue(opCode: String, param: Int): Long {
        return when(val mode = opCode[3 - param]) {
            '0' -> getMemory(getMemory(pointer + param).toInt())
            '1' -> getMemory(pointer + param)
            '2' -> getMemory(getMemory(pointer + param).toInt() + relativeBase)
            else -> throw java.lang.IllegalStateException("Illegal mode. $mode")
        }
    }

    private fun getAddress(opCode: String, param: Int): Int {
        when (opCode[3 - param]) {
            '0' -> return getMemory(pointer + param).toInt()
            '2' -> return getMemory(pointer + param).toInt() + relativeBase
            else -> throw java.lang.IllegalStateException("Goal must always be in position mode. Pointer: $pointer, opCode: $opCode, param: $param")
        }
    }

    private fun executeOpCode1(opCode: String) {
        printCommand(opCode, 3)
        val a = getValue(opCode, 1)
        val b = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        println1("$name   Result: Write ${a + b} to $goal*")
        setMemory(goal, a + b)
        pointer += 4
    }

    private fun executeOpCode2(opCode: String) {
        printCommand(opCode, 3)
        val a = getValue(opCode, 1)
        val b = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        println1("$name   Result: Write ${a * b} to $goal*")
        setMemory(goal, a * b)
        pointer += 4
    }

    private fun executeOpCode3(opCode: String) {
        printCommand(opCode, 1)
        val addr = getAddress(opCode, 1)
        if (input.isEmpty()) {
            throw java.lang.IllegalArgumentException("Needs input")
        }
        setMemory(addr, input.pop()!!.toLong())
        println1("$name   Result: Write ${getMemory(addr)} to $addr*")
        pointer += 2
    }

    private fun executeOpCode4(opCode: String): Long {
        printCommand(opCode, 1)
        output = getValue(opCode, 1)
        pointer += 2
        println1("$name:   Result: return $output and pause execution at $pointer")
        return output
    }

    private fun executeOpCode5(opCode: String) {
        printCommand(opCode, 2)
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        if (firstParam != 0L) {
            println1("$name   Result: not zero. Set Pointer to ${secondParam.toInt()}")
            pointer = secondParam.toInt()
        } else {
            println1("$name   Result: zero. Increase Pointer by 3")
            pointer += 3
        }
    }

    private fun executeOpCode6(opCode: String) {
        printCommand(opCode, 2)
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        if (firstParam == 0L) {
            println1("$name   Result: zero. Set Pointer to ${secondParam.toInt()}")
            pointer = secondParam.toInt()
        } else {
            println1("$name   Result: not zero. Increase Pointer by 3")
            pointer += 3
        }
    }

    private fun executeOpCode7(opCode: String) {
        printCommand(opCode, 3)
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        if (firstParam < secondParam) {
            println1("$name   Result: true. Set $goal* to 1")
            setMemory(goal,1)
        } else {
            println1("$name   Result: false. Set $goal* to 0")
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
            println1("$name   Result: true. Set $goal* to 1")
            setMemory(goal,1)
        } else {
            println1("$name   Result: false. Set $goal* to 0")
            setMemory(goal,0)
        }
        pointer += 4
    }

    private fun executeOpCode9(opCode: String) {
        printCommand(opCode, 1)
        val firstParam = getValue(opCode, 1)
        if (firstParam > 0) {
            println1("$name   Increase relative base $relativeBase by $firstParam")
        } else {
            println1("$name   Decrease relative base $relativeBase by ${abs(firstParam)}")
        }
        relativeBase += firstParam.toInt()
        pointer += 2
    }

    private fun executeOpCode99(): Long {
        pointer += 1
        halt = true
        return if (output == -1L) getMemory(0) else output
    }

    private fun printCommand(opCode: String, length: Int) {
        println1("$name: Execute opCode $opCode at position $pointer")
    }

    fun execute(input: Int, single: Boolean = false): Long {
        this.input.add(input)
        return execute(single)
    }

    fun execute(input: List<Int>, single: Boolean = false): Long {
        this.input.addAll(input)
        return execute(single)
    }

    fun execute(single: Boolean = false): Long {
        do {
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
                opCode.endsWith("9") -> executeOpCode9(opCode)
                else -> throw java.lang.IllegalStateException()
            }
        } while (pointer < memory.size && !single)
        return -42
    }

    fun isHalted() = halt

    fun copy(): IntCode {
        val co = IntCode(name, intCode)
        co.relativeBase = relativeBase
        co.memory = memory.clone()

        return co
    }
}