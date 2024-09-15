package yjh.cstar.engine.application

import org.springframework.stereotype.Service
import java.util.TreeMap

@Service
class RankingService() {
    fun generateRanking(ranking: TreeMap<Long, Int>, nicknames: Map<Long, String>): String {
        val sortedRankingDescending = ranking.entries.sortedByDescending { it.value }
        val result = StringBuilder()
        for ((index, entry) in sortedRankingDescending.withIndex()) {
            val playerNickname = nicknames[entry.key]
            val score = entry.value
            result.append("[${index + 1}등 $playerNickname-$score]  ")
        }
        return result.toString()
    }

    fun calculateGameResult(ranking: TreeMap<Long, Int>): Long {
        return ranking.maxByOrNull { it.value }?.key ?: -1
    }
}
