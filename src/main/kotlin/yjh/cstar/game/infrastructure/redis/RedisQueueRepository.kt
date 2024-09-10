package yjh.cstar.game.infrastructure.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisQueueRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun add(key: String, value: String): Long? {
        return redisTemplate.opsForList().rightPush(key, value)
    }

    fun poll(key: String, timeout: Long = 60, timeUnit: TimeUnit = TimeUnit.SECONDS): String? {
        return redisTemplate.opsForList().leftPop(key, timeout, timeUnit)
    }

    fun getSize(key: String): Long? {
        return redisTemplate.opsForList().size(key)
    }

    fun deleteAll(key: String) {
        redisTemplate.delete(key)
    }
}
