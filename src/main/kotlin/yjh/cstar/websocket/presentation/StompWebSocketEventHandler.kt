package yjh.cstar.websocket.presentation

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent

private val logger = KotlinLogging.logger {}

@Component
class StompWebSocketEventHandler {

    @EventListener
    fun handleWebSocketSessionConnectEventListener(event: SessionConnectEvent) {
        logger.info { "연결 중... 세션 ID: ${getSessionId(event)}" }
    }

    @EventListener
    fun handleWebSocketSessionConnectedEventListener(event: SessionConnectedEvent) {
        logger.info { "연결 완료. 세션 ID: ${getSessionId(event)}" }
    }

    @EventListener
    fun handleWebSocketSessionSubscribeEventListener(event: SessionSubscribeEvent) {
        logger.info { "구독 : ${getDestination(event)}" }
    }

    @EventListener
    fun handleWebSocketSessionUnsubscribeEventListener(event: SessionUnsubscribeEvent) {
        logger.info { "구독 취소 : ${getDestination(event)}" }
    }

    @EventListener
    fun handleWebSocketSessionDisconnectEventListener(event: SessionDisconnectEvent) {
        logger.info { "연결 종료" }
    }

    private fun getHeader(event: AbstractSubProtocolEvent) = StompHeaderAccessor.wrap(event.message)

    private fun getSessionId(event: AbstractSubProtocolEvent) = getHeader(event).sessionId

    private fun getDestination(event: AbstractSubProtocolEvent) = getHeader(event).destination
}

