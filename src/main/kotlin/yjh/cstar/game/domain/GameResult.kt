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
) {

    fun update(command: GameResult) {
        this.totalCount = command.totalCount
        this.correctCount = command.correctCount
        this.ranking = command.ranking
        this.updatedAt = LocalDateTime.now()
    }

    fun updateCorrectCount(playerId: Long) {
        if (this.playerId == playerId) {
            correctCount += 1
            updatedAt = LocalDateTime.now()
        }
    }
}
