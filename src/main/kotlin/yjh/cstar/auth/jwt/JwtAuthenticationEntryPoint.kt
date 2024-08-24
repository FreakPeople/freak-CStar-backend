package yjh.cstar.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import yjh.cstar.common.BaseErrorCode
import yjh.cstar.common.ErrorResponse

private val logger = KotlinLogging.logger {}

@Component
class JwtAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        logger.error { "[ERROR] ${authException.message}" }
        println("야 ㅁ너 머야?")

        val errorData = ErrorResponse(
            status = BaseErrorCode.UNAUTHORIZED,
            code = BaseErrorCode.UNAUTHORIZED.code,
            message = BaseErrorCode.UNAUTHORIZED.message
        )

        response.setContentType("application/json")
        response.setCharacterEncoding("UTF-8")
        response.setStatus(401)
        response.getWriter().write(objectMapper.writeValueAsString(errorData))
    }
}
