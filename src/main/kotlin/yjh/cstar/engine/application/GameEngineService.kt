package yjh.cstar.engine.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.engine.application.port.GameAnswerPollRepository
import yjh.cstar.game.application.GameResultService
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.game.presentation.request.RankingCreateRequest
import yjh.cstar.game.presentation.response.QuizInfoResponse
import yjh.cstar.game.presentation.response.RankingResponse
import yjh.cstar.quiz.domain.Quiz
import yjh.cstar.websocket.application.BroadCastService
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@Service
class GameEngineService(
    private val gameAnswerPollRepository: GameAnswerPollRepository,
    private val gameResultService: GameResultService,
    private val rankingService: RankingService,
    private val answerValidationService: AnswerValidationService,
    private val broadCastService: BroadCastService,
    private val redisRankingService: RedisRankingService,
) {

    companion object {
        private const val TIME_LIMIT_MILLIS = 10000
    }

    @Async("GameEngineThreadPool")
    fun start(players: List<Long>, quizzes: List<Quiz>, roomId: Long) {
        logger.info { "[INFO] 게임 엔진 스레드 시작 - roomId : $roomId" }

        val categoryId = quizzes.firstOrNull()?.categoryId ?: throw BaseException(BaseErrorCode.EMPTY_QUIZ)

        redisRankingService.init(roomId, players)

        val nicknames = mutableMapOf<Long, String>()

        val destination = "/topic/rooms/$roomId"
        val gameStartedAt = LocalDateTime.now()
        broadCastService.sendMessage(destination, "start", "게임 시작 합니다. $roomId", null)

        broadCastService.sendMessage(destination, "guide", "GAME START!", null)
        broadCastService.sendMessage(destination, "guide", "각 문제당 ${TIME_LIMIT_MILLIS / 1000}초의 제한시간이 주어집니다.", null)

        for ((idx, quiz) in quizzes.withIndex()) {
            val quizNo = idx + 1

            broadCastService.sendMessage(
                destination,
                "quiz",
                "${quizNo}번 문제 입니다.",
                QuizInfoResponse(quiz.id, quiz.question)
            )

            val startTime = System.currentTimeMillis()
            var notExistWinner = true
            while (checkTimeIn(startTime) && notExistWinner) {
                logger.info { "[WARN] busy waiting..." }
                broadCastService.sendMessage(destination, "countdown", "", null)

                gameAnswerPollRepository.poll(roomId, quiz.id)
                    ?.takeIf {
                        answerValidationService.validateAnswer(it.answer, quiz.answer)
                    }
                    ?.let { result ->
                        redisRankingService.increaseScore(roomId, result.playerId)
                        nicknames[result.playerId] = result.nickname
                        println(nicknames)
                        broadcastResult(destination, result)

                        val ranking = redisRankingService.getRanking(roomId)
                        val rankingMessage = rankingService.getRankingMessage(ranking, nicknames)
                        broadcastRanking(destination, rankingMessage)
                        notExistWinner = false
                    }
            }

            if (!notExistWinner) continue
            broadCastService.sendMessage(destination, "guide", "시간 초과! 다음 문제로 넘어갑니다!", null)
        }

        val winningPlayerId = redisRankingService.getWinnerId(roomId) ?: -1

        broadCastService.sendMessage(destination, "guide", "문제가 다 끝났습니다. 게임 결과는?! 두구두구", null)
        println(nicknames)
        broadCastService.sendMessage(
            destination,
            "champion",
            "최종 1등은 ?! [${nicknames[winningPlayerId]}]입니다!",
            winningPlayerId
        )

        val ranking = redisRankingService.getRanking(roomId)

        val rankingCreateRequest = RankingCreateRequest(
            ranking,
            roomId,
            winningPlayerId,
            quizzes.size,
            categoryId,
            gameStartedAt
        )
        gameResultService.create(rankingCreateRequest)

        logger.info { "[INFO] 게임 엔진 스레드 종료 - roomId : $roomId" }
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

    private fun checkTimeIn(startTime: Long): Boolean {
        return System.currentTimeMillis() - startTime < TIME_LIMIT_MILLIS
    }
}
