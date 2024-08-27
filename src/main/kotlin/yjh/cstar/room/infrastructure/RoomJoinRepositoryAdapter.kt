package yjh.cstar.room.infrastructure

import org.springframework.stereotype.Repository
import yjh.cstar.room.application.port.RoomJoinRepository
import yjh.cstar.room.domain.RoomJoin
import yjh.cstar.room.infrastructure.jpa.RoomJoinEntity
import yjh.cstar.room.infrastructure.jpa.RoomJoinJpaRepository

@Repository
class RoomJoinRepositoryAdapter(
    private val roomJoinJpaRepository: RoomJoinJpaRepository,
) : RoomJoinRepository {
    override fun save(roomJoin: RoomJoin): RoomJoin {
        return roomJoinJpaRepository.save(RoomJoinEntity.from(roomJoin)).toModel()
    }
}
