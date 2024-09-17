package yjh.cstar.engine.application

import org.springframework.stereotype.Service
import yjh.cstar.util.RedisUtil

@Service
class RedisRankingService(
    private val redisUtil: RedisUtil,
) {

    companion object {
        val KEY_PREFIX = "quiz-game-score:"
        val VALUE_PREFIX = "player:"
        val INIT_SCORE = 0
        val INCREASE_SCORE = 1
    }

    fun init(roomId: Long, players: List<Long>) {
        val key = KEY_PREFIX + roomId
        players.forEach {
            redisUtil.zadd(key, VALUE_PREFIX + it, INIT_SCORE.toDouble())
        }
    }

    fun increaseScore(roomId: Long, playerId: Long) {
        val key = KEY_PREFIX + roomId
        val value = VALUE_PREFIX + playerId
        redisUtil.zincrby(key, value, INCREASE_SCORE.toDouble())
    }

    fun getRanking(roomId: Long): List<Pair<String?, Double?>> {
        val key = KEY_PREFIX + roomId
        return redisUtil.zrevrange(key, 0, -1)
    }

    fun getWinnerId(roomId: Long): String? {
        val key = KEY_PREFIX + roomId
        return redisUtil.zrevrange(key, 0, -1)
            .firstOrNull()?.first
    }
}
