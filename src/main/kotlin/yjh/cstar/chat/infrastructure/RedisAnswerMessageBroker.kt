package yjh.cstar.chat.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import yjh.cstar.util.RedisUtil
import yjh.cstar.chat.application.port.AnswerMessageBroker
import yjh.cstar.chat.domain.PlayerAnswer
import yjh.cstar.chat.infrastructure.redis.PlayerAnswerEntity

@Repository
class RedisAnswerMessageBroker(
    private val redisUtil: RedisUtil,
    private val objectMapper: ObjectMapper,
) : AnswerMessageBroker {

    companion object {
        private const val KEY_PREFIX_ROOM_ID = "roomId : "
        private const val KEY_PREFIX_QUIZ_ID = "quizId : "
        private const val GAME_ANSWER_QUEUE_TTL = 300L

        fun getKey(roomId: Long, quizId: Long) =
            "$KEY_PREFIX_ROOM_ID$roomId, $KEY_PREFIX_QUIZ_ID$quizId"
    }

    override fun send(playerAnswer: PlayerAnswer) {
        val key = getKey(playerAnswer.roomId, playerAnswer.quizId)

        val playerAnswerEntity = PlayerAnswerEntity.from(playerAnswer)
        val value = objectMapper.writeValueAsString(playerAnswerEntity)

        redisUtil.rpush(key, value, GAME_ANSWER_QUEUE_TTL)
    }
}
