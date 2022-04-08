package algorithm

class IntCode(private val intCode: IntArray) {
    var memory = IntArray(intCode.size) { intCode[it] }
    var pointer = 0
    
    fun reset(resetValues: List<Int>) {
        pointer = 0
        memory = IntArray(intCode.size) { intCode[it] }
        for (i in resetValues.indices) {
            memory[1 + i] = resetValues[i]
        }
    }
    
    fun execute(): Int {
        while (pointer < memory.size) {
            val opCode = memory[pointer]
            if (opCode == 99) {
                break
            }
            val add1 = memory[pointer + 1]
            val add2 = memory[pointer + 2]
            val goalAdd = memory[pointer + 3]
            when (opCode) {
                1 -> memory[goalAdd] = memory[add1] + memory[add2]
                2 -> memory[goalAdd] = memory[add1] * memory[add2]
                else -> throw java.lang.IllegalStateException()
            }
            pointer += 4
        }
        return memory[0]
    }
}