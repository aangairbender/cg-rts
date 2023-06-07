package com.codingame.game

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import com.codingame.game.errors.InvalidInput
import java.util.regex.Matcher
import java.util.regex.Pattern

enum class Command(private val key: String, private val display: String, regex: String) {
    Move(
        key = "MOVE",
        display = "MOVE <id> <targetX> <targetY>",
        regex = "^MOVE (?<id>\\d+) (?<targetX>\\d+) (?<targetY>\\d+)$",
    ) {
        override fun Matcher.toActionThrowing() = Action.Move(
            id = group("id").toInt(),
            targetX = group("targetX").toInt(),
            targetY = group("targetY").toInt()
        )
    },

    Message(
        key = "MESSAGE",
        display = "MESSAGE <message>",
        regex = "^MESSAGE (?<message>.*)$"
    ) {
        override fun Matcher.toActionThrowing() = Action.Message(
            message = group("message")
        )
    },

    Wait(
        key = "WAIT",
        display = "WAIT",
        regex = "^WAIT$"
    ) {
        override fun Matcher.toActionThrowing() = Action.Wait
    };

    private val pattern = Pattern.compile(regex)

    protected abstract fun Matcher.toActionThrowing(): Action

    companion object {
        private val COMBINED_DISPLAY = values().joinToString(" | ") { it.key }

        fun parse(input: String): Either<InvalidInput, Action> = either {
            val command = Command.values()
                .firstOrNull { it.pattern.matcher(input).matches() }

            ensureNotNull(command) { InvalidInput(input, COMBINED_DISPLAY) }

            val matcher = command.pattern.matcher(input)
            try {
                with (command) {
                    matcher.toActionThrowing()
                }
            } catch (e: Exception) {
                raise(InvalidInput(input, command.display, e))
            }
        }
    }
}