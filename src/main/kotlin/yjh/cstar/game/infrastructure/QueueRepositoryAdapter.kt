package yjh.cstar.game.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import yjh.cstar.game.application.port.QueueService
import yjh.cstar.game.domain.AnswerResult
import yjh.cstar.game.infrastructure.redis.AnswerResultEntity
import yjh.cstar.game.infrastructure.redis.RedisQueueRepository

@Repository
class QueueRepositoryAdapter(
    private val redisQueueRepository: RedisQueueRepository,
    private val objectMapper: ObjectMapper,
) : QueueService {
    override fun add(answerResult: AnswerResult) {
        val roomId = answerResult.roomId
        val quizId = answerResult.quizId
        val key = "roomId : " + roomId.toString() + ", " + "quizId : " + quizId.toString()
        val answerResultEntity = AnswerResultEntity.from(answerResult)
        val value = objectMapper.writeValueAsString(answerResultEntity)
        redisQueueRepository.add(key, value)
    }

    override fun poll(roomId: Long, quizId: Long): AnswerResult? {
        val key = "roomId : " + roomId.toString() + ", " + "quizId : " + quizId.toString()
        return redisQueueRepository.poll(key, 1)?.let {
            objectMapper.readValue(it, AnswerResultEntity::class.java)?.toModel()
        }
    }
}
