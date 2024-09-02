package yjh.cstar.room.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException
import java.time.LocalDateTime

class Room(
    val id: Long = 0,
    val maxCapacity: Int,
    var currCapacity: Int = 0,
    var status: RoomStatus = RoomStatus.WAITING,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun create(command: RoomCreateCommand): Room {
            return Room(
                maxCapacity = command.maxCapacity
            )
        }
    }

    fun entrance() {
        checkWaitingStatus()
        checkCapacity()
        currCapacity++
    }

    fun leave() {
        require(currCapacity != 0) { BaseException(BaseErrorCode.CAPACITY_CANNOT_BE_NEGATIVE) }
        currCapacity--
    }

    fun startGame() {
        checkWaitingStatus()
        status = RoomStatus.IN_PROGRESS
    }

    fun endGameAndResetRoom() {
        resetStatusIfInProgress()
        currCapacity = 0
    }

    private fun resetStatusIfInProgress() {
        status = RoomStatus.WAITING.takeIf { status == RoomStatus.IN_PROGRESS } ?: status
    }

    private fun checkCapacity() {
        require(currCapacity <= maxCapacity - 1) {
            throw BaseException(BaseErrorCode.CAPACITY_EXCEEDED)
        }
    }

    private fun checkWaitingStatus() {
        require(status == RoomStatus.WAITING) {
            throw BaseException(BaseErrorCode.NOT_IN_WAITING_STATUS)
        }
    }
}
