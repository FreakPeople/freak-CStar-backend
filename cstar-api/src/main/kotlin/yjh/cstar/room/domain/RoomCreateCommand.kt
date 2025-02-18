package yjh.cstar.room.domain

import yjh.cstar.common.ApiErrorCode
import yjh.cstar.room.domain.Validator.Companion.MAX_CAPACITY
import yjh.cstar.room.domain.Validator.Companion.MIN_CAPACITY
import yjh.cstar.room.domain.Validator.Companion.validate

class RoomCreateCommand(
    val maxCapacity: Int,
    val ownerId: Long,
) {
    init {
        validate(maxCapacity <= MAX_CAPACITY) { ApiErrorCode.MAX_CAPACITY_OUT_OF_RANGE }
        validate(maxCapacity >= MIN_CAPACITY) { ApiErrorCode.MAX_CAPACITY_OUT_OF_RANGE }
    }
}
