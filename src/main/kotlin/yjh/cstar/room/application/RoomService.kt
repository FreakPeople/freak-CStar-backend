package yjh.cstar.room.application

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import yjh.cstar.room.application.port.RoomJoinRepository
import yjh.cstar.room.application.port.RoomRepository
import yjh.cstar.room.domain.Room
import yjh.cstar.room.domain.RoomCreateCommand
import yjh.cstar.room.domain.RoomJoin

@Transactional(readOnly = true)
@Service
class RoomService(
    private val roomRepository: RoomRepository,
    private val roomJoinRepository: RoomJoinRepository,
) {

    fun retrieve(id: Long): Room {
        return roomRepository.findByIdOrNull(id) ?: throw BaseException(BaseErrorCode.NOT_FOUND_ROOM)
    }

    fun retrieveAll(pageable: Pageable): Page<Room> {
        return roomRepository.findAll(pageable)
    }

    fun retrieveCurrParticipant(roomId: Long): List<Long> {
        val room = retrieve(roomId)
        return roomJoinRepository.findCurrParticipant(roomId, room.currCapacity)
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

        val roomJoin = RoomJoin(
            roomId = roomId,
            playerId = memberId
        )
        roomJoinRepository.save(roomJoin)

        return roomId
    }

    @Transactional
    fun leave(roomId: Long) {
        val room = retrieve(roomId)
        room.leave()
        roomRepository.save(room)
    }

    @Transactional
    fun startGame(roomId: Long) {
        val room = retrieve(roomId)
        room.startGame()
        roomRepository.save(room)
    }

    @Transactional
    fun endGameAndResetRoom(roomId: Long) {
        val room = retrieve(roomId)
        room.endGameAndResetRoom()
        roomRepository.save(room)
    }
}
