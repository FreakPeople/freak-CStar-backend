package yjh.cstar.game.presentation.request

import java.time.LocalDateTime
import java.util.*

data class RankingCreateRequest(
    val sortedRanking: TreeMap<Long, Int>,
    val roomId: Long,
    val winningPlayerId: Long,
    val totalQuizSize: Int,
    val categoryId: Long,
    val gameStartedAt: LocalDateTime,
)
