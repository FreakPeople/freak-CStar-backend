package yjh.cstar.play.domain

import yjh.cstar.common.BaseException
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
import yjh.cstar.play.domain.quiz.Quizzes
import yjh.cstar.play.domain.ranking.Ranking
import yjh.cstar.room.application.RoomService
import yjh.cstar.util.Logger
import java.time.LocalDateTime

class QuizGame(
    private val gameInfo: GameInfo,
    private val answerProvider: AnswerProvider,
    private val gameNotifier: GameNotifier,
    private val rankingHandler: RankingHandler,
    private val gameResultService: GameResultService,
    private val roomService: RoomService,
) : GameInitializable, GameRunnable, GameFinalizable {

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
                // TODO("while true 를 사용하지 않도록 수정할 것")
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

    override fun finishGame() {
        roomService.endGameAndResetRoom(roomId)
    }

    private fun recordGameResult(gameStartedAt: LocalDateTime) {
        val ranking = rankingHandler.getRanking(roomId)
        saveGameResult(ranking, quizzes, gameStartedAt)
    }

    private fun saveGameResult(ranking: Ranking, quizzes: Quizzes, gameStartedAt: LocalDateTime) {
        val winnerId = findWinner()
        val gameResultCreateCommand = GameResultCreateCommand(
            ranking.getRanking(),
            roomId,
            winnerId,
            quizzes.getSize(),
            categoryId,
            gameStartedAt
        )
        gameResultService.create(gameResultCreateCommand)
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
