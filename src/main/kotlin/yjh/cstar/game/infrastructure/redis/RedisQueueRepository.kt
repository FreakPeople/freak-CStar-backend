package yjh.cstar.game.infrastructure.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RedisQueueRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun add(key: String, value: String): Long? {
        return redisTemplate.opsForList().rightPush(key, value)
    }

    fun poll(key: String): String? {
        return redisTemplate.opsForList().leftPop(key)
    }

    fun getSize(key: String): Long? {
        return redisTemplate.opsForList().size(key)
    }

    fun deleteAll(key: String) {
        redisTemplate.delete(key)
    }
}
