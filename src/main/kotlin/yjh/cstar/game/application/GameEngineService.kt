package yjh.cstar.game.application

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.quiz.domain.Quiz
import java.time.LocalDateTime
import java.util.TreeMap

@Service
class GameEngineService(
    private val messagingTemplate: SimpMessagingTemplate,
    private val gameAnswerQueueService: GameAnswerQueueService,
    private val gameResultService: GameResultService,
) {

    companion object {
        private const val TIME_LIMIT = 10
    }

    private val ranking = TreeMap<Long, Int>()

    @Async("GameEngineThreadPool")
    fun start(quizzes: List<Quiz>, roomId: Long) {
        val destination = "/topic/rooms/$roomId"
        val gameStartedAt = LocalDateTime.now()

        messagingTemplate.convertAndSend(destination, "GAME START!")
        messagingTemplate.convertAndSend(destination, "각 문제당 ${TIME_LIMIT}초의 제한시간이 주어집니다.")

        for ((idx, quiz) in quizzes.withIndex()) {
            val quizNo = idx + 1
            messagingTemplate.convertAndSend(destination, "문제$quizNo : ${quiz.question}")

            val startTime = System.currentTimeMillis()
            var notExistWinner = true
            while (checkTimeIn(startTime) && notExistWinner) {
                gameAnswerQueueService.poll(roomId, quiz.id)
                    ?.takeIf { it.answer == quiz.answer }
                    ?.let { result ->
                        updateScore(result.playerId)
                        broadcastResult(destination, result)
                        broadcastRanking(destination)
                        notExistWinner = false
                    }
            }

            if (!notExistWinner) {
                continue
            }
            messagingTemplate.convertAndSend(destination, "시간 초과! 다음 문제로 넘어갑니다!")
        }

        val winningPlayerId = calculateGameResult()
        messagingTemplate.convertAndSend(destination, "문제가 다 끝났습니다. 게임 결과는?! 두구두구")
        messagingTemplate.convertAndSend(destination, "최종 1등은 ?! ${winningPlayerId}입니다!")

        gameResultService.create(ranking, gameStartedAt, roomId, winningPlayerId, quizzes.size)
    }

    private fun calculateGameResult() = ranking.maxByOrNull { it.value }?.key
        ?: throw BaseException(BaseErrorCode.INTERNAL_SERVER_ERROR)

    private fun broadcastResult(destination: String, result: AnswerResult) {
        messagingTemplate.convertAndSend(destination, "${result.playerId}님이 맞췄습니다!")
    }

    private fun broadcastRanking(destination: String) {
        messagingTemplate.convertAndSend(destination, ranking)
    }

    private fun updateScore(playerId: Long) {
        val score = ranking.getOrDefault(playerId, 0)
        ranking[playerId] = score + 1
    }

    private fun checkTimeIn(startTime: Long): Boolean {
        return System.currentTimeMillis() - startTime < TIME_LIMIT
    }
}
