package yjh.cstar.chat.application.port

import yjh.cstar.chat.domain.PlayerAnswer

interface AnswerMessageBroker {
    fun send(playerAnswer: PlayerAnswer)
}
