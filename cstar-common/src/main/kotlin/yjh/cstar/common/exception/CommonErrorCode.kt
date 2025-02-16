package yjh.cstar.common.exception

import org.springframework.http.HttpStatus

enum class CommonErrorCode(
    override val httpStatus: HttpStatus,
    override val code: Int,
    override val message: String,
) : BaseErrorCode {
    SUCCESS(HttpStatus.OK, 200, "요청 성공"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 에러"),
}
