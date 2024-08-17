package yjh.cstar.room.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.room.domain.Validator.Companion.MAX_CAPACITY
import yjh.cstar.room.domain.Validator.Companion.MIN_CAPACITY
import yjh.cstar.room.domain.Validator.Companion.validate

class RoomCreateCommand(
    val maxCapacity: Int,
) {
    init {
        validate(maxCapacity <= MAX_CAPACITY) { BaseErrorCode.MAX_CAPACITY_OUT_OF_RANGE }
        validate(maxCapacity >= MIN_CAPACITY) { BaseErrorCode.MAX_CAPACITY_OUT_OF_RANGE }
    }
}
