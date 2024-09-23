package yjh.cstar.engine.application.port

import yjh.cstar.engine.domain.player.Players
import yjh.cstar.engine.domain.ranking.Ranking

interface RankingHandler {

    fun initRankingBoard(roomId: Long, players: Players)

    fun assignScoreToPlayer(roomId: Long, playerId: Long)

    fun getWinner(roomId: Long): Long

    fun getRanking(roomId: Long): Ranking
}
