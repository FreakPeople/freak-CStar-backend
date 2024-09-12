package yjh.cstar.engine.application.port

import yjh.cstar.game.domain.AnswerResult

interface GameAnswerPollRepository {
    fun poll(roomId: Long, quizId: Long): AnswerResult?
}
