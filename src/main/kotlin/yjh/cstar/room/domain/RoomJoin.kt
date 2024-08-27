package yjh.cstar.room.domain

import java.time.LocalDateTime

class RoomJoin(
    val id: Long = 0,
    val roomId: Long,
    val playerId: Long,
    var joinedAt: LocalDateTime? = null,
) {
    init {
        joinedAt = LocalDateTime.now()
    }
}
