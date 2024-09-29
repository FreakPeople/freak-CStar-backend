package yjh.cstar.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import yjh.cstar.util.Logger

class JwtFilter(
    private val tokenProvider: TokenProvider,
) : GenericFilterBean() {

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val TOKEN_TYPE_BEARER = "Bearer "
    }

    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain,
    ) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val jwt = resolveToken(httpServletRequest)
        val requestURI = httpServletRequest.requestURI

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            val authentication = tokenProvider.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
            Logger.info("Security Context에 ${authentication.name} 인증 정보를 저장했습니다, uri: $requestURI")
        } else {
            Logger.info("유효한 JWT 토큰이 없습니다, uri: $requestURI")
        }

        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_TYPE_BEARER)) {
            return bearerToken.substring(TOKEN_TYPE_BEARER.length)
        }
        return null
    }
}
