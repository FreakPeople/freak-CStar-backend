package yjh.cstar.engine.application

import org.springframework.stereotype.Service
import java.util.TreeMap

@Service
class ScoreManagementService {
    fun updateScore(ranking: TreeMap<Long, Int>, playerId: Long) {
        val score = ranking.getOrDefault(playerId, 0)
        ranking[playerId] = score + 1
    }
}
