package yjh.cstar.websocket.application

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import yjh.cstar.util.RedisUtil
import yjh.cstar.websocket.domain.PlayerAnswer
import yjh.cstar.websocket.infrastructure.RedisAnswerMessageBroker
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@ActiveProfiles("local-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("[Application 테스트] PlayerAnswerSendService")
class PlayerAnswerSendServiceTest {

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

        private val redis: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379)
            .withReuse(true)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", redis::getHost)
            registry.add("spring.data.redis.port", redis::getFirstMappedPort)
        }

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            redis.start()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            redis.stop()
        }
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
