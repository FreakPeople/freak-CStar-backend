package yjh.cstar.play.infrastructure

import org.springframework.stereotype.Service
import yjh.cstar.play.application.port.RankingHandler
import yjh.cstar.play.domain.player.Players
import yjh.cstar.play.domain.ranking.Ranking
import yjh.cstar.util.RedisUtil

@Service
class RedisRankingHandler(
    private val redisUtil: RedisUtil,
) : RankingHandler {

    companion object {
        const val KEY_PREFIX = "quiz-game-score:"
        const val VALUE_PREFIX = "player:"
        const val INIT_SCORE = 0
        const val INCREASE_SCORE = 1

        fun getPlayerIdOf(value: String) = value.split(":")[1].toLong()
    }

    override fun initRankingBoard(roomId: Long, players: Players) {
        val key = getKey(roomId)
        redisUtil.delete(key)
        val ids: List<Long> = players.getPlayerIds()
        ids.forEach {
            redisUtil.zadd(key, getValue(it), INIT_SCORE.toDouble())
        }
    }

    override fun assignScoreToPlayer(roomId: Long, playerId: Long) {
        val key = getKey(roomId)
        val value = getValue(playerId)
        redisUtil.zincrby(key, value, INCREASE_SCORE.toDouble())
    }

    override fun getWinner(roomId: Long): Long {
        val key = getKey(roomId)
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
        val key = getKey(roomId)
        val ranking = redisUtil.zrevrange(key, 0, -1) //  List<Pair<String?, Double?>>
        return Ranking.of(ranking)
    }

    private fun getKey(roomId: Long) = KEY_PREFIX + roomId

    private fun getValue(playerId: Long) = VALUE_PREFIX + playerId
}
