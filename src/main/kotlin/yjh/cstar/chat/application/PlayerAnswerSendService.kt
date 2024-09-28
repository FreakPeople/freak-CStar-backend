package yjh.cstar.chat.application

import org.springframework.stereotype.Service
import yjh.cstar.chat.application.port.AnswerMessageBroker
import yjh.cstar.chat.domain.PlayerAnswer

@Service
class PlayerAnswerSendService(
    private val answerMessageBroker: AnswerMessageBroker,
) {

    fun send(playerAnswer: PlayerAnswer) {
        answerMessageBroker.send(playerAnswer)
    }
}
