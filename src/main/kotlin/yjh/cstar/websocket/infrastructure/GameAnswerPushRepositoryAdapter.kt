package yjh.cstar.websocket.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import yjh.cstar.engine.infrastructure.redis.AnswerResultEntity
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.util.RedisUtil
import yjh.cstar.websocket.application.port.GameAnswerPushRepository

@Repository
class GameAnswerPushRepositoryAdapter(
    private val redisUtil: RedisUtil,
    private val objectMapper: ObjectMapper,
) : GameAnswerPushRepository {

    override fun push(answerResult: AnswerResult) {
        val roomId = answerResult.roomId
        val quizId = answerResult.quizId
        val key = "roomId : " + roomId.toString() + ", " + "quizId : " + quizId.toString()
        val answerResultEntity = AnswerResultEntity.from(answerResult)
        val value = objectMapper.writeValueAsString(answerResultEntity)
        redisUtil.rpush(key, value)
    }
}
