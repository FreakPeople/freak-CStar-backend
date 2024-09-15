package yjh.cstar.game.presentation.request

import yjh.cstar.game.domain.GameStartCommand

data class GameStartRequest(
    val roomId: Long,
    val quizCategoryId: Long,
    val totalQuestions: Int,
)

fun GameStartRequest.toCommand() = GameStartCommand(
    roomId = roomId,
    quizCategoryId = quizCategoryId,
    totalQuestions = totalQuestions
)
