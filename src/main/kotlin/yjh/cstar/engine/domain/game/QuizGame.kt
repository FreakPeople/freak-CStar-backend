package yjh.cstar.engine.domain.game

import io.github.oshai.kotlinlogging.KotlinLogging
import yjh.cstar.common.BaseException
import yjh.cstar.engine.domain.io.InputHandler
import yjh.cstar.engine.domain.io.OutputHandler
import yjh.cstar.engine.domain.io.RankingHandler
import yjh.cstar.engine.domain.player.Players
import yjh.cstar.engine.domain.quiz.PlayerAnswer
import yjh.cstar.engine.domain.quiz.Quiz
import yjh.cstar.engine.domain.ranking.Ranking
import yjh.cstar.game.application.GameResultService
import yjh.cstar.game.presentation.request.RankingCreateRequest
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

class QuizGame(
    private val gameInfo: GameInfo,
    private val inputHandler: InputHandler,
    private val outputHandler: OutputHandler,
    private val rankingHandler: RankingHandler,
    private val gameResultService: GameResultService,
) : GameInitializable, GameRunnable {

    private val players = gameInfo.players
    private val quizzes = gameInfo.quizzes
    private val roomId = gameInfo.roomId
    private val categoryId = gameInfo.categoryId
    private val destination = "/topic/rooms/$roomId"

    companion object {
        const val TIME_LIMIT_MILLIS = 10000 // TODO("사용자의 입력을 받도록 개선")
    }

    override fun initialize() {
        rankingHandler.init(roomId, players)
    }

    override fun run() {
        val gameStartedAt = getCurrentAt()
        outputHandler.sendGameStartComments(destination, gameInfo.roomId)

        try {
            for (idx in quizzes.indices) {
                val quiz = quizzes[idx]
                val quizNo = idx + 1
                val quizId = quiz.id

                outputHandler.resetPlayerAnswer(roomId, quizId)

                outputHandler.sendQuizQuestion(destination, quizNo, quiz)

                val roundStartTime = getCurrentTime()
                while (true) {
                    logger.info { "[INFO] 정답 대기중..." }

                    if (isTimeOut(roundStartTime)) {
                        outputHandler.sendTimeOut(destination)
                        break
                    }

                    val playerAnswer: PlayerAnswer? = inputHandler.getPlayerAnswer(roomId, quizId)
                    if (playerAnswer == null) {
                        continue
                    }

                    if (quiz.isCorrectAnswer(playerAnswer)) {
                        rankingHandler.increaseScore(roomId, playerAnswer.playerId)
                        val ranking = rankingHandler.getRanking(roomId)
                        outputHandler.sendRanking(destination, players, ranking)

                        val playerId = playerAnswer.playerId
                        outputHandler.sendRoundResult(destination, playerId, players.getNickname(playerId))
                        break
                    }
                }
            }

            findAndSendWinner(players)

            val ranking = rankingHandler.getRanking(roomId)
            saveGameResult(ranking, quizzes, gameStartedAt)
        } catch (e: BaseException) {
            logger.error { e }
        } catch (e: Exception) {
            logger.error { "[ERROR] 프로그램 내부에 문제가 생겼습니다." }
        }
    }

    private fun isTimeOut(startTime: Long) = getDuration(startTime) >= TIME_LIMIT_MILLIS

    private fun getCurrentTime() = System.currentTimeMillis()

    private fun getCurrentAt() = LocalDateTime.now()

    private fun getDuration(pastTime: Long) = getCurrentTime() - pastTime

    private fun findWinner() = rankingHandler.getWinner(roomId)

    private fun findAndSendWinner(players: Players) {
        val winnerId = findWinner()
        val winnerNickname = players.getNickname(winnerId)
        outputHandler.sendGameResult(destination, winnerId, winnerNickname)
    }

    private fun saveGameResult(ranking: Ranking, quizzes: List<Quiz>, gameStartedAt: LocalDateTime) {
        val winnerId = findWinner()
        val rankingCreateRequest = RankingCreateRequest(
            ranking,
            roomId,
            winnerId,
            quizzes.size,
            categoryId,
            gameStartedAt
        )
        gameResultService.create(rankingCreateRequest)
    }
}
