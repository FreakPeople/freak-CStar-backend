package yjh.cstar.websocket.domain

class PlayerAnswer(
    val answer: String,
    val quizId: Long,
    val roomId: Long,
    val playerId: Long,
    val nickname: String,
)
