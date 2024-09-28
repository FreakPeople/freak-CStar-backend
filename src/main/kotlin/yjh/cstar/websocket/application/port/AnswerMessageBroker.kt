package yjh.cstar.websocket.application.port

import yjh.cstar.websocket.domain.PlayerAnswer

interface AnswerMessageBroker {
    fun send(playerAnswer: PlayerAnswer)
}
