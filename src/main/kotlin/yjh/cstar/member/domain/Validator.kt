package yjh.cstar.member.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.BaseException

class Validator {
    companion object {
        const val MIN_NICKNAME = 2
        const val MAX_NICKNAME = 15
        const val MIN_PASSWORD = 5
        const val MAX_PASSWORD = 15

        fun validate(value: Boolean, lazyErrorCode: () -> BaseErrorCode) {
            if (!value) throw BaseException(lazyErrorCode())
        }
    }
}
