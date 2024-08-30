package yjh.cstar.game.presentation.response

data class GameMessage(
    val type: String,
    val message: String,
    val data: Any?,
)
