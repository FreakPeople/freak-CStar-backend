package yjh.cstar.game.presentation.request

import yjh.cstar.engine.domain.ranking.Ranking
import java.time.LocalDateTime

data class RankingCreateRequest(
    val ranking: Ranking,
    val roomId: Long,
    val winningPlayerId: Long,
    val totalQuizSize: Int,
    val categoryId: Long,
    val gameStartedAt: LocalDateTime,
)
