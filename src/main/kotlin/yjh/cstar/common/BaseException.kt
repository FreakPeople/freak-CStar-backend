package yjh.cstar.common

class BaseException(
    val baseErrorCode: BaseErrorCode,
) : RuntimeException(baseErrorCode.message)
