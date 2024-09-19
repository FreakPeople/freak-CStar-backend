package yjh.cstar.engine.application

import org.springframework.stereotype.Service
import yjh.cstar.engine.domain.Players

@Service
class RankingService() {
    fun getRankingMessage(ranking: List<Pair<String?, Double?>>, players: Players): String {
        val result = StringBuilder()
        for (index in ranking.indices) {
            val (player, score) = ranking[index]

            if (player == null) {
                continue
            }

            val playerId = player.split(":").get(1).toLong()
            val playerNickname = players.getNickname(playerId)
            result.append("[${index + 1}등 $playerNickname-${score?.toInt()}]  ")
        }
        return result.toString()
    }
}
