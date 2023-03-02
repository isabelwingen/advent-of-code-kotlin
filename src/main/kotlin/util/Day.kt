package util

abstract class Day(val key: String) {
    private fun resourceFile(): String {
        val year = this.javaClass.packageName.drop(3)
        return "$year/day$key.txt"
    }

    abstract fun executePart1(name: String = resourceFile()): Any

    abstract fun executePart2(name: String = resourceFile()): Any
}