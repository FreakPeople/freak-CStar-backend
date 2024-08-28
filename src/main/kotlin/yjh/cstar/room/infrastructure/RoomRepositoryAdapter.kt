package yjh.cstar.room.infrastructure

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import yjh.cstar.room.application.port.RoomRepository
import yjh.cstar.room.domain.Room
import yjh.cstar.room.infrastructure.jpa.RoomEntity
import yjh.cstar.room.infrastructure.jpa.RoomJpaRepository

@Repository
class RoomRepositoryAdapter(
    private val roomJpaRepository: RoomJpaRepository,
) : RoomRepository {

    override fun save(room: Room): Room {
        return roomJpaRepository.save(RoomEntity.from(room)).toModel()
    }

    override fun findAll(pageable: Pageable): Page<Room> {
        return roomJpaRepository.findAll(pageable).map { it.toModel() }
    }

    override fun findByIdOrNull(id: Long): Room? {
        return roomJpaRepository.findByIdOrNull(id)?.toModel()
    }
}
