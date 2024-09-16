package yjh.cstar.engine.application

import org.springframework.stereotype.Service
import yjh.cstar.engine.domain.Ranking

@Service
class RankingService() {
    fun getRankingMessage(ranking: Ranking, nicknames: Map<Long, String>): String {
        val sortedRankingDescending = ranking.sortByScore()
        val result = StringBuilder()

        val sortedList = sortedRankingDescending.entries.sortedByDescending { it.key }
        for ((index, entry) in sortedList.withIndex()) {
            val score = entry.key
            val playerNickname = nicknames[entry.value.toLong()]
            result.append("[${index + 1}등 $playerNickname-$score]  ")
        }
        return result.toString()
    }

    fun getWinnerId(ranking: Ranking): Long {
        return ranking.getWinnerId()
    }
}
