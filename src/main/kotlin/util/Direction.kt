package util

enum class Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN;

    fun opposite(): Direction {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}