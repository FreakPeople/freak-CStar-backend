package yjh.cstar.game.domain

import java.time.LocalDateTime

data class GameResultCreateCommand(
    val ranking: LinkedHashMap<String, Int>,
    val roomId: Long,
    val winningPlayerId: Long,
    val totalQuizSize: Int,
    val categoryId: Long,
    val gameStartedAt: LocalDateTime,
)
