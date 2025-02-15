package yjh.cstar.member.domain

import yjh.cstar.common.ApiErrorCode
import yjh.cstar.common.exception.BaseException

class Validator {
    companion object {
        const val MIN_NICKNAME = 2
        const val MAX_NICKNAME = 15
        const val MIN_PASSWORD = 5
        const val MAX_PASSWORD = 15

        fun validate(value: Boolean, lazyErrorCode: () -> ApiErrorCode) {
            if (!value) throw BaseException(lazyErrorCode())
        }
    }
}
