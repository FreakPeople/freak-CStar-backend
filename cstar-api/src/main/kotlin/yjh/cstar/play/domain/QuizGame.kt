package yjh.cstar.play.domain

import yjh.cstar.common.exception.BaseException
import yjh.cstar.common.util.TimeUtil.Companion.getCurrentLocalDateTime
import yjh.cstar.common.util.TimeUtil.Companion.getCurrentTime
import yjh.cstar.common.util.TimeUtil.Companion.getDuration
import yjh.cstar.common.util.logging.Logger
import yjh.cstar.game.application.GameResultService
import yjh.cstar.game.domain.GameResultCreateCommand
import yjh.cstar.play.application.port.AnswerProvider
import yjh.cstar.play.application.port.GameNotifier
import yjh.cstar.play.application.port.RankingHandler
import yjh.cstar.play.domain.game.GameFinalizable
import yjh.cstar.play.domain.game.GameInfo
import yjh.cstar.play.domain.game.GameInitializable
import yjh.cstar.play.domain.game.GameRunnable
import yjh.cstar.play.domain.player.Players
import yjh.cstar.play.domain.quiz.Quiz
import yjh.cstar.play.domain.quiz.RoomQuizSet
import yjh.cstar.play.domain.ranking.Ranking
import yjh.cstar.room.application.RoomService
import java.time.LocalDateTime

class QuizGame(
    private val gameInfo: GameInfo,
    private val answerProvider: AnswerProvider,
    private val gameNotifier: GameNotifier,
    private val rankingHandler: RankingHandler,
    private val gameResultService: GameResultService,
    private val roomService: RoomService,
) : GameInitializable, GameRunnable, GameFinalizable {

    private val players: Players = gameInfo.players
    private val roomQuizSet: RoomQuizSet = gameInfo.roomQuizSet
    private val roomId: Long = gameInfo.roomId
    private val categoryId: Long = gameInfo.categoryId
    private val destination: String = "/topic/rooms/$roomId"

    companion object {
        const val TIME_LIMIT_DURATION = 10000

        fun of(gameInfo: GameInfo, gameConfig: GameConfig): QuizGame {
            return QuizGame(
                gameInfo = gameInfo,
                answerProvider = gameConfig.answerProvider,
                gameNotifier = gameConfig.gameNotifier,
                rankingHandler = gameConfig.rankingHandler,
                gameResultService = gameConfig.gameResultService,
                roomService = gameConfig.roomService
            )
        }
    }

    override fun initialize() {
        rankingHandler.initRankingBoard(roomId, players)
    }

    override fun run() {
        val gameStartedAt = getCurrentLocalDateTime()

        gameNotifier.notifyGameStartComments(destination, gameInfo.roomId)

        try {
            runGameWhile({ roomQuizSet.isRunning() }) {
                val quizInfo = roomQuizSet.getNextQuizInfo()
                val (quizNo, quizId, quiz) = quizInfo

                submitQuizToPlayers(quizId, quizNo, quiz)
            }

            val winnerId: Long = findWinnerId()
            notifyWinnerInfo(winnerId)

            val ranking = rankingHandler.getRanking(roomId)
            saveGameResult(ranking, winnerId, gameStartedAt)
        } catch (e: BaseException) {
            Logger.error(e)
        } catch (e: Exception) {
            Logger.error(e.stackTraceToString())
            Logger.error("[ERROR] 프로그램 내부에 문제가 생겼습니다.")
        }
    }

    override fun finishGame() {
        roomService.endGameAndResetRoom(roomId)
    }

    private fun submitQuizToPlayers(quizId: Long, quizNo: Int, quiz: Quiz) {
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

            val playerAnswer = answerProvider.receivePlayerAnswer(
                roomId = roomId,
                quizId = quizId,
                awaitSecond = 1L
            ) ?: continue

            if (playerAnswer.isCorrect(quiz)) {
                val roundWinnerId = playerAnswer.playerId

                rankingHandler.assignScoreToRoundWinner(roomId, roundWinnerId)
                notifyRanking()

                notifyRoundResult(roundWinnerId)
                break
            }
        }
    }

    private fun runGameWhile(isRunning: () -> Boolean, action: () -> Unit) {
        while (isRunning()) {
            action()
        }
    }

    private fun notifyWinnerInfo(winnerId: Long) {
        val winnerNickname = players.getNickname(winnerId)
        gameNotifier.notifyGameResult(destination, winnerId, winnerNickname)
    }

    private fun saveGameResult(ranking: Ranking, winnerId: Long, gameStartedAt: LocalDateTime) {
        val gameResultCreateCommand = GameResultCreateCommand(
            ranking.getRanking(),
            roomId,
            winnerId,
            roomQuizSet.getSize(),
            categoryId,
            gameStartedAt
        )
        gameResultService.create(gameResultCreateCommand)
    }

    private fun notifyRoundResult(roundWinnerId: Long) =
        gameNotifier.notifyRoundResult(destination, roundWinnerId, players.getNickname(roundWinnerId))

    private fun notifyRanking() {
        val ranking = rankingHandler.getRanking(roomId)
        gameNotifier.notifyRanking(destination, players, ranking)
    }

    private fun findWinnerId(): Long =
        rankingHandler.getWinnerId(roomId)

    private fun isTimeOut(startTime: Long): Boolean =
        getDuration(startTime) >= TIME_LIMIT_DURATION
}
