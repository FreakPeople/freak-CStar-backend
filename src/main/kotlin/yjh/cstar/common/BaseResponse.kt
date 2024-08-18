package yjh.cstar.common

import java.time.LocalDateTime

data class Response<T>(
    val status: BaseErrorCode = BaseErrorCode.SUCCESS,
    val code: Int = BaseErrorCode.SUCCESS.code,
    val message: String = BaseErrorCode.SUCCESS.message,
    val data: T? = null,
)

data class ErrorResponse(
    val status: BaseErrorCode,
    val code: Int,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
