package yjh.cstar.common.response

import yjh.cstar.common.exception.BaseErrorCode
import yjh.cstar.common.exception.CommonErrorCode
import java.time.LocalDateTime

data class Response<T>(
    val status: BaseErrorCode = CommonErrorCode.SUCCESS,
    val code: Int = CommonErrorCode.SUCCESS.code,
    val message: String = CommonErrorCode.SUCCESS.message,
    val data: T? = null,
)

data class ErrorResponse(
    val status: BaseErrorCode,
    val code: Int,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
