package yjh.cstar.game.domain

import java.time.LocalDateTime

class GameResult(
    val id: Long = 0,
    val gameId: Long,
    val playerId: Long,
    var totalCount: Int,
    var correctCount: Int,
    var ranking: Int,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
)
