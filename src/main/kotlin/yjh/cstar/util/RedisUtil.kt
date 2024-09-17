package yjh.cstar.util

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisUtil(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun rpush(key: String, value: String): Long? {
        return redisTemplate.opsForList().rightPush(key, value)
    }

    fun lpop(key: String, timeout: Long = 60, timeUnit: TimeUnit = TimeUnit.SECONDS): String? {
        return redisTemplate.opsForList().leftPop(key, timeout, timeUnit)
    }

    fun size(key: String): Long? {
        return redisTemplate.opsForList().size(key)
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }

    fun zadd(key: String, value: String, score: Double) {
        redisTemplate.opsForZSet().add(key, value, score)
    }

    fun zincrby(key: String, value: String, delta: Double) {
        redisTemplate.opsForZSet().incrementScore(key, value, delta)
    }

    fun zrevrange(key: String, min: Long, max: Long): List<Pair<String?, Double?>> {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, min, max)
            ?.map { it.value to it.score }
            ?: emptyList()
    }
}
