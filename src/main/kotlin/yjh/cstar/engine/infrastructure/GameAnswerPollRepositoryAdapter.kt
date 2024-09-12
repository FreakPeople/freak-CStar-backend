package yjh.cstar.engine.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import yjh.cstar.engine.application.port.GameAnswerPollRepository
import yjh.cstar.engine.infrastructure.redis.AnswerResultEntity
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.util.RedisUtil

@Repository
class GameAnswerPollRepositoryAdapter(
    private val redisUtil: RedisUtil,
    private val objectMapper: ObjectMapper,
) : GameAnswerPollRepository {

    override fun poll(roomId: Long, quizId: Long): AnswerResult? {
        val key = "roomId : " + roomId.toString() + ", " + "quizId : " + quizId.toString()
        return redisUtil.lpop(key, 1)?.let {
            objectMapper.readValue(it, AnswerResultEntity::class.java)?.toModel()
        }
    }
}
