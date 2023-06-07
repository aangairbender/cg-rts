package com.codingame.game

sealed class Action {
    data class Move(
        val id: Int,
        val targetX: Int,
        val targetY: Int
    ) : Action()
    data class Message(val message: String) : Action()
    object Wait : Action()
}
