package yjh.cstar.websocket.presentation.request

data class AnswerMessageRequest(
    val answer: String,
    val quizId: Long,
)
