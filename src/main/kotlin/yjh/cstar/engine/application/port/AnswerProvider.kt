package yjh.cstar.engine.application.port

import yjh.cstar.engine.domain.quiz.PlayerAnswer

interface AnswerProvider {

    fun getPlayerAnswer(roomId: Long, quizId: Long): PlayerAnswer?

    fun resetPlayerAnswer(roomId: Long, quizId: Long)
}
