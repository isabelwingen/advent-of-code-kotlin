import kotlin.math.abs

private fun parseInput(path: String): List<Pair<String, Int>> {
    return getResourceAsList(path)
        .filter { it.isNotBlank() }
        .map { it[0].toString() to it.substring(1, it.length).toInt() }
}

class Day12Part1 {

    private var orientation = "east"
    private var x = 0
    private var y = 0

    private fun moveForward(steps: Int) {
        when (orientation) {
            "east" -> x += steps
            "west" -> x -= steps
            "north" -> y += steps
            "south" -> y -= steps
        }
    }

    private fun rotate90DegreeClockwise() {
        when (orientation) {
            "east" -> orientation = "south"
            "south" -> orientation = "west"
            "west" -> orientation = "north"
            "north" -> orientation = "east"
        }
    }

    private fun rotate90DegreeAntiClockwise() {
        when (orientation) {
            "east" -> orientation = "north"
            "north" -> orientation = "west"
            "west" -> orientation = "south"
            "south" -> orientation = "east"
        }
    }

    private fun turnLeft(degrees: Int) {
        val x = (degrees / 90)
        when (x) {
            1 -> {
                rotate90DegreeAntiClockwise()
            }
            2 -> {
                rotate90DegreeAntiClockwise()
                rotate90DegreeAntiClockwise()
            }
            3 -> {
                rotate90DegreeClockwise()
            }
        }
    }

    private fun turnRight(degrees: Int) {
        val x = (degrees / 90)
        when (x) {
            1 -> {
                rotate90DegreeClockwise()
            }
            2 -> {
                rotate90DegreeAntiClockwise()
                rotate90DegreeAntiClockwise()
            }
            3 -> {
                rotate90DegreeAntiClockwise()
            }
        }
    }

    private fun executeCommand(command: Pair<String, Int>) {
        when (command.first) {
            "F" -> moveForward(command.second)
            "L" -> turnLeft(command.second)
            "R" -> turnRight(command.second)
            "N" -> y += command.second
            "S" -> y -= command.second
            "E" -> x += command.second
            "W" -> x -= command.second
        }
    }

    fun execute(): Pair<Int, Int> {
        parseInput("day12.txt").forEach { executeCommand(it) }
        return x to y
    }
}

class Day12Part2 {
    private var waypointX = 10
    private var waypointY = 1
    private var shipX = 0
    private var shipY = 0

    fun rotateClockwise(degree: Int) {
        when (degree) {
            90 -> {
                val h = waypointX
                waypointX = waypointY
                waypointY = -h
            }
            180 -> {
                waypointX *= -1
                waypointY *= -1
            }
            270 -> {
                val h = waypointX
                waypointX = -waypointY
                waypointY = h
            }
        }
    }

    fun rotateCounterClockwise(degree: Int) {
       rotateClockwise(360 - degree)
    }

    fun moveForward(times: Int) {
        shipX += times * waypointX
        shipY += times * waypointY
    }

    fun executeCommand(command: Pair<String, Int>) {
        when (command.first) {
            "N" -> waypointY += command.second
            "S" -> waypointY -= command.second
            "E" -> waypointX += command.second
            "W" -> waypointX -= command.second
            "F" -> moveForward(command.second)
            "L" -> rotateCounterClockwise(command.second)
            "R" -> rotateClockwise(command.second)
        }
    }

    fun execute(): Int {
        parseInput("day12.txt").forEach {
            run {
                println("Ship: $shipX, $shipY, Waypoint: $waypointX, $waypointY")
                executeCommand(it)
            }
        }
        println("Ship: $shipX, $shipY, Waypoint: $waypointX, $waypointY")
        return abs(shipX) + abs(shipY)
    }
}