package yjh.cstar.engine.application

import org.springframework.stereotype.Service

@Service
class RankingService() {
    fun getRankingMessage(ranking: List<Pair<String?, Double?>>, nicknames: Map<Long, String>): String {
        val result = StringBuilder()
        for (index in ranking.indices) {
            val (player, score) = ranking[index]
            val playerId = player?.takeLast(1)?.toLong()
            val playerNickname = nicknames[playerId]
            result.append("[${index + 1}등 $playerNickname-${score?.toInt()}]  ")
        }
        return result.toString()
    }
}
