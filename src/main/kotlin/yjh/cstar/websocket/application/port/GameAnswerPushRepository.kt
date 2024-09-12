package yjh.cstar.websocket.application.port

import yjh.cstar.game.domain.AnswerResult

interface GameAnswerPushRepository {
    fun push(answerResult: AnswerResult)
}
