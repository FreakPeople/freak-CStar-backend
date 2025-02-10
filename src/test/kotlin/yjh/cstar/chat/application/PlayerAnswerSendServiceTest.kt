package yjh.cstar.chat.application

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.transaction.annotation.Transactional
import yjh.cstar.IntegrationTest
import yjh.cstar.chat.domain.PlayerAnswer
import yjh.cstar.chat.infrastructure.RedisAnswerMessageBroker
import yjh.cstar.util.RedisUtil
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@Transactional
@DisplayName("[Application 테스트] PlayerAnswerSendService")
class PlayerAnswerSendServiceTest : IntegrationTest() {

    @Autowired
    private lateinit var redisUtil: RedisUtil

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var playerAnswerSendService: PlayerAnswerSendService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        private const val ROOM_ID = 1L
        private const val QUIZ_ID = 1L
        private val KEY = RedisAnswerMessageBroker.getKey(ROOM_ID, QUIZ_ID)
    }

    @BeforeTest
    fun beforeEach() {
        redisUtil.delete(KEY)
    }

    @AfterTest
    fun afterEach() {
        redisUtil.delete(KEY)
    }

    @Test
    fun `플레이어 정답 선착순 제출 테스트`() {
        // given
        val answerResult = PlayerAnswer(
            answer = "answer",
            roomId = ROOM_ID,
            quizId = QUIZ_ID,
            playerId = 1,
            nickname = "nickname"
        )

        // when
        assertDoesNotThrow { playerAnswerSendService.send(answerResult) }

        // then
        val size = redisTemplate.opsForList().size(KEY)
        assertEquals(1, size)
    }
}
