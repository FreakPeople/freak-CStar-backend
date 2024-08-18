package yjh.cstar.room.infrastructure

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
}
