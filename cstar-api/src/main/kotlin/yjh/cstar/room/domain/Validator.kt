package yjh.cstar.room.domain

import yjh.cstar.common.ApiErrorCode
import yjh.cstar.common.exception.BaseException

class Validator {
    companion object {
        const val MIN_CAPACITY = 2
        const val MAX_CAPACITY = 5

        fun validate(value: Boolean, lazyErrorCode: () -> ApiErrorCode) {
            if (!value) throw BaseException(lazyErrorCode())
        }
    }
}
