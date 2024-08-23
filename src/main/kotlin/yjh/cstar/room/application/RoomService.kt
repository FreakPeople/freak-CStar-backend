package yjh.cstar.room.application

import org.springframework.stereotype.Service
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.room.application.port.RoomRepository
import yjh.cstar.room.domain.Room
import yjh.cstar.room.domain.RoomCreateCommand

@Service
class RoomService(
    private val roomRepository: RoomRepository,
) {
    fun create(command: RoomCreateCommand): Long {
        val room = Room.create(command)
        return roomRepository.save(room).id
    }

    fun retrieve(id: Long): Room {
        return roomRepository.findByIdOrNull(id) ?: throw BaseException(BaseErrorCode.NOT_FOUND_ROOM)
    }
}
