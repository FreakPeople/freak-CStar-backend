package yjh.cstar.room.infrastructure.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Table(name = "room_join")
@Entity
class RoomJoinEntity(
    @Id
    @Column(name = "room_join_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "room_id", nullable = false)
    private val roomId: Long,

    @Column(name = "member_id", nullable = false)
    private val playerId: Long,

    @Column(name = "joined_at", nullable = false)
    private var joinedAt: LocalDateTime?,
)
