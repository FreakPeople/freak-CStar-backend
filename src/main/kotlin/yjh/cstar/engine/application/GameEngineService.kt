package yjh.cstar.engine.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import yjh.cstar.engine.domain.quiz.QuizDto

private val logger = KotlinLogging.logger {}

@Service
class GameEngineService(
    private val quizGameService: QuizGameService,
) {

    @Async("GameEngineThreadPool")
    fun start(players: Map<Long, String>, quizzes: List<QuizDto>, roomId: Long, categoryId: Long) {
        logger.info { "[INFO] 게임 엔진 스레드 시작 - roomId : $roomId" }

        quizGameService.play(players, quizzes, roomId, categoryId)

        logger.info { "[INFO] 게임 엔진 스레드 종료 - roomId : $roomId" }
    }
}
