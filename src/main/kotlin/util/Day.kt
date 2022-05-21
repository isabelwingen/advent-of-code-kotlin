package util

abstract class Day(val key: String) {
    private fun resourceFile(): String {
        return "day$key.txt"
    }

    abstract fun executePart1(name: String = resourceFile()): Any

    abstract fun expectedResultPart1(): Any

    abstract fun executePart2(name: String = resourceFile()): Any

    abstract fun expectedResultPart2(): Any

    fun executeAndCheckPart1(name: String = resourceFile()): Any {
        val res = executePart1(name)
        assert(res == expectedResultPart1())
        return res
    }

    fun executeAndCheckPart2(name: String = resourceFile()): Any {
        val res = executePart2(name)
        assert(res == expectedResultPart2())
        return res
    }
}