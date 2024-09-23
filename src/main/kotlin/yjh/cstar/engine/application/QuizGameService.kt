package yjh.cstar.engine.application

import org.springframework.stereotype.Service
import yjh.cstar.engine.domain.game.GameInfo
import yjh.cstar.engine.domain.game.QuizGame
import yjh.cstar.engine.domain.io.InputHandler
import yjh.cstar.engine.domain.io.OutputHandler
import yjh.cstar.engine.domain.io.RankingHandler
import yjh.cstar.engine.domain.quiz.QuizDto
import yjh.cstar.game.application.GameResultService

@Service
class QuizGameService(
    private val inputHandler: InputHandler,
    private val outputHandler: OutputHandler,
    private val rankingHandler: RankingHandler,
    private val gameResultService: GameResultService,
) {

    fun play(players: Map<Long, String>, quizzes: List<QuizDto>, roomId: Long, categoryId: Long) {
        val gameInfo = GameInfo.of(players, quizzes, roomId, categoryId)
        val quizGame = QuizGame(gameInfo, inputHandler, outputHandler, rankingHandler, gameResultService)

        quizGame.initialize()
        quizGame.run()
    }
}
