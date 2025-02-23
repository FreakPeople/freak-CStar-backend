package yjh.cstar.common.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import yjh.cstar.common.util.logging2.RequestLogTrace

@Order(1)
@Component
class LoggingFilter(
    private val requestLogTrace: RequestLogTrace,
) : Filter {

    companion object {
        private val PACKAGE_NAME: String = LoggingFilter::class.java.name
    }

    private val logger: Logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        request as HttpServletRequest

        val method = request.method
        val uri = request.requestURI
        val protocol = request.protocol
        val ip = request.remoteAddr

        val logMessage = "$PACKAGE_NAME $method $uri $protocol IP=$ip"
        val startTraceInfo = requestLogTrace.begin(logMessage, logger)

        chain?.doFilter(request, response)

        requestLogTrace.end(startTraceInfo, logger)
    }
}
