package yjh.cstar.engine.domain

import java.time.LocalDateTime
import java.time.ZoneId

class StartTime {
    val at = LocalDateTime.now()
    val millis = at.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    fun checkTimeOut(duration: Int): Boolean {
        return System.currentTimeMillis() - millis < duration
    }
}
