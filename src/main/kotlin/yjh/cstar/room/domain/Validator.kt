package yjh.cstar.room.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException

class Validator {
    companion object {
        const val MIN_NICKNAME = 2
        const val MAX_NICKNAME = 15
        const val MIN_PASSWORD = 5
        const val MAX_PASSWORD = 15
        const val MIN_CAPACITY = 2
        const val MAX_CAPACITY = 5

        fun validate(value: Boolean, lazyErrorCode: () -> BaseErrorCode) {
            if (!value) throw BaseException(lazyErrorCode())
        }
    }
}
