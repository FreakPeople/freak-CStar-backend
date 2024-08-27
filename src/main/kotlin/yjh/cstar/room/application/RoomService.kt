package yjh.cstar.room.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.room.application.port.RoomRepository
import yjh.cstar.room.domain.Room
import yjh.cstar.room.domain.RoomCreateCommand
import yjh.cstar.room.infrastructure.jpa.RoomJoinJpaRepository

@Transactional(readOnly = true)
@Service
class RoomService(
    private val roomRepository: RoomRepository,
    private val roomJoinJpaRepository: RoomJoinJpaRepository,
) {

    fun retrieve(id: Long): Room {
        return roomRepository.findByIdOrNull(id) ?: throw BaseException(BaseErrorCode.NOT_FOUND_ROOM)
    }

    @Transactional
    fun create(command: RoomCreateCommand): Long {
        val room = Room.create(command)
        return roomRepository.save(room).id
    }

    @Transactional
    fun join(roomId: Long, memberId: Long): Long {
        val room = retrieve(roomId)
        room.entrance()
        roomRepository.save(room)
        return roomId
    }

    @Transactional
    fun startGame(roomId: Long) {
        val room = retrieve(roomId)
        room.startGame()
        roomRepository.save(room)
    }
}
