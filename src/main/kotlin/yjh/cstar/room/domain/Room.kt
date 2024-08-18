package yjh.cstar.room.domain

import java.time.LocalDateTime

class Room(
    val id: Long = 0,
    val maxCapacity: Int,
    val currCapacity: Int = 0,
    val status: RoomStatus = RoomStatus.WAITING,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun create(command: RoomCreateCommand): Room {
            return Room(
                maxCapacity = command.maxCapacity
            )
        }
    }
}
