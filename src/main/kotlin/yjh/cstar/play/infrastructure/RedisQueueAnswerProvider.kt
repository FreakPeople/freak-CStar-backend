package yjh.cstar.play.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import yjh.cstar.play.application.port.AnswerProvider
import yjh.cstar.play.domain.quiz.PlayerAnswer
import yjh.cstar.play.infrastructure.redis.PlayerAnswerEntity
import yjh.cstar.util.RedisUtil

@Service
class RedisQueueAnswerProvider(
    private val redisUtil: RedisUtil,
    private val objectMapper: ObjectMapper,
) : AnswerProvider {

    override fun receivePlayerAnswer(roomId: Long, quizId: Long): PlayerAnswer? {
        val key = generateKey(roomId, quizId)
        return redisUtil.lpop(key, 1)?.let {
            objectMapper.readValue(it, PlayerAnswerEntity::class.java).toModel()
        }
    }

    override fun initializePlayerAnswerToReceive(roomId: Long, quizId: Long) {
        val key = generateKey(roomId, quizId)
        redisUtil.delete(key)
    }

    private fun generateKey(roomId: Long, quizId: Long): String {
        return "roomId : $roomId, quizId : $quizId"
    }
}
