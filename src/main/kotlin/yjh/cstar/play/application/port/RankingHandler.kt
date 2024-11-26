package yjh.cstar.play.application.port

import yjh.cstar.play.domain.player.Players
import yjh.cstar.play.domain.ranking.Ranking

interface RankingHandler {

    fun initRankingBoard(roomId: Long, players: Players)

    fun assignScoreToRoundWinner(roomId: Long, roundWinner: Long)

    fun getWinner(roomId: Long): Long

    fun getRanking(roomId: Long): Ranking
}
