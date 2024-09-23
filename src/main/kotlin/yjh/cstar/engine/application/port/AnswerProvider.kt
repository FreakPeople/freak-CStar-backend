package yjh.cstar.engine.application.port

import yjh.cstar.engine.domain.quiz.PlayerAnswer

interface AnswerProvider {

    fun receivePlayerAnswer(roomId: Long, quizId: Long): PlayerAnswer?

    fun initializePlayerAnswerToReceive(roomId: Long, quizId: Long)
}
