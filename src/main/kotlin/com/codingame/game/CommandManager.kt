package com.codingame.game

import arrow.core.Either
import arrow.core.raise.either
import com.codingame.game.errors.InvalidInput

object CommandManager {
    fun parseCommands(lines: List<String>): Either<InvalidInput, List<Action>> = either {
        lines.first()
            .split(';')
            .map(String::trim)
            .map { Command.parse(it).bind() }
    }
}
