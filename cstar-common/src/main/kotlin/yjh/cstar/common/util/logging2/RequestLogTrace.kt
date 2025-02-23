package yjh.cstar.common.util.logging2

import org.slf4j.Logger
import org.springframework.stereotype.Component
import yjh.cstar.common.constant.Icon

@Component
class RequestLogTrace(
    val traceIdHolder: ThreadLocal<TraceId> = ThreadLocal(),
) : LogTrace {
    companion object {
        val START_PREFIX = "-->"
        val END_PREFIX = "<--"
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
            "[{}] {}{} time = {}ms",
            traceId.uuid,
            "  ".repeat(traceId.level) + "|" + END_PREFIX,
            startStatus.message,
            intervalTimeMs
        )

        if (traceId.isFirstLevel()) {
            traceIdHolder.remove()
        } else {
            val prevTraceId = traceId.prevTraceId()
            traceIdHolder.set(prevTraceId)
        }
    }

    override fun exception(message: String, logger: Logger) {
        val traceId = traceIdHolder.get()
        if (traceId.hasErrorStatus()) {
            return
        }

        logger.info("[{}] ${Icon.ERROR.value} LOGGING ERROR ${Icon.ERROR.value} : {}", traceId.uuid, message)

        traceIdHolder.set(TraceId.changeStatusToError())
    }

    fun removeTraceHolder() {
        traceIdHolder.remove()
    }

    fun getTraceId(): TraceId? {
        return traceIdHolder.get()
    }
}
