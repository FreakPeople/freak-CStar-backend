package yjh.cstar.room.infrastructure.jpa

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
import yjh.cstar.room.domain.Room
import yjh.cstar.room.domain.RoomStatus
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@Table(name = "room")
@Entity
class RoomEntity(
    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column(name = "max_capacity", nullable = false)
    private val maxCapacity: Int,

    @Column(name = "curr_capacity", nullable = false)
    private val currCapacity: Int,

    @Column(name = "status", nullable = false)
    private val status: RoomStatus,

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private var createdAt: LocalDateTime?,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private var updatedAt: LocalDateTime?,

    @Column(name = "deleted_at")
    private val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun from(room: Room): RoomEntity {
            return RoomEntity(
                id = room.id,
                maxCapacity = room.maxCapacity,
                currCapacity = room.currCapacity,
                status = room.status,
                createdAt = room.createdAt,
                updatedAt = room.updatedAt,
                deletedAt = room.deletedAt
            )
        }
    }

    fun toModel(): Room {
        return Room(
            id = this.id,
            maxCapacity = this.maxCapacity,
            currCapacity = this.currCapacity,
            status = this.status,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }
}
