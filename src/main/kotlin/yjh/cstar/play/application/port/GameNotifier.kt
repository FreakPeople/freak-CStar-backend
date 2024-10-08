package yjh.cstar.play.application.port

import yjh.cstar.play.domain.player.Players
import yjh.cstar.play.domain.quiz.Quiz
import yjh.cstar.play.domain.ranking.Ranking

interface GameNotifier {

    fun notifyGameStartComments(destination: String, roomId: Long)

    fun notifyQuizQuestion(destination: String, quizNo: Int, quiz: Quiz)

    fun notifyRoundResult(destination: String, playerId: Long, nickname: String)

    fun notifyRanking(destination: String, players: Players, ranking: Ranking)

    fun notifyGameResult(destination: String, playerId: Long, nickname: String)

    fun notifyTimeOut(destination: String)

    fun notifyCountdown(destination: String)
}
