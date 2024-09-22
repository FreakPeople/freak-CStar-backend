package yjh.cstar.engine.domain.io

import yjh.cstar.engine.domain.quiz.PlayerAnswer

interface InputHandler {

    fun getPlayerAnswer(roomId: Long, quizId: Long): PlayerAnswer?
}
