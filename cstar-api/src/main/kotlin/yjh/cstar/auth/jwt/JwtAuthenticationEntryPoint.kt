package yjh.cstar.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import yjh.cstar.common.ApiErrorCode
import yjh.cstar.common.response.ErrorResponse
import yjh.cstar.common.util.logging.Logger

@Component
class JwtAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        Logger.error("[ERROR] ${authException.message}")

        val errorData = ErrorResponse(
            status = ApiErrorCode.UNAUTHORIZED,
            code = ApiErrorCode.UNAUTHORIZED.code,
            message = ApiErrorCode.UNAUTHORIZED.message
        )

        response.apply {
            contentType = "application/json"
            characterEncoding = "UTF-8"
            status = 401
            writer.write(objectMapper.writeValueAsString(errorData))
        }
    }
}
