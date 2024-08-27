package yjh.cstar.room.application.port

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import yjh.cstar.room.domain.Room

interface RoomRepository {
    fun save(room: Room): Room

    fun findAll(pageable: Pageable): Page<Room>

    fun findByIdOrNull(id: Long): Room?
}
