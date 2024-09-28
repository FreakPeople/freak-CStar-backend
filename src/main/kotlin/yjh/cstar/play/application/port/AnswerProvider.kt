package yjh.cstar.play.application.port

import yjh.cstar.play.domain.quiz.PlayerAnswer

interface AnswerProvider {

    fun receivePlayerAnswer(roomId: Long, quizId: Long): PlayerAnswer?

    fun initializePlayerAnswerToReceive(roomId: Long, quizId: Long)
}
