package yjh.cstar.common.exception

class BaseException(
    val errorCode: BaseErrorCode,
) : RuntimeException(errorCode.message)
