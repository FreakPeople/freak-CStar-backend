package yjh.cstar.play.infrastructure

import org.springframework.stereotype.Service
import yjh.cstar.chat.application.BroadCastService
import yjh.cstar.game.presentation.response.QuizInfoResponse
import yjh.cstar.play.application.port.GameNotifier
import yjh.cstar.play.domain.game.QuizGame
import yjh.cstar.play.domain.player.Players
import yjh.cstar.play.domain.quiz.Quiz
import yjh.cstar.play.domain.ranking.Ranking

@Service
class WebsocketGameNotifier(
    private val broadCastService: BroadCastService,
) : GameNotifier {

    override fun notifyGameStartComments(destination: String, roomId: Long) {
        broadCastService.sendMessage(destination, "start", "게임 시작 합니다. $roomId", null)
        broadCastService.sendMessage(destination, "guide", "GAME START!", null)
        broadCastService.sendMessage(
            destination,
            "guide",
            "각 문제당 ${QuizGame.TIME_LIMIT_MILLIS / 1000}초의 제한시간이 주어집니다.",
            null
        )
    }

    override fun notifyQuizQuestion(destination: String, quizNo: Int, quiz: Quiz) {
        broadCastService.sendMessage(
            destination,
            "quiz",
            "${quizNo}번 문제 입니다.",
            QuizInfoResponse(quiz.id, quiz.question)
        )
    }

    override fun notifyRoundResult(destination: String, playerId: Long, nickname: String) {
        broadCastService.sendMessage(destination, "winner", "[$nickname]님이 맞췄습니다!", playerId)
    }

    override fun notifyRanking(destination: String, players: Players, ranking: Ranking) {
        val result = StringBuilder()

        val sortedRanking = ranking.getRanking() // "player:1 - 10"

        for ((idx, entry) in sortedRanking.entries.withIndex()) {
            val (key, score) = entry
            val playerId = key.split(":").get(1).toLong()
            val nickname = players.getNickname(playerId)
            val rank = idx + 1
            result.append("[${rank}등 $nickname-$score]  ")
        }

        broadCastService.sendMessage(destination, "rank", "현재 랭킹 정보 입니다.", result)
    }

    override fun notifyTimeOut(destination: String) {
        broadCastService.sendMessage(destination, "guide", "시간 초과! 다음 문제로 넘어갑니다!", null)
    }

    override fun notifyGameResult(destination: String, playerId: Long, nickname: String) {
        broadCastService.sendMessage(destination, "champion", "최종 1등은 ?! [$nickname]입니다!", playerId)
    }

    override fun notifyCountdown(destination: String) {
        broadCastService.sendMessage(destination, "countdown", "1초 경과", null)
    }
}
