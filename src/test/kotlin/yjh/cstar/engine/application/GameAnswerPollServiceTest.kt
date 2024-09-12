package yjh.cstar.engine.application

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import yjh.cstar.engine.infrastructure.redis.AnswerResultEntity
import yjh.cstar.util.RedisUtil
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ActiveProfiles("local-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("[Application 테스트] GameAnswerPollService")
class GameAnswerPollServiceTest {

    @Autowired
    private lateinit var redisUtil: RedisUtil

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var gameAnswerPollService: GameAnswerPollService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        private const val ROOM_ID = 1L
        private const val QUIZ_ID = 1L
        private const val KEY = "roomId : " + ROOM_ID + ", " + "quizId : " + QUIZ_ID

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
    fun `플레이어 정답 선착순 뽑아오기 테스트`() {
        // given
        val answerResultEntity = AnswerResultEntity(
            answer = "answer",
            roomId = ROOM_ID,
            quizId = QUIZ_ID,
            playerId = 1,
            nickname = "nickname"
        )
        val value = objectMapper.writeValueAsString(answerResultEntity)
        redisTemplate.opsForList().rightPush(KEY, value)

        // when
        val answerResult = gameAnswerPollService.poll(ROOM_ID, QUIZ_ID)

        // then
        assertNotNull(answerResult)
        assertEquals("answer", answerResult.answer)
        assertEquals(ROOM_ID, answerResult.roomId)
        assertEquals(QUIZ_ID, answerResult.quizId)
        assertEquals(1, answerResult.playerId)

        val size = redisTemplate.opsForList().size(KEY)
        assertEquals(0, size)
    }
}
