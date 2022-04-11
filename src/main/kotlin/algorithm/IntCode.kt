package algorithm

import java.util.LinkedList

class IntCode(private val name: String, private val intCode: IntArray) {
    private var memory = LongArray(intCode.size) { intCode[it].toLong() }
    private var input = LinkedList<Int>()
    private var pointer = 0
    private var halt = false
    private var output = -1L

    fun init(input: List<Int> = listOf()) {
        this.input = LinkedList(input)
        this.halt = false
        pointer = 0
        memory = LongArray(intCode.size) { intCode[it].toLong() }
    }

    // param starts with 1
    private fun getValue(opCode: String, param: Int): Long {
        val mode = opCode[3 - param]
        return if (mode == '0') {
            memory[memory[pointer + param].toInt()]
        } else {
            memory[pointer + param]
        }
    }

    private fun getAddress(opCode: String, param: Int): Int {
        val mode = opCode[3 - param]
        if (mode == '0') {
            return memory[pointer + param].toInt()
        } else {
            throw java.lang.IllegalStateException("Goal must always be in position mode. Pointer: $pointer, opCode: $opCode, param: $param")
        }
    }

    private fun executeOpCode1(opCode: String) {
        val a = getValue(opCode, 1)
        val b = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        memory[goal] = a + b
        pointer += 4
    }

    private fun executeOpCode2(opCode: String) {
        val a = getValue(opCode, 1)
        val b = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        memory[goal] = a * b
        pointer += 4
    }

    private fun executeOpCode3(opCode: String) {
        val addr = getAddress(opCode, 1)
        memory[addr] = input.pop()!!.toLong()
        pointer += 2
    }

    private fun executeOpCode4(opCode: String): Long {
        output = getValue(opCode, 1)
        pointer += 2
        return output
    }

    private fun executeOpCode5(opCode: String) {
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        if (firstParam != 0L) {
            pointer = secondParam.toInt()
        } else {
            pointer += 3
        }
    }

    private fun executeOpCode6(opCode: String) {
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        if (firstParam == 0L) {
            pointer = secondParam.toInt()
        } else {
            pointer += 3
        }
    }

    private fun executeOpCode7(opCode: String) {
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        if (firstParam < secondParam) {
            memory[goal] = 1
        } else {
            memory[goal] = 0
        }
        pointer += 4
    }

    private fun executeOpCode8(opCode: String) {
        val firstParam = getValue(opCode, 1)
        val secondParam = getValue(opCode, 2)
        val goal = getAddress(opCode, 3)
        if (firstParam == secondParam) {
            memory[goal] = 1
        } else {
            memory[goal] = 0
        }
        pointer += 4
    }

    private fun executeOpCode99(): Long {
        pointer += 1
        halt = true
        return if (output == -1L) memory[0] else output
    }


    fun execute(input: Int = 0): Long {
        this.input.add(input)
        while (pointer < memory.size) {
            val opCodeAsInt = memory[pointer]
            val opCode = memory[pointer].toString().padStart(5, '0')
            println("$name: Execute opCode $opCode ($opCodeAsInt) at position $pointer")
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
                else -> throw java.lang.IllegalStateException()
            }
        }
        return memory[0]
    }

    fun isHalted() = halt
}