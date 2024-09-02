package yjh.cstar.websocket.presentation

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.util.StringUtils
import yjh.cstar.auth.jwt.TokenProvider
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.game.application.GameAnswerQueueService
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.websocket.presentation.request.AnswerMessageRequest

@Controller
class StompController(
    private val gameAnswerQueueService: GameAnswerQueueService,
    private val tokenProvider: TokenProvider,
) {

    @MessageMapping("/chatting/{roomId}")
    @SendTo("/topic/rooms/{roomId}")
    fun chatting(
        @DestinationVariable roomId: Long,
        @Header("Authorization") bearerToken: String,
        answerMessageRequest: AnswerMessageRequest,
    ) {
        val jwt = resolveToken(bearerToken)
        val playerId = jwt
            ?.takeIf { StringUtils.hasText(it) && tokenProvider.validateToken(it) }
            ?.let { tokenProvider.getMemberId(tokenProvider.getAuthentication(it)) }

        requireNotNull(playerId) { BaseException(BaseErrorCode.UNAUTHORIZED) }
        require(answerMessageRequest.answer.isNotBlank()) { BaseException(BaseErrorCode.EMPTY_ANSWER) }
        require(answerMessageRequest.quizId > 0) { BaseException(BaseErrorCode.INVALID_QUIZ_ID) }

        val answer = AnswerResult(
            answer = answerMessageRequest.answer,
            quizId = answerMessageRequest.quizId,
            roomId = roomId,
            playerId = playerId,
            nickname = answerMessageRequest.nickname
        )

        // 비동기 함수임
        gameAnswerQueueService.add(answer)
    }

    private fun resolveToken(bearerToken: String): String? {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}
