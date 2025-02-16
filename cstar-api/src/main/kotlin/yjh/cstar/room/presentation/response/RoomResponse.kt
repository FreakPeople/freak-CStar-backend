package yjh.cstar.room.presentation.response

import yjh.cstar.room.domain.Room
import yjh.cstar.room.domain.RoomStatus
import java.time.LocalDateTime

data class RoomResponse(
    val id: Long,
    val maxCapacity: Int,
    val currCapacity: Int,
    val status: RoomStatus,
    var createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun from(room: Room): RoomResponse {
            return RoomResponse(
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
}
