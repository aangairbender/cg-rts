package com.codingame.game

import com.codingame.gameengine.core.AbstractMultiplayerPlayer

class Player : AbstractMultiplayerPlayer() {
    var resources: Int = 0
    val units = mutableListOf<GameUnit>()
    val actions = mutableListOf<Action>()
    var message: String? = null
        private set

    override fun getExpectedOutputLines() = 1

    fun reset() {
       message = null

    }
}
