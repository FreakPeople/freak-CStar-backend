package yjh.cstar.util

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisUtil(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    companion object {
        private const val BASE_TTL = 9999L
        private const val BASE_TIME_OUT = 60L
        private val BASE_TIME_UNIT = TimeUnit.SECONDS
    }

    fun rpush(key: String, value: String, ttl: Long = BASE_TTL): Long? {
        val listSize = redisTemplate.opsForList().rightPush(key, value)
        ttl?.let {
            redisTemplate.expire(key, ttl, BASE_TIME_UNIT)
        }
        return listSize
    }

    fun lpop(key: String, timeout: Long = BASE_TIME_OUT): String? {
        return redisTemplate.opsForList().leftPop(key, timeout, BASE_TIME_UNIT)
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
