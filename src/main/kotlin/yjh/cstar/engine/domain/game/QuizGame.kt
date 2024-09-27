package yjh.cstar.engine.domain.game

import yjh.cstar.common.BaseException
import yjh.cstar.engine.application.port.AnswerProvider
import yjh.cstar.engine.application.port.GameNotifier
import yjh.cstar.engine.application.port.RankingHandler
import yjh.cstar.engine.domain.player.Players
import yjh.cstar.engine.domain.quiz.Quizzes
import yjh.cstar.engine.domain.ranking.Ranking
import yjh.cstar.game.application.GameResultService
import yjh.cstar.game.presentation.request.RankingCreateRequest
import yjh.cstar.util.Logger
import java.time.LocalDateTime

class QuizGame(
    private val gameInfo: GameInfo,
    private val answerProvider: AnswerProvider,
    private val gameNotifier: GameNotifier,
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
        rankingHandler.initRankingBoard(roomId, players)
    }

    override fun run() {
        val gameStartedAt = getCurrentAt()
        gameNotifier.notifyGameStartComments(destination, gameInfo.roomId)

        try {
            for ((index, quiz) in quizzes.getQuizList().withIndex()) {
                val quizNo = index + 1
                val quizId = quiz.id

                answerProvider.initializePlayerAnswerToReceive(roomId, quizId)

                gameNotifier.notifyQuizQuestion(destination, quizNo, quiz)

                val roundStartTime = getCurrentTime()
                while (true) {
                    Logger.info("[INFO] 정답 대기중...")

                    gameNotifier.notifyCountdown(destination)

                    if (isTimeOut(roundStartTime)) {
                        gameNotifier.notifyTimeOut(destination)
                        break
                    }

                    /**
                     * 1초 동안 플레이어 응답을 대기합니다.(Blocking)
                     */
                    val playerAnswer = answerProvider.receivePlayerAnswer(roomId, quizId)
                        ?: continue

                    if (playerAnswer.isCorrect(quiz)) {
                        val roundWinnerId = playerAnswer.playerId

                        rankingHandler.assignScoreToPlayer(roomId, roundWinnerId)
                        notifyRanking()

                        notifyRoundResult(roundWinnerId)
                        break
                    }
                }
            }

            findAndSendWinner(players)

            recordGameResult(gameStartedAt)
        } catch (e: BaseException) {
            Logger.error(e)
        } catch (e: Exception) {
            Logger.error("[ERROR] 프로그램 내부에 문제가 생겼습니다.")
        }
    }

    private fun recordGameResult(gameStartedAt: LocalDateTime) {
        val ranking = rankingHandler.getRanking(roomId)
        saveGameResult(ranking, quizzes, gameStartedAt)
    }

    private fun saveGameResult(ranking: Ranking, quizzes: Quizzes, gameStartedAt: LocalDateTime) {
        val winnerId = findWinner()
        val rankingCreateRequest = RankingCreateRequest(
            ranking,
            roomId,
            winnerId,
            quizzes.getSize(),
            categoryId,
            gameStartedAt
        )
        gameResultService.create(rankingCreateRequest)
    }

    private fun isTimeOut(startTime: Long) = getDuration(startTime) >= TIME_LIMIT_MILLIS

    private fun findAndSendWinner(players: Players) {
        val winnerId = findWinner()
        val winnerNickname = players.getNickname(winnerId)
        gameNotifier.notifyGameResult(destination, winnerId, winnerNickname)
    }

    private fun notifyRoundResult(roundWinnerId: Long) {
        gameNotifier.notifyRoundResult(destination, roundWinnerId, players.getNickname(roundWinnerId))
    }

    private fun notifyRanking() {
        val ranking = rankingHandler.getRanking(roomId)
        gameNotifier.notifyRanking(destination, players, ranking)
    }

    private fun findWinner() = rankingHandler.getWinner(roomId)

    private fun getCurrentTime() = System.currentTimeMillis()

    private fun getCurrentAt() = LocalDateTime.now()

    private fun getDuration(pastTime: Long) = getCurrentTime() - pastTime
}
