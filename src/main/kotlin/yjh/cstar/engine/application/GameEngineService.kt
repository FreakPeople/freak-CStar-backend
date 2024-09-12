package yjh.cstar.game.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import yjh.cstar.engine.application.port.GameAnswerPollRepository
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.game.presentation.response.QuizInfoResponse
import yjh.cstar.game.presentation.response.RankingResponse
import yjh.cstar.quiz.domain.Quiz
import yjh.cstar.websocket.application.BroadCastService
import java.time.LocalDateTime
import java.util.TreeMap

private val logger = KotlinLogging.logger {}

@Service
class GameEngineService(
    private val gameAnswerPollRepository: GameAnswerPollRepository,
    private val gameResultService: GameResultService,
    private val broadCastService: BroadCastService,
) {

    companion object {
        private const val TIME_LIMIT_MILLIS = 10000
    }

    @Async("GameEngineThreadPool")
    fun start(quizzes: List<Quiz>, roomId: Long) {
        logger.info { "[INFO] 게임 엔진 스레드 시작 - roomId : $roomId" }

        val ranking = TreeMap<Long, Int>()
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
                gameAnswerPollRepository.poll(roomId, quiz.id)
                    ?.takeIf {
                        it.answer.replace(" ", "")
                            .equals(quiz.answer.replace(" ", ""), ignoreCase = true)
                    }
                    ?.let { result ->
                        updateScore(ranking, result.playerId)
                        nicknames[result.playerId] = result.nickname
                        broadcastResult(destination, result)
                        broadcastRanking(ranking, nicknames, destination)
                        notExistWinner = false
                    }
            }

            if (!notExistWinner) {
                continue
            }
            broadCastService.sendMessage(destination, "guide", "시간 초과! 다음 문제로 넘어갑니다!", null)
        }

        val winningPlayerId = calculateGameResult(ranking)

        broadCastService.sendMessage(destination, "guide", "문제가 다 끝났습니다. 게임 결과는?! 두구두구", null)
        broadCastService.sendMessage(
            destination,
            "champion",
            "최종 1등은 ?! [${nicknames[winningPlayerId]}]입니다!",
            winningPlayerId
        )

        gameResultService.create(ranking, gameStartedAt, roomId, winningPlayerId, quizzes.size)
        logger.info { "[INFO] 게임 엔진 스레드 종료 - roomId : $roomId" }
    }

    private fun calculateGameResult(ranking: TreeMap<Long, Int>) = ranking.maxByOrNull { it.value }?.key ?: -1

    private fun broadcastResult(destination: String, result: AnswerResult) {
        broadCastService.sendMessage(destination, "winner", "[${result.nickname}]님이 맞췄습니다!", result.playerId)
    }

    private fun broadcastRanking(
        ranking: TreeMap<Long, Int>,
        nicknames: MutableMap<Long, String>,
        destination: String,
    ) {
        val sortedRankingDescending = ranking.entries.sortedByDescending { it.value }
        val result = StringBuilder()
        for ((index, entry) in sortedRankingDescending.withIndex()) {
            val playerNickname = nicknames[entry.key]
            val score = entry.value
            result.append("[${index + 1}등 $playerNickname-$score]  ")
        }
        broadCastService.sendMessage(destination, "rank", "현재 랭킹 정보 입니다.", RankingResponse(result.toString()))
    }

    private fun updateScore(ranking: TreeMap<Long, Int>, playerId: Long) {
        val score = ranking.getOrDefault(playerId, 0)
        ranking[playerId] = score + 1
    }

    private fun checkTimeIn(startTime: Long): Boolean {
        return System.currentTimeMillis() - startTime < TIME_LIMIT_MILLIS
    }
}
