package yjh.cstar.common.util.logging2

import java.util.*

class TraceId(
    val uuid: String = UUID.randomUUID().toString().substring(0, 8),
    val level: Int = 0,
) {
    companion object {
        fun new(): TraceId {
            return TraceId()
        }
    }

    fun nextTraceId(): TraceId {
        return TraceId(uuid, level + 1)
    }

    fun prevTraceId(): TraceId {
        return TraceId(uuid, level - 1)
    }

    fun isFirstLevel(): Boolean {
        return this.level == 0
    }
}
