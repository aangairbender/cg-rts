package com.codingame.game

class GameUnit(
    val id: Int,
    val owner: Player,
    val type: GameUnitType,
    val position: Coord,
)

enum class GameUnitType {
    Worker,
    Fighter
}