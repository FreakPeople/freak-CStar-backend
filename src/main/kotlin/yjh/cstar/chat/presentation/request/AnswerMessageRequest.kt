package yjh.cstar.chat.presentation.request

import yjh.cstar.chat.domain.PlayerAnswer

data class AnswerMessageRequest(
    val answer: String,
    val quizId: Long,
    val nickname: String,
)

fun AnswerMessageRequest.toPlayerAnswer(roomId: Long, PlayerId: Long) = PlayerAnswer(
    answer,
    quizId,
    roomId,
    roomId,
    nickname
)
