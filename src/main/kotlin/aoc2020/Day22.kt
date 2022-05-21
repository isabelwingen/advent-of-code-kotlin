package aoc2020

import getInputAsLines
import splitBy
import util.Day
import java.util.LinkedList
import java.util.Queue

data class Game(val player1: Queue<Int>, val player2: Queue<Int>) {
    override fun toString() = "player1: $player1\nplayer2: $player2"
}

class Day22 : Day("22") {

    private fun parseInput(name: String): Game {
        val coll = getInputAsLines(name)
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

    override fun executePart1(name: String): Int {
        var game = parseInput(name)
        while (game.player1.isNotEmpty() && game.player2.isNotEmpty()) {
            game = roundPart1(game)
        }

        val res = game.player1.ifEmpty { game.player2 }.toList()
        return res.mapIndexed { index, v -> (res.size - index) * v }.sum()
    }

    override fun expectedResultPart1() = 34127

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

    private fun play(gameIn: Game, baseGame: Boolean = true): Int {
        var game = gameIn
        val previousRounds = mutableSetOf<List<Int>>()
        var breakerEnd = false
        while (game.player1.isNotEmpty() && game.player2.isNotEmpty()) {
            val gameAsList = game.player2.toList()
            if (previousRounds.any { it == gameAsList }) {
                breakerEnd = true
                break
            }
            previousRounds.add(gameAsList)
            game = roundPart2(Game(LinkedList(game.player1), LinkedList(game.player2)))
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

    override fun executePart2(name: String): Int {
        val game = parseInput(name)
        return play(game)
    }

    override fun expectedResultPart2() = 32054
}