package yjh.cstar.member.domain

import yjh.cstar.common.BaseErrorCode
import yjh.cstar.room.domain.Validator

class MemberCreateCommand(
    val email: String,
    val password: String,
    val nickname: String,
) {
    init {
        Validator.validate(password.length <= Validator.MAX_PASSWORD) { BaseErrorCode.PASSWORD_OUT_OF_LENGTH }
        Validator.validate(password.length >= Validator.MIN_PASSWORD) { BaseErrorCode.PASSWORD_OUT_OF_LENGTH }
        Validator.validate(nickname.length <= Validator.MAX_NICKNAME) { BaseErrorCode.NICKNAME_OUT_OF_LENGTH }
        Validator.validate(nickname.length >= Validator.MIN_NICKNAME) { BaseErrorCode.NICKNAME_OUT_OF_LENGTH }
    }
}
