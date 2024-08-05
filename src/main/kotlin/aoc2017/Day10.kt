package aoc2017

import getInput
import util.Day

class Day10: Day("10") {

    private data class Record(val list: List<Int> = IntRange(0, 255).toList(), val skip: Int = 0, val startOfList: Int = 0)

    private fun oneRun(record: Record, lengths: List<Int>): Record {
        var list = record.list
        var skip = record.skip
        var startOfList = record.startOfList
        for (l in lengths) {
            list = list.take(l).reversed() + list.drop(l)
            val startElement = list[startOfList]
            val currentPosition = (l + skip).mod(list.size)
            skip++
            list = list.drop(currentPosition) + list.take(currentPosition)
            startOfList = list.indexOf(startElement)
        }
        return Record(list, skip, startOfList)
    }

    override fun executePart1(name: String): Any {
        val lengths = getInput(name).split(",").map { Integer.valueOf(it.trim()) }
        val (resultList, _, startOfList) = oneRun(Record(), lengths)
        return resultList[startOfList] * resultList[startOfList+1]
    }

    override fun executePart2(name: String): Any {
        val lengths = getInput(name).map { it.code } + listOf(17, 31, 73, 47, 23)
        val record = (0..63).fold(Record()) { r, _ -> oneRun(r, lengths)}

        return (record.list.drop(record.startOfList) + record.list.take(record.startOfList))
            .chunked(16)
            .joinToString("") { l -> l.reduce(Int::xor).toString(16) }
    }
}