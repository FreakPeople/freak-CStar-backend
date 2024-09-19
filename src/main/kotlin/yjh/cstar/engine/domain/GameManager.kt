package yjh.cstar.engine.domain

import io.github.oshai.kotlinlogging.KotlinLogging
import yjh.cstar.engine.application.AnswerValidationService
import yjh.cstar.engine.application.RankingService
import yjh.cstar.engine.application.RedisRankingService
import yjh.cstar.engine.application.port.GameAnswerPollRepository
import yjh.cstar.game.application.GameResultService
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.game.presentation.request.RankingCreateRequest
import yjh.cstar.game.presentation.response.QuizInfoResponse
import yjh.cstar.game.presentation.response.RankingResponse
import yjh.cstar.quiz.domain.Quiz
import yjh.cstar.util.RedisUtil
import yjh.cstar.websocket.application.BroadCastService

private val logger = KotlinLogging.logger {}

class GameManager(
    val players: Players,
    val quizzes: Quizzes,
    val roomId: Long,
    val categoryId: Long,
    private val gameAnswerPollRepository: GameAnswerPollRepository,
    private val gameResultService: GameResultService,
    private val rankingService: RankingService,
    private val answerValidationService: AnswerValidationService,
    private val broadCastService: BroadCastService,
    private val redisRankingService: RedisRankingService,
    private val redisUtil: RedisUtil,
) {
    val gameStartTime = StartTime()
    val destination = "/topic/rooms/$roomId"

    companion object {
        private const val TIME_LIMIT_MILLIS = 10000
    }

    fun start() {
        redisRankingService.init(roomId, players)

        sendStartMessage()

        play()

        val winnerId = redisRankingService.getWinnerId(roomId) ?: -1
        val winner = players.findWinner(winnerId)
        requireNotNull(winner) { "승리자 없음." }
        val ranking = redisRankingService.getRanking(roomId)

        sendResult(winner)
        saveResult(ranking, winnerId)
    }

    private fun sendStartMessage() {
        broadCastService.sendMessage(destination, "start", "게임 시작 합니다. $roomId", null)
        broadCastService.sendMessage(destination, "guide", "GAME START!", null)
        broadCastService.sendMessage(destination, "guide", "각 문제당 ${TIME_LIMIT_MILLIS / 1000}초의 제한시간이 주어집니다.", null)
    }

    private fun play() {
        for ((idx, quiz) in quizzes.getQuizList().withIndex()) {
            val quizNo = idx + 1
            setQuiz(quizNo, quiz)
            askQuestion(quiz)
        }
    }

    private fun setQuiz(quizNo: Int, quiz: Quiz) {
        broadCastService.sendMessage(
            destination,
            "quiz",
            "${quizNo}번 문제 입니다.",
            QuizInfoResponse(quiz.id, quiz.question)
        )
        redisUtil.delete("roomId : " + roomId + ", " + "quizId : " + quiz.id)
    }

    private fun askQuestion(quiz: Quiz) {
        val quizStartTime = StartTime()
        var notExistWinner = true

        while (quizStartTime.checkTimeOut(TIME_LIMIT_MILLIS) && notExistWinner) {
            logger.info { "[INFO] 정답 대기중..." }

            val playerAnswer: AnswerResult? = gameAnswerPollRepository.poll(roomId, quiz.id)
            if (playerAnswer == null) {
                continue
            }

            if (answerValidationService.validateAnswer(playerAnswer.answer, quiz.answer)) {
                redisRankingService.increaseScore(roomId, playerAnswer.playerId)
                players.addNickName(playerAnswer.playerId, playerAnswer.nickname)

                broadcastResult(destination, playerAnswer)

                val ranking = redisRankingService.getRanking(roomId)
                val rankingMessage = rankingService.getRankingMessage(ranking, players)
                broadcastRanking(destination, rankingMessage)
                return
            }
        }

        broadCastService.sendMessage(destination, "guide", "시간 초과! 다음 문제로 넘어갑니다!", null)
    }

    private fun sendResult(winner: Player) {
        broadCastService.sendMessage(destination, "guide", "문제가 다 끝났습니다. 게임 결과는?! 두구두구", null)
        broadCastService.sendMessage(destination, "champion", "최종 1등은 ?! [${winner.nickname}]입니다!", winner.id)
    }

    private fun saveResult(ranking: List<Pair<String?, Double?>>, winnerId: Long) {
        val rankingCreateRequest = RankingCreateRequest(
            ranking,
            roomId,
            winnerId,
            quizzes.getSize(),
            categoryId,
            gameStartTime.at
        )
        gameResultService.create(rankingCreateRequest)
    }

    private fun broadcastResult(destination: String, result: AnswerResult) {
        broadCastService.sendMessage(destination, "winner", "[${result.nickname}]님이 맞췄습니다!", result.playerId)
    }

    private fun broadcastRanking(
        destination: String,
        rankingMessage: String,
    ) {
        broadCastService.sendMessage(destination, "rank", "현재 랭킹 정보 입니다.", RankingResponse(rankingMessage))
    }
}
