package yjh.cstar.engine.application

import org.springframework.stereotype.Service
import yjh.cstar.engine.application.port.AnswerProvider
import yjh.cstar.engine.application.port.GameNotifier
import yjh.cstar.engine.application.port.RankingHandler
import yjh.cstar.engine.domain.game.GameInfo
import yjh.cstar.engine.domain.game.QuizGame
import yjh.cstar.engine.domain.quiz.QuizDto
import yjh.cstar.game.application.GameResultService

@Service
class QuizGameService(
    private val answerProvider: AnswerProvider,
    private val gameNotifier: GameNotifier,
    private val rankingHandler: RankingHandler,
    private val gameResultService: GameResultService,
) {

    fun play(players: Map<Long, String>, quizzes: List<QuizDto>, roomId: Long, categoryId: Long) {
        val gameInfo = GameInfo.of(players, quizzes, roomId, categoryId)

        val quizGame = QuizGame(gameInfo, answerProvider, gameNotifier, rankingHandler, gameResultService)

        quizGame.initialize()

        quizGame.run()
    }
}
