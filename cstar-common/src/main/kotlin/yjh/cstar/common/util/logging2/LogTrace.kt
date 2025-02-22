package yjh.cstar.common.util.logging2

import org.slf4j.Logger

interface LogTrace {
    fun begin(message: String, logger: Logger): TraceStartInfo

    fun end(status: TraceStartInfo, logger: Logger)

    fun exception(message: String, logger: Logger)
}
