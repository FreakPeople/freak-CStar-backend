package yjh.cstar.common.util.logging2

import org.slf4j.Logger
import org.springframework.stereotype.Component

@Component
class RequestLogTrace(
    val traceIdHolder: ThreadLocal<TraceId> = ThreadLocal(),
) : LogTrace {
    companion object {
        val START_PREFIX = "-->"
        val END_PREFIX = "<--"
        val EXCEPTION_PREFIX = "<X-"
    }

    override fun begin(message: String, logger: Logger): TraceStartInfo {
        val traceId = traceIdHolder.get()
        if (traceId == null) {
            traceIdHolder.set(TraceId.new())
        } else {
            traceIdHolder.set(traceId.nextTraceId())
        }

        val currTraceId = traceIdHolder.get()
        val startTimeMs = System.currentTimeMillis()
        logger.info("[{}] {}{}", currTraceId.uuid, "  ".repeat(currTraceId.level) + "|" + START_PREFIX, message)

        return TraceStartInfo(currTraceId, startTimeMs, message)
    }

    override fun end(startStatus: TraceStartInfo, logger: Logger) {
        val traceId = startStatus.traceId
        val intervalTimeMs = System.currentTimeMillis() - startStatus.startTime

        logger.info(
            "[{}] {}{} time={}ms",
            traceId.uuid,
            "  ".repeat(traceId.level) + "|" + END_PREFIX,
            startStatus.message,
            intervalTimeMs
        )

        if (traceId.isFirstLevel()) {
            traceIdHolder.remove()
        } else {
            traceIdHolder.set(traceId.prevTraceId())
        }
    }

    override fun exception(startStatus: TraceStartInfo, e: Exception, logger: Logger) {
        val traceId = startStatus.traceId
        val intervalTimeMs = System.currentTimeMillis() - startStatus.startTime

        logger.info(
            "[{}] {}{} time={}ms exception={}",
            traceId.uuid,
            "  ".repeat(traceId.level) + "|" + EXCEPTION_PREFIX,
            startStatus.message,
            intervalTimeMs,
            e.message
        )

        traceIdHolder.set(traceId.prevTraceId())
    }

    fun removeTraceHolder() {
        traceIdHolder.remove()
    }

    fun getTraceId(): TraceId {
        return traceIdHolder.get()
    }
}
