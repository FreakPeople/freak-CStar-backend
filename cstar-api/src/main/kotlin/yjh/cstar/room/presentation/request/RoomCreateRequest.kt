package yjh.cstar.room.presentation.request

import yjh.cstar.room.domain.RoomCreateCommand

data class RoomCreateRequest(
    val maxCapacity: Int,
)

fun RoomCreateRequest.toCommand() = RoomCreateCommand(
    maxCapacity = maxCapacity
)
