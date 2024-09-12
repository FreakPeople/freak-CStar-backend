package yjh.cstar.websocket.application

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import yjh.cstar.game.presentation.response.GameMessage

@Service
class BroadCastService(
    private val messagingTemplate: SimpMessagingTemplate,
    private val objectMapper: ObjectMapper,
) {

    fun sendMessage(destination: String, type: String, message: String, data: Any?) {
        val message = GameMessage(type, message, data)
        val jsonMessage = objectMapper.writeValueAsString(message)
        messagingTemplate.convertAndSend(destination, jsonMessage)
    }
}
