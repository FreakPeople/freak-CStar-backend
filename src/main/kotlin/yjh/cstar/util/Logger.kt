package yjh.cstar.util

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class Logger {
    companion object {
        private val logger = KotlinLogging.logger {}

        fun info(message: String) {
            logger.info { message }
        }

        fun error(message: String) {
            logger.error { message }
        }

        fun error(message: Any) {
            logger.error { message }
        }
    }
}
