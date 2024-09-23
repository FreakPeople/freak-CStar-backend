package yjh.cstar.engine.application.port

import yjh.cstar.engine.domain.player.Players
import yjh.cstar.engine.domain.quiz.Quiz
import yjh.cstar.engine.domain.ranking.Ranking

interface GameNotifier {

    fun sendGameStartComments(destination: String, roomId: Long)

    fun sendQuizQuestion(destination: String, quizNo: Int, quiz: Quiz)

    fun sendRoundResult(destination: String, playerId: Long, nickname: String)

    fun sendRanking(destination: String, players: Players, ranking: Ranking)

    fun sendGameResult(destination: String, playerId: Long, nickname: String)

    fun sendTimeOut(destination: String)

    fun sendCountdown(destination: String)
}
