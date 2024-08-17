package yjh.cstar.room.application

import org.springframework.stereotype.Service
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
}
