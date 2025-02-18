package yjh.cstar.game.infrastructure.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import yjh.cstar.game.domain.Game
import yjh.cstar.game.domain.GameType
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Table(name = "game")
@Entity
class GameEntity(
    @Id
    @Column(name = "game_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "room_id", nullable = false)
    private val roomId: Long,

    @Column(name = "winner_id", nullable = false)
    private val winnerId: Long,

    @Column(name = "game_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val gameType: GameType,

    @Column(name = "total_quiz_count", nullable = false)
    private val totalQuizCount: Int,

    @Column(name = "started_at", nullable = false)
    private var startedAt: LocalDateTime,

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private var createdAt: LocalDateTime?,

    @Column(name = "deleted_at")
    private val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun from(game: Game): GameEntity {
            return GameEntity(
                id = game.id,
                roomId = game.roomId,
                winnerId = game.winnerId,
                gameType = game.gameType,
                totalQuizCount = game.totalQuizCount,
                startedAt = game.startedAt,
                createdAt = game.createdAt,
                deletedAt = game.deletedAt
            )
        }
    }
    fun toModel(): Game {
        return Game(
            id = this.id,
            roomId = this.roomId,
            winnerId = this.winnerId,
            gameType = this.gameType,
            totalQuizCount = this.totalQuizCount,
            startedAt = this.startedAt,
            createdAt = this.createdAt,
            deletedAt = this.deletedAt
        )
    }
}
