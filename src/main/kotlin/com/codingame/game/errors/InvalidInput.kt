package com.codingame.game.errors

data class InvalidInput(
    val input: String,
    val expected: String,
    val throwable: Throwable? = null,
) {
    override fun toString() =
        "Invalid input: expected $expected but got '$input'"
}
