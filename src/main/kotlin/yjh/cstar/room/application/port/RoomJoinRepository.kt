package yjh.cstar.room.application.port

import yjh.cstar.room.domain.RoomJoin

interface RoomJoinRepository {
    fun save(roomJoin: RoomJoin): RoomJoin
}
