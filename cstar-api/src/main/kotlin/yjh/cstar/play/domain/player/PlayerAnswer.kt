package yjh.cstar.play.domain.player

import yjh.cstar.play.domain.quiz.Quiz

class PlayerAnswer(
    val answer: String,
    val quizId: Long,
    val roomId: Long,
    val playerId: Long,
    val nickname: String,
) {

    fun isCorrect(quiz: Quiz) = quiz.isSameAnswer(answer)
}
