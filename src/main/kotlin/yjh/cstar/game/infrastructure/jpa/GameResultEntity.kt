package yjh.cstar.game.infrastructure.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Table(name = "member_game_result")
@Entity
class GameResultEntity(
    @Id
    @Column(name = "member_game_result_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "game_id", nullable = false)
    private val gameId: Long,

    @Column(name = "member_id", nullable = false)
    private val playerId: Long,

    @Column(name = "total_count", nullable = false)
    private val totalCount: Int,

    @Column(name = "correct_count", nullable = false)
    private val correctCount: Int,

    @Column(name = "ranking", nullable = false)
    private val ranking: Int,

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private var createdAt: LocalDateTime?,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private var updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    private val deletedAt: LocalDateTime? = null,
)
