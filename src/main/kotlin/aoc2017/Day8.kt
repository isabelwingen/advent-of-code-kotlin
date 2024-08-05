package aoc2017

import getInputAsLines
import util.Day

class Day8: Day("8") {
    private enum class CalcOperator {
        INC, DEC;

        companion object {
            fun parse(str: String): CalcOperator {
                return when (str) {
                    "inc" -> INC
                    "dec" -> DEC
                    else -> throw IllegalStateException()
                }
            }
        }
    }

    private enum class ComparisonOperator {
        GT, GTE, LT, LTE, EQU, NEQ;

        fun execute(a: Int, b: Int): Boolean {
            return when (this) {
                GT -> a > b
                GTE -> a >= b
                LT -> a < b
                LTE -> a <= b
                EQU -> a == b
                NEQ -> a != b
            }
        }

        companion object {
            fun parse(str: String): ComparisonOperator {
                return when (str) {
                    ">" -> GT
                    ">=" -> GTE
                    "<" -> LT
                    "<=" -> LTE
                    "==" -> EQU
                    "!=" -> NEQ
                    else -> throw IllegalStateException()
                }
            }
        }
    }

    private data class Condition(val register: String, val op: ComparisonOperator, val value: Int) {

        fun check(registers: Map<String, Int>): Boolean {
            return op.execute(registers.getValue(register), value)
        }
    }

    private data class Instruction(val register: String, val op: CalcOperator, val value: Int, val condition: Condition) {
        fun execute(registers: MutableMap<String, Int>): Int {
            if (condition.check(registers)) {
                if (op == CalcOperator.INC) {
                    registers[register] = registers.getValue(register) + value
                } else {
                    registers[register] = registers.getValue(register) - value
                }
            }
            return registers.getValue(register)
        }
    }

    private fun parseLine(line: String): Instruction {
        val (reg1,op1,value1,_,reg2,op2,value2) = line.split(" ")
        val condition = Condition(reg2, ComparisonOperator.parse(op2), Integer.valueOf(value2))
        return Instruction(reg1, CalcOperator.parse(op1), Integer.valueOf(value1), condition)
    }

    override fun executePart1(name: String): Any {
        val instructions = getInputAsLines(name, true).map { parseLine(it) }
        val registers = mutableMapOf<String, Int>().withDefault { 0 }
        for (instruction in instructions) {
            instruction.execute(registers)
        }
        return registers.values.maxOf { it }
    }

    override fun executePart2(name: String): Any {
        val instructions = getInputAsLines(name, true).map { parseLine(it) }
        val registers = mutableMapOf<String, Int>().withDefault { 0 }
        var highestValue = 0
        for (instruction in instructions) {
            val newValue = instruction.execute(registers)
            if (newValue > highestValue) {
                highestValue = newValue
            }
        }
        return highestValue
    }
}

operator fun <T> List<T>.component6() = this[5]
operator fun <T> List<T>.component7() = this[6]