package yjh.cstar.play.application.port

import yjh.cstar.play.domain.player.Players
import yjh.cstar.play.domain.ranking.Ranking

interface RankingHandler {

    fun initRankingBoard(roomId: Long, players: Players)

    fun assignScoreToPlayer(roomId: Long, playerId: Long)

    fun getWinner(roomId: Long): Long

    fun getRanking(roomId: Long): Ranking
}
