package aoc2020

import getResourceAsList
import splitBy
import java.util.LinkedList
import java.util.Queue

data class Game(val player1: Queue<Int>, val player2: Queue<Int>) {
    override fun toString() = "player1: $player1\nplayer2: $player2"
}

private fun parseInput11(name: String = "day22.txt"): Game {
    val coll = getResourceAsList(name)
        .splitBy { it.isBlank() }
        .filter { it.all { x -> x.isNotBlank() } }
        .map { it.drop(1) }
        .map { it.map { x -> x.toInt() } }

    return Game(LinkedList(coll[0]), LinkedList(coll[1]))
}

private fun roundPart1(game: Game): Game {
    val player1 = game.player1
    val player2 = game.player2
    val a = player1.poll()!!
    val b = player2.poll()!!
    return if (a > b) {
        player1.add(a)
        player1.add(b)
        Game(player1, player2)
    } else {
        player2.add(b)
        player2.add(a)
        Game(player1, player2)
    }
}

fun executeDay22Part1(name: String = "day22.txt"): Int {
    var game = parseInput11(name)
    val deckSize = game.player1.size + game.player2.size
    while (game.player1.isNotEmpty() && game.player2.isNotEmpty()) {
        game = roundPart1(game)
    }

    return listOf(game.player1.toList(), game.player2.toList())
        .first { it.isNotEmpty() }
        .mapIndexed {index, v -> (deckSize - index) * v}
        .sum()
}

private fun roundPart2(game: Game): Game {
    val a = game.player1.poll()
    val b = game.player2.poll()
    if (a <= game.player1.size && b <= game.player2.size) {
        val winner = play(
            Game(LinkedList(game.player1.toList().take(a)), LinkedList(game.player2.toList().take(b))),
            false)
        if (winner == 1) {
            game.player1.add(a)
            game.player1.add(b)
        } else {
            game.player2.add(b)
            game.player2.add(a)
        }
    } else {
        if (a > b) {
            game.player1.add(a)
            game.player1.add(b)
        } else {
            game.player2.add(b)
            game.player2.add(a)
        }
    }
    return game
}

fun Game.deepCopy(): Game {
    val p1 = LinkedList(this.player1)
    val p2 = LinkedList(this.player2)
    return Game(p1, p2)
}

private fun play(gameIn: Game, baseGame: Boolean = true): Int {
    var game = gameIn
    val previousRounds = mutableListOf<Game>()
    var breakerEnd = false
    while (game.player1.isNotEmpty() && game.player2.isNotEmpty()) {
        if (previousRounds.any { it == game }) {
            breakerEnd = true
            break
        }
        previousRounds.add(game)
        game = roundPart2(game.deepCopy())
    }
    var winningPlayer = 1
    if (!breakerEnd && game.player2.isNotEmpty()) {
        winningPlayer = 2
    }
    return if (baseGame) {
        val winningDeck = if (winningPlayer == 1) game.player1.toList() else game.player2.toList()
        winningDeck
            .mapIndexed {index, v -> (winningDeck.size - index) * v}
            .sum()
    } else {
        winningPlayer
    }
}

fun executeDay22Part2(name: String = "day22.txt"): Int {
    val game = parseInput11(name)
    return play(game)
}