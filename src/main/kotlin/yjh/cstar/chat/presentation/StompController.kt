package yjh.cstar.chat.presentation

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.util.StringUtils
import yjh.cstar.auth.jwt.TokenProvider
import yjh.cstar.chat.application.PlayerAnswerSendService
import yjh.cstar.chat.presentation.request.AnswerMessageRequest
import yjh.cstar.chat.presentation.request.toPlayerAnswer
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException

@Controller
class StompController(
    private val playerAnswerSendService: PlayerAnswerSendService,
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

        val playerAnswer = answerMessageRequest.toPlayerAnswer(roomId, playerId)
        playerAnswerSendService.send(playerAnswer)
    }

    private fun resolveToken(bearerToken: String): String? {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}
