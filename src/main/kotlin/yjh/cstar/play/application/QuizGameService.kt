package yjh.cstar.play.application

import org.springframework.stereotype.Service
import yjh.cstar.game.application.GameResultService
import yjh.cstar.play.application.port.AnswerProvider
import yjh.cstar.play.application.port.GameNotifier
import yjh.cstar.play.application.port.RankingHandler
import yjh.cstar.play.domain.game.GameInfo
import yjh.cstar.play.domain.game.QuizGame
import yjh.cstar.play.domain.quiz.QuizDto
import yjh.cstar.room.application.RoomService

@Service
class QuizGameService(
    private val answerProvider: AnswerProvider,
    private val gameNotifier: GameNotifier,
    private val rankingHandler: RankingHandler,
    private val gameResultService: GameResultService,
    private val roomService: RoomService,
) {

    fun play(players: Map<Long, String>, quizzes: List<QuizDto>, roomId: Long, categoryId: Long) {
        val gameInfo = GameInfo.of(players, quizzes, roomId, categoryId)
        val quizGame = QuizGame(gameInfo, answerProvider, gameNotifier, rankingHandler, gameResultService, roomService)

        quizGame.initialize()
        quizGame.run()
        quizGame.finishGame()
    }
}
