package yjh.cstar.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.ErrorResponse
import yjh.cstar.util.Logger

@Component
class JwtAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        Logger.error("[ERROR] ${accessDeniedException.message}")

        val errorData = ErrorResponse(
            status = BaseErrorCode.FORBIDDEN,
            code = BaseErrorCode.FORBIDDEN.code,
            message = BaseErrorCode.FORBIDDEN.message
        )

        response.apply {
            contentType = "application/json"
            characterEncoding = "UTF-8"
            status = 403
            writer.write(objectMapper.writeValueAsString(errorData))
        }
    }
}
