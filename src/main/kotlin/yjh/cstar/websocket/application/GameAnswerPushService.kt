package yjh.cstar.websocket.application

import org.springframework.stereotype.Service
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.websocket.application.port.GameAnswerPushRepository

@Service
class GameAnswerPushService(
    private val gameAnswerPushRepository: GameAnswerPushRepository,
) {

    fun push(answerResult: AnswerResult) {
        gameAnswerPushRepository.push(answerResult)
    }
}
