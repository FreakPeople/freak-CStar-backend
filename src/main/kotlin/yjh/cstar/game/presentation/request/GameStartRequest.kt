package yjh.cstar.game.presentation.request

import yjh.cstar.game.domain.GameStartCommand

data class GameStartRequest(
    val roomId: Long,
    val quizCategory: String,
    val totalQuestions: Int,
)

fun GameStartRequest.toCommand() = GameStartCommand(
    roomId = roomId,
    quizCategory = quizCategory,
    totalQuestions = totalQuestions
)
