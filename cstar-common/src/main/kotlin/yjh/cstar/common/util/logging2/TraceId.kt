package yjh.cstar.common.util.logging2

import java.util.UUID

class TraceId(
    val uuid: String = UUID.randomUUID().toString().substring(0, 8),
    val level: Int = 0,
    val status: TraceStatus = TraceStatus.SUCCESS,
) {
    companion object {
        fun new(): TraceId {
            return TraceId()
        }

        fun changeStatusToError(): TraceId {
            return TraceId(status = TraceStatus.ERROR)
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

    fun hasErrorStatus(): Boolean {
        return this.status == TraceStatus.ERROR
    }
}
