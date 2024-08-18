package yjh.cstar.room.application.port

import yjh.cstar.room.domain.Room

interface RoomRepository {
    fun save(room: Room): Room
}
