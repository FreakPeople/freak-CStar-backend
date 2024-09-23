package yjh.cstar.engine.infrastructure

import org.springframework.stereotype.Service
import yjh.cstar.engine.application.port.RankingHandler
import yjh.cstar.engine.domain.player.Players
import yjh.cstar.engine.domain.ranking.Ranking
import yjh.cstar.util.RedisUtil

@Service
class RedisRankingHandler(
    private val redisUtil: RedisUtil,
) : RankingHandler {

    companion object {
        val KEY_PREFIX = "quiz-game-score:"
        val VALUE_PREFIX = "player:"
        val INIT_SCORE = 0
        val INCREASE_SCORE = 1
    }

    override fun init(roomId: Long, players: Players) {
        val key = KEY_PREFIX + roomId
        redisUtil.delete(key)
        val ids: List<Long> = players.getPlayerIds()
        ids.forEach {
            redisUtil.zadd(key, VALUE_PREFIX + it, INIT_SCORE.toDouble())
        }
    }

    override fun increaseScore(roomId: Long, playerId: Long) {
        val key = KEY_PREFIX + roomId
        val value = VALUE_PREFIX + playerId
        redisUtil.zincrby(key, value, INCREASE_SCORE.toDouble())
    }

    override fun getWinner(roomId: Long): Long {
        val key = KEY_PREFIX + roomId
        val rankings = redisUtil.zrevrange(key, 0, -1)

        if (rankings.size == 0) {
            return -1L
        }

        val winningPair = rankings.first()
        return winningPair.first
            ?.split(":")
            ?.getOrNull(1)
            ?.toLong()
            ?: -1L
    }

    override fun getRanking(roomId: Long): Ranking {
        val key = KEY_PREFIX + roomId
        val ranking = redisUtil.zrevrange(key, 0, -1) //  List<Pair<String?, Double?>>
        return Ranking.of(ranking)
    }
}
