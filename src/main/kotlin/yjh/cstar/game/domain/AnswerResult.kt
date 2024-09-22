package yjh.cstar.game.domain

import yjh.cstar.engine.domain.quiz.PlayerAnswer

class AnswerResult(
    val answer: String,
    val quizId: Long,
    val roomId: Long,
    val playerId: Long,
    val nickname: String,
)

fun AnswerResult.toPlayerAnswer(): PlayerAnswer {
    return PlayerAnswer(
        roomId = roomId,
        playerId = playerId,
        quizId = quizId,
        playerAnswer = answer
    )
}
