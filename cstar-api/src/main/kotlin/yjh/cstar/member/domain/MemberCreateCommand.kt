package yjh.cstar.member.domain

import yjh.cstar.common.ApiErrorCode

class MemberCreateCommand(
    val email: String,
    val password: String,
    val nickname: String,
) {
    init {
        Validator.validate(password.length <= Validator.MAX_PASSWORD) { ApiErrorCode.PASSWORD_OUT_OF_LENGTH }
        Validator.validate(password.length >= Validator.MIN_PASSWORD) { ApiErrorCode.PASSWORD_OUT_OF_LENGTH }
        Validator.validate(nickname.length <= Validator.MAX_NICKNAME) { ApiErrorCode.NICKNAME_OUT_OF_LENGTH }
        Validator.validate(nickname.length >= Validator.MIN_NICKNAME) { ApiErrorCode.NICKNAME_OUT_OF_LENGTH }
    }
}
