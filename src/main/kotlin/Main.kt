import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import util.Day
import java.io.File
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.Path

fun getInput(file: String): String {
    return if (dir == "resources") {
        val dirUrl = object {}.javaClass.getResource(chosenYear)!!
        File(dirUrl.file!!, file).readText()
    } else {
        File(dir, file).readText()
    }
}

fun getInputAsLines(file: String): List<String> {
    return getInput(file).split("\n")
}

var dir = "resources"
var chosenYear = "2019"

private fun executeDay(year: String, day: String, part: String) {
    val p = object {}.javaClass.classLoader.loadClass("aoc$year.Day$day")
    val dayInstance = p.constructors[0].newInstance() as Day

    if (part == "--all") {
        val res1 = dayInstance.executePart1()
        val res2 = dayInstance.executePart2()
        println("Results of $year/$day: Part 1=$res1 and Part2=$res2")
    } else {
        val res = if (part == "1") {
             dayInstance.executePart1()
        } else {
            dayInstance.executePart2()
        }
        println("Result of $year/$day.$part is $res")
    }
}

fun main(args: Array<String>) {
    val parser = ArgParser("Advent of Code")

    val dayOptions = IntRange(1, 25).map { it.toString() }.toMutableList()
    dayOptions.add("--all")

    val year by parser.option(ArgType.Choice(listOf("2019", "2020"), { it }), shortName = "y", description = "Advent of code year").required()
    val day by parser.option(ArgType.Choice(dayOptions, { it }), shortName = "d", description = "Day of the year").required()
    val part by parser.option(ArgType.Choice(listOf("1", "2", "--all"), { it }), shortName = "p", description = "Part 1 or Part 2").required()
    val directory by parser.option(ArgType.String, shortName = "dir", description = "Absolute path to directory with resource files")
    parser.parse(args)

    if (directory != null) {
        dir = directory!!
    }
    chosenYear = year

    if (day == "--all") {
        IntRange(1, 25).forEach { executeDay(year, it.toString(), part) }
    } else {
        executeDay(year, day, part)
    }


}

