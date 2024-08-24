package yjh.cstar.websocket

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient


@ActiveProfiles("local-test")
@DisplayName("[Websocket 테스트] Websocket 연결 테스트")
@SpringBootTest
class WebSocketTest {

    @Test
    fun connect() {
        val webSocketClient = StandardWebSocketClient();
        val stompClient = WebSocketStompClient(webSocketClient).apply {
            messageConverter = MappingJackson2MessageConverter()
        }

        val url = "ws://localhost:8080/connect/websocket"
        val sessionHandler = MyStompSessionHandler()

        val sessionAsync = stompClient.connectAsync(url, sessionHandler)
        val session = sessionAsync.get()

        session.send("/chatting/666", "안녕")
    }

    class MyStompSessionHandler : StompSessionHandlerAdapter() {
        override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
            super.afterConnected(session, connectedHeaders)
        }
    }
}
