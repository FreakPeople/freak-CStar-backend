package yjh.cstar.common.exception

import org.springframework.http.HttpStatus

/**
 * 각 모듈에서 BaseErrorCode 인터페이스를 구현해 사용합니다
 */
interface BaseErrorCode {
    val message: String
    val httpStatus: HttpStatus
    val code: Int
}
