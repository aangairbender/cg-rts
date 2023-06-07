package com.codingame.game

import com.codingame.gameengine.core.MultiplayerGameManager
import com.google.inject.Inject
import com.google.inject.Singleton
import java.util.Random

@Singleton
class Game {
    @Inject
    private lateinit var gameManager: MultiplayerGameManager<Player>

    private lateinit var players: List<Player>
    private lateinit var random: Random
    private lateinit var grid: Grid

    private var gameTurn: Int = 0
    private var nextId = 0

    fun init() {
        players = gameManager.players
        random = gameManager.random
        grid = Grid(random, players)
        gameTurn = 0
        initPlayers()
    }

    private fun initPlayers() {
        for (player in players) {
            player.resources = Config.PLAYER_STARTING_RESOURCES
        }
        // poc code, will be replaced
        players[0].units.add(GameUnit(
            id = generateId(),
            owner = players[0],
            type = GameUnitType.Worker,
            position = Coord(0, 0),
        ))
        players[1].units.add(GameUnit(
            id = generateId(),
            owner = players[1],
            type = GameUnitType.Worker,
            position = Coord(Config.MAP_WIDTH - 1, Config.MAP_HEIGHT - 1),
        ))
    }

    fun resetGameTurnData() {
        players.forEach(Player::reset)
    }

    fun getCurrentFrameInfoFor(player: Player) = buildList {
        // resources
        add("0")
        // units
        add(player.units.size.toString())
        for (unit in player.units) {
            add("${unit.id} ${unit.position.x} ${unit.position.y} 0 ${unit.type} 10")
        }
        // buildings
        add("0")
    }

    fun performGameUpdate(frameIdx: Int) {
        gameTurn++
        // TODO: implement
    }

    private fun generateId() = nextId++
}
