package yjh.cstar.common.exception

/**
 * BaseException을 통해서 예외를 발생시킵니다
 */
class BaseException(
    val errorCode: BaseErrorCode,
) : RuntimeException(errorCode.message)
