package yjh.cstar.websocket.presentation

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.game.application.GameAnswerQueueService
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.websocket.presentation.request.AnswerMessageRequest

@Controller
class StompController(
    private val gameAnswerQueueService: GameAnswerQueueService,
) {

    @MessageMapping("/chatting/{roomId}")
    @SendTo("/topic/rooms/{roomId}")
    fun chatting(@DestinationVariable roomId: Long, answerMessageReuqest: AnswerMessageRequest) {
        // Authentication 에서 받아야함
        val playerId = 1L

        require(answerMessageReuqest.answer.isBlank()) { BaseException(BaseErrorCode.EMPTY_ANSWER) }
        require(answerMessageReuqest.quizId <= 0) { BaseException(BaseErrorCode.INVALID_QUIZ_ID) }

        val answer = AnswerResult(
            answer = answerMessageReuqest.answer,
            quizId = answerMessageReuqest.quizId,
            roomId = roomId,
            playerId = playerId
        )

        // 비동기 함수임
        gameAnswerQueueService.add(answer)
    }
}
