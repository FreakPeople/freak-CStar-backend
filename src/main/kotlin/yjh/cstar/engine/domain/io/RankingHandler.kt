package yjh.cstar.engine.domain.io

import yjh.cstar.engine.domain.player.Players
import yjh.cstar.engine.domain.ranking.Ranking

interface RankingHandler {

    fun init(roomId: Long, players: Players)

    fun increaseScore(roomId: Long, playerId: Long)

    fun getWinner(roomId: Long): Long

    fun getRanking(roomId: Long): Ranking
}
