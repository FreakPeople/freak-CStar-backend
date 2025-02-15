package yjh.cstar.play.presentation

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Controller
import yjh.cstar.common.util.logging.Logger
import yjh.cstar.play.application.GamePlayService
import yjh.cstar.play.application.request.QuizDto

@Controller
class GamePlayEngine(
    private val gamePlayService: GamePlayService,
) {

    @Async("GamePlayThreadPool")
    fun start(players: Map<Long, String>, quizzes: List<QuizDto>, roomId: Long, categoryId: Long) {
        Logger.info("[INFO] 게임 엔진 스레드 시작 - roomId : $roomId")

        gamePlayService.play(players, quizzes, roomId, categoryId)

        Logger.info("[INFO] 게임 엔진 스레드 종료 - roomId : $roomId")
    }
}
