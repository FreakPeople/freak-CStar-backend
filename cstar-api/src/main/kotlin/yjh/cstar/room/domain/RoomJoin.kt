package yjh.cstar.room.domain

class RoomJoin(
    val id: Long = 0,
    val roomId: Long,
    val playerId: Long,
) {
    companion object {
        fun create(roomId: Long, playerId: Long): RoomJoin {
            return RoomJoin(
                roomId = roomId,
                playerId = playerId
            )
        }
    }
}
