package yjh.cstar.chat

import org.junit.jupiter.api.Disabled
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
@DisplayName("[Chat 테스트] Chat 연결 테스트")
@SpringBootTest
class ChatTest {

    /**
     * 웹소켓 연결 할 서버가 켜져있지 않으면(8080) 동작하지 않는 테스트이므로 @Disabled 처리를 해주었습니다.
     */
    @Disabled
    @Test
    fun connect() {
        val webSocketClient = StandardWebSocketClient()
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
