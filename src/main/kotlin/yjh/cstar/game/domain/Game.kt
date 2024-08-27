package yjh.cstar.game.domain

import java.time.LocalDateTime

class Game(
    val id: Long = 0,
    val roomId: Long,
    val winnerId: Long,
    var totalQuizCount: Int,
    var startedAt: LocalDateTime,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
)
