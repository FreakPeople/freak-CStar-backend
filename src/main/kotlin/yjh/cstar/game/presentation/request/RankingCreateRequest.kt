package yjh.cstar.game.presentation.request

import java.time.LocalDateTime

data class RankingCreateRequest(
    val ranking: List<Pair<String?, Double?>>,
    val roomId: Long,
    val winningPlayerId: Long,
    val totalQuizSize: Int,
    val categoryId: Long,
    val gameStartedAt: LocalDateTime,
)
