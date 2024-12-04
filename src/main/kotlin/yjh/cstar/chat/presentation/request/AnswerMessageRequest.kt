package yjh.cstar.chat.presentation.request

import yjh.cstar.chat.domain.PlayerAnswer

data class AnswerMessageRequest(
    val answer: String,
    val quizId: Long,
    val nickname: String,
)

fun AnswerMessageRequest.toPlayerAnswer(roomId: Long, playerId: Long): PlayerAnswer {
    return  PlayerAnswer(
        answer = answer,
        quizId = quizId,
        roomId = roomId,
        playerId = playerId,
        nickname = nickname
    )
}
