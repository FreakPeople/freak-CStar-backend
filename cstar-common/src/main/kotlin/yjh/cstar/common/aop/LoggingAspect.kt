package yjh.cstar.common.aop

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import yjh.cstar.common.util.logging2.LogTrace
import yjh.cstar.common.util.logging2.TraceStartInfo

@Aspect
@Configuration
class LoggingAspect(
    private val trace: LogTrace,
) {
    val logger: Logger = LoggerFactory.getLogger(LoggingAspect::class.java)
    lateinit var traceStartInfo: TraceStartInfo

    @Around("@annotation(yjh.cstar.common.aop.annotation.Logging)")
    fun logAdvice(joinPoint: ProceedingJoinPoint) {
        val traceMessage = createTraceMessage(joinPoint)
        traceStartInfo = trace.begin(traceMessage, logger)

        joinPoint.proceed()

        trace.end(traceStartInfo, logger)
    }

    @AfterThrowing("@annotation(yjh.cstar.common.aop.annotation.Logging)", throwing = "e")
    fun logExceptionAdvice(joinPoint: JoinPoint, e: Exception) {
        trace.exception(traceStartInfo, e, logger)
    }

    private fun createTraceMessage(joinPoint: ProceedingJoinPoint): String {
        val className = joinPoint.target.javaClass.name
        val methodName = joinPoint.signature.name
        return "$className.$methodName()"
    }
}
