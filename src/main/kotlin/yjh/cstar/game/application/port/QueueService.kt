package yjh.cstar.game.application.port

import yjh.cstar.game.domain.AnswerResult

interface QueueService {
    fun add(answerResult: AnswerResult)
    fun poll(roomId: Long, quizId: Long): AnswerResult?
}
