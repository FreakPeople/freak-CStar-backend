package yjh.cstar.websocket.presentation

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils

@Controller
class StompController {

    @MessageMapping("/chatting/{roomId}")
    @SendTo("/topic/rooms/{roomId}")
    fun chatting(@DestinationVariable roomId: Long, chattingMessage: String) = HtmlUtils.htmlEscape(chattingMessage)
}
