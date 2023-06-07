package com.codingame.game

import arrow.core.escaped
import arrow.core.getOrElse
import com.codingame.game.errors.InvalidInput
import com.codingame.gameengine.core.AbstractPlayer
import com.codingame.gameengine.core.AbstractReferee
import com.codingame.gameengine.core.GameManager
import com.codingame.gameengine.core.MultiplayerGameManager
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.google.inject.Inject
import com.google.inject.Singleton
import org.apache.commons.lang3.StringEscapeUtils

@Singleton
class Referee : AbstractReferee() {
    @Inject
    private lateinit var gameManager: MultiplayerGameManager<Player>
    @Inject
    private lateinit var commandManager: CommandManager
    @Inject
    private lateinit var game: Game

    override fun init(): Unit = runCatching {
        game.init()

        gameManager.run {
            frameDuration = 500
            maxTurns = 600
            turnMaxTime = 50
        }
    }.getOrElse {
        it.printStackTrace()
        System.err.println("Referee failed to initialize")
        abort()
    }

    override fun gameTurn(turn: Int) {
        game.resetGameTurnData()

        for (player in gameManager.activePlayers) {
            for (line in game.getCurrentFrameInfoFor(player)) {
                player.sendInputLine(line)
            }
            player.execute()
        }
        handlePlayerCommands()

        game.performGameUpdate(turn)

        if (gameManager.activePlayers.size < 2) {
            abort()
        }
    }

    private fun handlePlayerCommands() {
        for (player in gameManager.activePlayers) {
            val outputs = try {
                player.outputs
            } catch (e: AbstractPlayer.TimeoutException) {
                player.deactivateDueToTimeout()
                return
            }

            val actions = CommandManager.parseCommands(outputs).getOrElse {
                player.deactivateDueToInvalidInput(it)
                return
            }

            player.actions.addAll(actions)
        }
    }

    private fun abort() = gameManager.endGame()

    private fun Player.deactivateDueToTimeout() {
        deactivate("Timeout!")
        gameManager.addToGameSummary("$nicknameToken has not provided $expectedOutputLines lines in time")
    }

    private fun Player.deactivateDueToInvalidInput(error: InvalidInput) {
        deactivate(StringEscapeUtils.escapeHtml4(error.toString()))
        score = -1
        gameManager.addToGameSummary(error.toString())
        gameManager.addToGameSummary(GameManager.formatErrorMessage("$nicknameToken: disqualified!"))
    }
}
