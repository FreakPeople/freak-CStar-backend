package yjh.cstar.common.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import yjh.cstar.common.response.ErrorResponse
import yjh.cstar.common.util.logging.Logger

@RestControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        Logger.error("[ERROR] $e")
        val errorCode = e.errorCode
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
                status = CommonErrorCode.INTERNAL_SERVER_ERROR,
                code = 500,
                message = "서버 내부 에러"
            )
        )
    }
}
