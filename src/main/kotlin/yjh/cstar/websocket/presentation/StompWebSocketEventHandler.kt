package yjh.cstar.websocket.presentation

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.GenericMessage
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent
import yjh.cstar.auth.jwt.TokenProvider
import yjh.cstar.room.application.RoomService

private val logger = KotlinLogging.logger {}

@Component
class StompWebSocketEventHandler(
    private val roomService: RoomService,
    private val tokenProvider: TokenProvider,
) {

    companion object {
        val sessions = mutableMapOf<String, Long>() // 세션 ID : 방 ID
    }

    @EventListener
    fun handleWebSocketSessionConnectEventListener(event: SessionConnectEvent) {
        logger.info { "연결 중... 세션 ID: ${getSessionId(event)}" }
    }

    @EventListener
    fun handleWebSocketSessionConnectedEventListener(event: SessionConnectedEvent) {
        logger.info { "연결 완료. 세션 ID: ${getSessionId(event)}" }

        val session = getSessionId(event)
        val roomId = getRoomId(event)

        session?.let { sessions.put(it, roomId) }
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

        val session = getSessionId(event)

        session?.let {
            val roomId = sessions.getOrDefault(it, -1)
            sessions.remove(it)
            roomService.leave(roomId)
            logger.info { "roomService.leave() 호출" }
        }
    }

    private fun getMemberId(event: AbstractSubProtocolEvent): Long {
        val headers = getNativeHeader(event)

        val authHeader = headers["Authorization"].toString()
        val token = headers["Authorization"].toString().substring(8, authHeader.length - 1)
        return tokenProvider.getMemberId(token)
    }

    private fun getRoomId(event: AbstractSubProtocolEvent): Long {
        val headers = getNativeHeader(event)

        val rawRoomId = headers["RoomId"].toString()
        return headers["RoomId"].toString().substring(1, rawRoomId.length - 1).toLong()
    }

    private fun getNativeHeader(event: AbstractSubProtocolEvent): Map<*, *> {
        val messageHeaders: MessageHeaders = getHeader(event).messageHeaders

        val genericMessage = messageHeaders.values
            .filter { it is GenericMessage<*> }
            .first() as GenericMessage<*>

        return genericMessage.headers.values
            .filter { it is Map<*, *> && it::class.simpleName == "UnmodifiableMap" }
            .first() as Map<*, *>
    }

    private fun getHeader(event: AbstractSubProtocolEvent) = StompHeaderAccessor.wrap(event.message)

    private fun getSessionId(event: AbstractSubProtocolEvent) = getHeader(event).sessionId

    private fun getDestination(event: AbstractSubProtocolEvent) = getHeader(event).destination
}
