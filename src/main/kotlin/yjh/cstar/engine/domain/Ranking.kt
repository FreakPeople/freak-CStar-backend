package yjh.cstar.engine.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import java.util.*

class Ranking(
    private val players: List<Long>,
    val ranking: TreeMap<Long, Int> = TreeMap<Long, Int>().apply {
        players.forEach { this[it] = 0 }
    },
) {

    fun updateScore(playerId: Long) {
        val score = ranking.getOrDefault(playerId, 0)
        ranking[playerId] = score + 1
    }

    fun getWinnerId(): Long {
        val (playerId, score) = ranking.maxByOrNull { it.value }
            ?: throw BaseException(BaseErrorCode.INTERNAL_SERVER_ERROR)

        return when {
            score == 0 -> -1
            else -> playerId
        }
    }

    fun sortByScore(): TreeMap<Long, Int> {
        val sortedEntries = ranking.entries.sortedByDescending { it.value }

        val sortedMap = TreeMap<Long, Int>()
        for (entry in sortedEntries) {
            sortedMap[entry.value.toLong()] = entry.key.toInt()
        }
        return sortedMap
    }
}
