package yjh.cstar.common.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import yjh.cstar.common.response.ErrorResponse
import yjh.cstar.common.util.logging2.RequestLogTrace
import yjh.cstar.common.util.logging2.TraceId

@RestControllerAdvice
class GlobalControllerAdvice(
    val requestLogTrace: RequestLogTrace,
) {

    private val logger: Logger = LoggerFactory.getLogger(GlobalControllerAdvice::class.java)

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        printErrorLog(e)

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
        printErrorLog(e)

        return ResponseEntity.ok(
            ErrorResponse(
                status = CommonErrorCode.INTERNAL_SERVER_ERROR,
                code = 500,
                message = "서버 내부 에러"
            )
        )
    }

    private fun printErrorLog(e: Exception) {
        val traceId: TraceId? = requestLogTrace.getTraceId()
        if (traceId == null) {
            logger.error("UNEXPECTED ERROR : {}", e.toString())
            return
        }
        logger.error("[{}] LOGGING ERROR : {}", traceId.uuid, e.toString())
        requestLogTrace.removeTraceHolder()
    }
}
