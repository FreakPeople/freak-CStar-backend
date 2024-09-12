package yjh.cstar.engine.application

import org.springframework.stereotype.Service
import yjh.cstar.engine.application.port.GameAnswerPollRepository
import yjh.cstar.game.domain.AnswerResult

@Service
class GameAnswerPollService(
    private val gameAnswerPollRepository: GameAnswerPollRepository,
) {
    /**
     * 정답을 메세지 브로커에 전달한다.
     */
    fun poll(roomId: Long, quizId: Long): AnswerResult? {
        return gameAnswerPollRepository.poll(roomId, quizId)
    }
}
