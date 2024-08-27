package yjh.cstar.game.application

import org.springframework.stereotype.Service
import yjh.cstar.game.application.port.QueueService
import yjh.cstar.game.domain.AnswerResult

@Service
class GameAnswerQueueService(
    private val queueService: QueueService,
) {
    /**
     * 플레이어의 정답을 add을 통해 제출하고, 순차적으로 저장한다.
     */
    fun add(answerResult: AnswerResult) {
        queueService.add(answerResult)
    }

    /**
     * 순차적으로 저장된 정답을,하나씩 꺼내온다.
     */
    fun poll(roomId: Long, quizId: Long): AnswerResult? {
        return queueService.poll(roomId, quizId)
    }
}
