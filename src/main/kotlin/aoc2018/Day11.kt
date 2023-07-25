package aoc2018

import util.Day

class Day11: Day("11") {

    private fun p(x: Int, y: Int, k: Long): Int {
        val id = (x+10).toLong()
        val s = ((id*y)+k)*id
        return s.toString().reversed()[2].toString().toInt() - 5
    }

    private fun sumField(k: Long): Array<LongArray> {
        val res = Array(300) { LongArray(300) }
        for (y in 0..299) {
            for (x in 0..299) {
                val left = if (x == 0) 0 else res[y][x-1]
                val up = if (y == 0) 0 else res[y-1][x]
                val diagonal = if (x==0 || y==0) 0 else res[y-1][x-1]
                res[y][x] = p(x+1,y+1,k) + left + up - diagonal
            }
        }
        return res
    }

    private fun sumOfSquareFromUpperLeft(x: Int, y: Int, s: Int, field: Array<LongArray>): Long {
        val xx = x+s-1
        val yy = y+s-1
        val self = field[yy][xx]
        val up = if (y==0) 0 else field[y-1][xx]
        val left = if (x==0) 0 else field[yy][x-1]
        val diagonal = if (x==0||y==0) 0 else field[y-1][x-1]
        return self-up-left+diagonal
    }

    private data class Result(val x: Int = 0, val y: Int = 0, val size: Int = 0, val sum: Long = 0L)

    private fun maxValueForSize(size: Int, field: Array<LongArray>): Result {
        var max = Result()
        for (y in 1..301-size) {
            for (x in 1..301-size) {
                val t = sumOfSquareFromUpperLeft(x-1, y-1, size, field)
                if (t > max.sum) {
                    max = Result(x, y, size, t)
                }
            }
        }
        return max
    }

    override fun executePart1(name: String): Any {
        val field = sumField(9424)
        return maxValueForSize(3, field)
    }

    override fun executePart2(name: String): Any {
        val field = sumField(9424)
        var max = Result()
        for (size in 1..300) {
            val res = maxValueForSize(size, field)
            if (res.sum > max.sum) {
                max = res
            }
        }
        return max
    }
}