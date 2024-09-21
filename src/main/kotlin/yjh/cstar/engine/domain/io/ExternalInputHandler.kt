package yjh.cstar.engine.domain.io

import org.springframework.stereotype.Service
import yjh.cstar.engine.application.port.GameAnswerPollRepository
import yjh.cstar.engine.domain.quiz.PlayerAnswer
import yjh.cstar.game.domain.toPlayerAnswer

@Service
class ExternalInputHandler(
    val gameAnswerPollRepository: GameAnswerPollRepository,
) : InputHandler {

    override fun getPlayerAnswer(roomId: Long, quizId: Long): PlayerAnswer? {
        return gameAnswerPollRepository.poll(roomId, quizId)?.toPlayerAnswer()
    }
}
