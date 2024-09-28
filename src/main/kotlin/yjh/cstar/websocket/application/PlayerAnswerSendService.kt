package yjh.cstar.websocket.application

import org.springframework.stereotype.Service
import yjh.cstar.websocket.application.port.AnswerMessageBroker
import yjh.cstar.websocket.domain.PlayerAnswer

@Service
class PlayerAnswerSendService(
    private val answerMessageBroker: AnswerMessageBroker,
) {

    fun send(playerAnswer: PlayerAnswer) {
        answerMessageBroker.send(playerAnswer)
    }
}
