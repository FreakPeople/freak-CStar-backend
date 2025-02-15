package yjh.cstar.common.exception

import org.springframework.http.HttpStatus

interface BaseErrorCode {
    val message: String
    val httpStatus: HttpStatus
    val code: Int
}
