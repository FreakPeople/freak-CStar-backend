package yjh.cstar.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@EnableAsync
@Configuration
class AsyncConfig {

    @Value("\${game.engine.threads.core}")
    lateinit var core: String

    @Value("\${game.engine.threads.max}")
    lateinit var max: String

    @Bean("GameEngineThreadPool")
    fun gameEngineThreadPool(): ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = core.toInt()
            maxPoolSize = max.toInt()
        }
    }
}
