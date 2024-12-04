package yjh.cstar.util

import java.time.LocalDateTime

class TimeUtil {

    companion object {
        fun getCurrentTime(): Long =
            System.currentTimeMillis()

        fun getCurrentLocalDateTime(): LocalDateTime =
            LocalDateTime.now()

        fun getDuration(pastTime: Long): Long =
            getCurrentTime() - pastTime
    }
}
