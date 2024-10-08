package yjh.cstar.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import yjh.cstar.util.Logger

@RestControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        Logger.error("[ERROR] $e")
        val errorCode = e.baseErrorCode
        return ResponseEntity.ok(
            ErrorResponse(
                status = errorCode,
                code = errorCode.code,
                message = errorCode.message
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        Logger.error("[ERROR] $e")
        return ResponseEntity.ok(
            ErrorResponse(
                status = BaseErrorCode.INTERNAL_SERVER_ERROR,
                code = BaseErrorCode.INTERNAL_SERVER_ERROR.code,
                message = BaseErrorCode.INTERNAL_SERVER_ERROR.message
            )
        )
    }
}
