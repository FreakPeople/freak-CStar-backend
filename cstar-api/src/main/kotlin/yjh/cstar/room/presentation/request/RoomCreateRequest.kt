package yjh.cstar.room.presentation.request

import yjh.cstar.room.domain.RoomCreateCommand

data class RoomCreateRequest(
    val maxCapacity: Int,
    val ownerId: Long,
)

fun RoomCreateRequest.toCommand() = RoomCreateCommand(
    maxCapacity = maxCapacity,
    ownerId = ownerId
)
